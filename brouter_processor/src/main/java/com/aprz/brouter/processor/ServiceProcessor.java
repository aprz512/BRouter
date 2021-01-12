package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.Degrade;
import com.aprz.brouter.annotation.Service;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.Service"})
public class ServiceProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (CollectionUtils.isEmpty(annotations)) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Service.class);
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }

        MethodSpec.Builder degradesMethod = buildServices(elements);

        try {
            String interceptorModuleClassName = Constant.SERVICE_CLASS_PREFIX + moduleName;
            JavaFile.builder(Constant.SERVICE_PACKAGE_NAME,
                    TypeSpec.classBuilder(interceptorModuleClassName)
                            .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Constant.SERVICE_MODULE)))
                            .addModifiers(PUBLIC)
                            .addMethod(degradesMethod.build())
                            .build()
            ).indent("    ").build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private MethodSpec.Builder buildServices(Set<? extends Element> elements) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("services");
        ParameterizedTypeName mapType = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                ClassName.get(String.class),
                ClassName.get(Object.class));


        methodBuilder.addStatement("$T<$T, $T> result = new $T()",
                Map.class,
                String.class,
                Object.class,
                mapType);
        for (Element element : elements) {
            Service annotation = element.getAnnotation(Service.class);
            methodBuilder.addStatement("result.put($S, new $T())", annotation.name(), TypeName.get(element.asType()));
        }

        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Object.class));

        methodBuilder.addStatement("return result");
        methodBuilder.addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(returnType);
        return methodBuilder;
    }

}
