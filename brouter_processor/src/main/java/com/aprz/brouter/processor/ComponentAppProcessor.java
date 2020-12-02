package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.ComponentAppAnno;
import com.example.component_api.ComponentConstants;
import com.example.component_api.ComponentUtil;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
 * 用于处理组件生命周期注解
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.ComponentAppAnno"})
public class ComponentAppProcessor extends BaseHostProcessor {
    private TypeElement centerServiceTypeElement;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        centerServiceTypeElement = elementUtils.getTypeElement(ComponentConstants.CENTER_SERVICE_CLASS_NAME);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> moduleAppElements = roundEnvironment.getElementsAnnotatedWith(ComponentAppAnno.class);
            parseAnnotation(moduleAppElements);
            createImpl();
            return true;
        }
        return false;
    }

    private void createImpl() {
        String className = ComponentUtil.genHostModuleApplicationClassName(componentHost);
        //pkg
        String pkg = className.substring(0, className.lastIndexOf('.'));
        //simpleName
        String cn = className.substring(className.lastIndexOf('.') + 1);

        // superClassName
        ClassName superClass = ClassName.get(elementUtils.getTypeElement(ComponentUtil.COMPONENT_APPLICATION_IMPL_CLASS_NAME));

        MethodSpec initMapMethod = generateInitMapMethod();
        MethodSpec initHostMethod = generateInitHostMethod();
        MethodSpec onCreateMethod = generateOnCreateMethod();
        MethodSpec onDestroyMethod = generateOnDestroyMethod();
        TypeSpec typeSpec = TypeSpec.classBuilder(cn)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.FINAL)
                .superclass(superClass)
                .addMethod(initMapMethod)
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
        ParameterSpec parameterSpec = ParameterSpec.builder(applicationName, "application")
                .build();
        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("onCreate")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addParameter(parameterSpec)
                .addModifiers(Modifier.PUBLIC);
        methodSpecBuilder.addStatement("super.onCreate(application)");
        methodSpecBuilder.addStatement("$T.getInstance().register(getName())", centerServiceTypeElement);
        return methodSpecBuilder.build();
    }

    private MethodSpec generateOnDestroyMethod() {
        TypeName returnType = TypeName.VOID;
        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("onDestroy")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        methodSpecBuilder.addStatement("super.onDestroy()");

        methodSpecBuilder.addStatement("$T.getInstance().unregister(getName())", centerServiceTypeElement);
        methodSpecBuilder.addComment("清空缓存");
        return methodSpecBuilder.build();
    }

    private MethodSpec generateInitMapMethod() {
        TypeName returnType = TypeName.VOID;
        final MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("initComponentLifecycle")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED);
        openUriMethodSpecBuilder.addStatement("super.initComponentLifecycle()");
        applicationList.forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                openUriMethodSpecBuilder.addStatement(
                        "componentLifecycle" + "=new $T()",
                        ClassName.get((TypeElement) element)
                );
            }
        });
        return openUriMethodSpecBuilder.build();
    }

    private List<Element> applicationList = new ArrayList<>();

    private void parseAnnotation(Set<? extends Element> moduleAppElements) {
        applicationList.clear();
        TypeMirror typeApplicationLifecycle = elementUtils.getTypeElement(ComponentConstants.APPLICATION_LIFECYCLE_INTERFACE_CLASS_NAME).asType();
        for (Element element : moduleAppElements) {
            TypeMirror tm = element.asType();
            if (!(element instanceof TypeElement)) {
//                throw new ProcessException(element + " is not a 'TypeElement' ");
            }
            if (!types.isSubtype(tm, typeApplicationLifecycle)) {
//                throw new ProcessException(element + " you must implement IApplicationLifecycle interface");
            }
            // 如果是一个 Application
            ComponentAppAnno moduleApp = element.getAnnotation(ComponentAppAnno.class);
            if (moduleApp == null) {
                continue;
            }
            applicationList.add(element);
        }
    }


}
