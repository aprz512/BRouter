package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.Degrade;
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
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.Degrade"})
public class DegradeProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (CollectionUtils.isEmpty(annotations)) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Degrade.class);
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }

        MethodSpec.Builder degradesMethod = buildDegrades(elements);

        try {
            String interceptorModuleClassName = Constant.DEGRADE_CLASS_PREFIX + moduleName;
            JavaFile.builder(Constant.DEGRADE_PACKAGE_NAME,
                    TypeSpec.classBuilder(interceptorModuleClassName)
                            .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Constant.DEGRADE_MODULE)))
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

    private MethodSpec.Builder buildDegrades(Set<? extends Element> elements) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("degrades");
        ParameterizedTypeName listType = ParameterizedTypeName.get(
                ClassName.get(ArrayList.class),
                ClassName.get(typeElement(Constant.DEGRADE_ROUTE)));


        methodBuilder.addStatement("$T<$T> result = new $T()",
                List.class,
                typeElement(Constant.DEGRADE_ROUTE),
                listType);
        for (Element element : elements) {
            Degrade annotation = element.getAnnotation(Degrade.class);
            methodBuilder.addStatement("result.add(new $T())", TypeName.get(element.asType()));
        }

        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(List.class),
                ClassName.get(typeElement(Constant.DEGRADE_ROUTE)));

        methodBuilder.addStatement("return result");
        methodBuilder.addModifiers(PUBLIC)
                .returns(returnType);
        return methodBuilder;
    }

}
