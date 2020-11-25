package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.ComponentAppAnno;
import com.aprz.brouter.annotation.FragmentAnno;
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
import javax.lang.model.type.TypeMirror;

/**
 * 用于Fragment生命周期注解
 * 主要工作：
 * 1.找到所有被{@FragmentAnno}注解的类
 * 2.生成一个辅助类(extend ModuleFragmentImpl)
 * 3.将被注解的类注册到FragmentManager中(懒加载模式)
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.FragmentAnno"})
public class FragmentProcessor extends BaseHostProcessor {
    private static final String NAME_OF_APPLICATION = "application";
    private TypeElement bundleTypeElement;
    private TypeName bundleTypeName;
    private ClassName classNameFragmentContainer;
    private ClassName functionClassName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        final TypeElement typeElementFragmentContainer = elementUtils.getTypeElement(ComponentConstants.FRAGMENT_MANAGER_CALL_CLASS_NAME);
        if (typeElementFragmentContainer != null) {
            classNameFragmentContainer = ClassName.get(typeElementFragmentContainer);
        }

        final TypeElement function1TypeElement = elementUtils.getTypeElement(ComponentConstants.FUNCTION_CLASS_NAME);
        if (function1TypeElement != null) {
            functionClassName = ClassName.get(function1TypeElement);
        }

        bundleTypeElement = elementUtils.getTypeElement(ComponentConstants.ANDROID_BUNDLE);
        bundleTypeName = TypeName.get(bundleTypeElement.asType());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> moduleAppElements = roundEnvironment.getElementsAnnotatedWith(FragmentAnno.class);
            parseAnnotation(moduleAppElements);
            createImpl();
            return true;
        }
        return false;
    }

    private void createImpl() {
        String className = ComponentUtil.genHostFragmentClassName(componentHost);
        //pkg
        String pkg = className.substring(0, className.lastIndexOf('.'));
        //simpleName
        String cn = className.substring(className.lastIndexOf('.') + 1);

        // superClassName,这里可能为null,没有找到对应的类
        ClassName superClass = ClassName.get(elementUtils.getTypeElement(ComponentUtil.FRAGMENT_IMPL_CLASS_NAME));

        MethodSpec initHostMethod = generateInitHostMethod();
        MethodSpec onCreateMethod = generateOnCreateMethod();
        MethodSpec onDestroyMethod = generateOnDestroyMethod();
        MethodSpec onGetFragmentMapMethod = generateGetFragmentMapMethod();
        TypeSpec typeSpec = TypeSpec.classBuilder(cn)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.FINAL)
                .superclass(superClass)
                .addMethod(initHostMethod)
                .addMethod(onCreateMethod)
                .addMethod(onDestroyMethod)
                .addMethod(onGetFragmentMapMethod)
                .build();
        try {
            JavaFile
                    .builder(pkg, typeSpec)
                    .indent("    ")
                    .build().writeTo(mFiler);
        } catch (IOException e) {
        }
    }

    /**
     * 关键方法--返回所有收集到的fragment的名字集合，后续统一取消注册
     *
     * @return
     */

    private MethodSpec generateGetFragmentMapMethod() {
        ParameterizedTypeName fragmentMapParameterizedTypeName =
                ParameterizedTypeName.get(mClassNameHashSet, TypeName.get(mTypeElementString.asType()));

        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("getNameSet")
                .returns(fragmentMapParameterizedTypeName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        methodSpecBuilder.addStatement("HashSet<String> set = new HashSet();");
        fragmentList.forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                FragmentAnno anno = element.getAnnotation(FragmentAnno.class);
                List<String> fragmentFlags = Arrays.asList(anno.value());
                for (String fragmentFlag : fragmentFlags) {
                    methodSpecBuilder.addStatement("set.add($S)", fragmentFlag);
                }
            }
        });
        methodSpecBuilder.addStatement("return set");
        return methodSpecBuilder.build();
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
        //在创建fragment的时候，可以带bundle过去
        final ParameterSpec bundleParameter = ParameterSpec.builder(bundleTypeName, "bundle").build();
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
        fragmentList.forEach(new Consumer<Element>() {

            @Override
            public void accept(Element element) {
                FragmentAnno anno = element.getAnnotation(FragmentAnno.class);
                String implName = "implName" + atomicInteger.incrementAndGet();
                String serviceImplClassName = element.toString();
                TypeName serviceImplTypeName = TypeName.get(elementUtils.getTypeElement(serviceImplClassName).asType());
                boolean haveDefaultConstructor = isHaveDefaultConstructor(element.toString());
                MethodSpec.Builder getMethodBuilder = MethodSpec.methodBuilder("apply")
                        .addAnnotation(Override.class)
                        .addParameter(bundleParameter)
                        .addModifiers(Modifier.PUBLIC);

                getMethodBuilder
                        .beginControlFlow("if(bundle == null)")
                        .addStatement("bundle = new Bundle()")
                        .endControlFlow()
                        .addStatement("$T fragment =  new $T($N)", serviceImplTypeName, serviceImplTypeName, (haveDefaultConstructor ? "" : NAME_OF_APPLICATION))
                        .addStatement("fragment.setArguments(bundle)")
                        .addStatement("return fragment")
                        .returns(TypeName.get(element.asType()));

                TypeSpec innerTypeSpec = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(ParameterizedTypeName.get(functionClassName, bundleTypeName, serviceImplTypeName))
                        .addMethod(getMethodBuilder.build())
                        .build();
                if (functionClassName != null) {
                    methodSpecBuilder.addStatement("$T $N = $L", functionClassName, implName, innerTypeSpec);
                }
                if (classNameFragmentContainer != null) {
                    methodSpecBuilder.addStatement("$T.register($S,$N)", classNameFragmentContainer, anno.value(), implName);
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

        fragmentList.forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                FragmentAnno anno = element.getAnnotation(FragmentAnno.class);
                methodSpecBuilder.addStatement("$T.unregister($S)", classNameFragmentContainer, anno.value());
            }
        });
        return methodSpecBuilder.build();
    }


    private List<Element> fragmentList = new ArrayList<>();
    private final Set<String> nameSet = new HashSet<>();

    /**
     * 是否有默认的构造器
     *
     * @param className
     * @return
     */
    private boolean isHaveDefaultConstructor(String className) {
        // 实现类的类型
        TypeElement typeElementClassImpl = elementUtils.getTypeElement(className);
        String constructorName = typeElementClassImpl.getSimpleName().toString() + ("()");
        List<? extends Element> enclosedElements = typeElementClassImpl.getEnclosedElements();
        for (Element enclosedElement : enclosedElements) {
            if (enclosedElement.toString().equals(constructorName)) {
                return true;
            }
        }
        return false;
    }

    private void parseAnnotation(Set<? extends Element> moduleAppElements) {
        fragmentList.clear();
        nameSet.clear();
        for (Element element : moduleAppElements) {
            // 如果是一个 Application
            FragmentAnno moduleApp = element.getAnnotation(FragmentAnno.class);
            if (moduleApp == null) {
                continue;
            }
            String fragmentName = moduleApp.value();
            if (nameSet.contains(fragmentName)) {
//                throw new ProcessException("the name of '" + fragmentName + "' is already exist");
            } else {
                nameSet.add(fragmentName);
            }
            fragmentList.add(element);
        }
    }


}
