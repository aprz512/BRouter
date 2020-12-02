package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.FragmentAnno;
import com.aprz.brouter.annotation.ServiceAnno;
import com.example.component_api.ComponentConstants;
import com.example.component_api.ComponentUtil;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * 用于Service注解
 * 主要工作：
 * 1.找到所有被{@ServiceAnno}注解的类
 * 2.生成一个辅助类(extend ModuleFragmentImpl)
 * 3.将被注解的类注册到FragmentManager中(懒加载模式)
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.ServiceAnno"})
public class ServiceProcessor extends BaseHostProcessor {
    private static final String NAME_OF_APPLICATION = "application";
    private ClassName classNameFragmentContainer;
    private ClassName lazyLoadClassName;
    private ClassName singletonLazyLoadClassName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        final TypeElement typeElementFragmentContainer = elementUtils.getTypeElement(ComponentConstants.SERVICE_MANAGER_CALL_CLASS_NAME);
        if (typeElementFragmentContainer != null) {
            classNameFragmentContainer = ClassName.get(typeElementFragmentContainer);
        }
        final TypeElement service1TypeElement = elementUtils.getTypeElement(ComponentConstants.CALLABLE_CLASS_NAME);
        final TypeElement service2TypeElement = elementUtils.getTypeElement(ComponentConstants.SINGLETON_CALLABLE_CLASS_NAME);
        if (service1TypeElement != null) {
            lazyLoadClassName = ClassName.get(service1TypeElement);
        }
        if (service2TypeElement != null) {
            singletonLazyLoadClassName = ClassName.get(service2TypeElement);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> moduleAppElements = roundEnvironment.getElementsAnnotatedWith(ServiceAnno.class);
            parseAnnotation(moduleAppElements);
            createImpl();
            return true;
        }
        return false;
    }

    private void createImpl() {
        String className = ComponentUtil.genHostServiceClassName(componentHost);
        //pkg
        String pkg = className.substring(0, className.lastIndexOf('.'));
        //simpleName
        String cn = className.substring(className.lastIndexOf('.') + 1);

        // superClassName,这里可能为null,没有找到对应的类
        ClassName superClass = ClassName.get(elementUtils.getTypeElement(ComponentUtil.SERVICE_IMPL_CLASS_NAME));

        MethodSpec initHostMethod = generateInitHostMethod();
        MethodSpec onCreateMethod = generateOnCreateMethod();
        MethodSpec onDestroyMethod = generateOnDestroyMethod();
        TypeSpec typeSpec = TypeSpec.classBuilder(cn)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.FINAL)
                .superclass(superClass)
                .addMethod(initHostMethod)
                .addMethod(onCreateMethod)
                .addMethod(onDestroyMethod)
                .build();
        try {
            JavaFile
                    .builder(pkg, typeSpec)
                    .indent("    ")
                    .build().writeTo(mFiler);
        } catch (IOException e) {
        }
    }

    private MethodSpec generateInitHostMethod() {
        TypeName returnType = TypeName.get(mTypeElementString.asType());
        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("getName")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addStatement("return $S", componentHost);
        return openUriMethodSpecBuilder.build();
    }

    private MethodSpec generateOnCreateMethod() {
        TypeName returnType = TypeName.VOID;
        ClassName applicationName = ClassName.get(elementUtils.getTypeElement(ComponentConstants.ANDROID_APPLICATION));
        ParameterSpec parameterSpec = ParameterSpec.builder(applicationName, NAME_OF_APPLICATION)
                .build();
        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("onCreate")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addParameter(parameterSpec)
                .addModifiers(Modifier.PUBLIC);
        methodSpecBuilder.addStatement("super.onCreate(application)");

        /**
         *针对每一个被注解的fragment，生成一个辅助实例，用于向ComponentManager中注册
         * 后续客户端间接通过该实例拿到对应的fragment
         * 格式如下:
         * Function loginComponent = new Function<Bundle, LoginFragment>() {
         *             @Override
         *             public LoginFragment apply(Bundle bundle) {
         *                 if(bundle == null) {
         *                     bundle = new Bundle();
         *                 }
         *                 LoginFragment fragment =  new LoginFragment();
         *                 fragment.setArguments(bundle);
         *                 return fragment;
         *             }
         *         };
         *         FragmentManager.register("login.fragment",loginComponent);
         */
        final AtomicInteger atomicInteger = new AtomicInteger();
        serviceList.forEach(new Consumer<Element>() {

            @Override
            public void accept(Element element) {
                String serviceImplCallPath = null;
                TypeMirror serviceImplTypeMirror = null;
                TypeName serviceImplTypeName = null;

                String serviceImplClassName = element.toString();
                serviceImplTypeMirror = elementUtils.getTypeElement(serviceImplClassName).asType();
                serviceImplTypeName = TypeName.get(serviceImplTypeMirror);

                ServiceAnno anno = element.getAnnotation(ServiceAnno.class);
                String implName = "implName" + atomicInteger.incrementAndGet();

                MethodSpec.Builder getOrRawMethodBuilder = MethodSpec.methodBuilder(anno.singleTon() ? "getSingleInstance" : "get")
                        .addAnnotation(Override.class)
                        .addModifiers(anno.singleTon() ? Modifier.PROTECTED : Modifier.PUBLIC);
                String serviceName = "service" + atomicInteger.incrementAndGet();

                getOrRawMethodBuilder
                        .addStatement("$T $N = new $T($N)", serviceImplTypeName, serviceName, serviceImplTypeName, "");

                getOrRawMethodBuilder
                        .addStatement("return $N", serviceName)
                        .returns(serviceImplTypeName);
                TypeSpec innerTypeSpec = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(ParameterizedTypeName.get(singletonLazyLoadClassName, serviceImplTypeName))
                        .addMethod(getOrRawMethodBuilder.build())
                        .build();

                methodSpecBuilder.addStatement("$T $N = $L", lazyLoadClassName, implName, innerTypeSpec);

                ClassName className = ClassName.get(elementUtils.getTypeElement(getFullServiceClassName(anno)));
                if (classNameFragmentContainer != null) {
                    methodSpecBuilder.addStatement("$T.register($T.class,$N)", classNameFragmentContainer, className, implName);
                }


            }
        });
        return methodSpecBuilder.build();
    }

    private MethodSpec generateOnDestroyMethod() {
        TypeName returnType = TypeName.VOID;
        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("onDestroy")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        methodSpecBuilder.addStatement("super.onDestroy()");
        methodSpecBuilder.addComment("清空缓存");

        serviceList.forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                ServiceAnno anno = element.getAnnotation(ServiceAnno.class);
                ClassName className = ClassName.get(elementUtils.getTypeElement(getFullServiceClassName(anno)));
                methodSpecBuilder.addStatement("$T.unregister($T.class)", classNameFragmentContainer, className);
            }
        });
        return methodSpecBuilder.build();
    }


    private List<Element> serviceList = new ArrayList<>();

    /**
     * 获取注解中的目标 Service 接口的全类名
     *
     * @param anno
     * @return
     */
    private String getFullServiceClassName(ServiceAnno anno) {
        try {
            Class interceptor = anno.value();
            return interceptor.getName();
        } catch (MirroredTypesException e) {
            List<? extends TypeMirror> typeMirrors = e.getTypeMirrors();
            if (typeMirrors != null && !typeMirrors.isEmpty()) {
                return typeMirrors.get(0).toString();
            }
        }
        return "";
    }

    private void parseAnnotation(Set<? extends Element> moduleAppElements) {
        serviceList.clear();
        for (Element element : moduleAppElements) {
            ServiceAnno moduleApp = element.getAnnotation(ServiceAnno.class);
            if (moduleApp == null) {
                continue;
            }
            serviceList.add(element);
        }
    }


}
