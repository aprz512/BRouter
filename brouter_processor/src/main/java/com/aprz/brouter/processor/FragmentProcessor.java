package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.FragmentRoute;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.FragmentRoute"})
public class  FragmentProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isEmpty(annotations)) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(FragmentRoute.class);
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }

        MethodSpec.Builder fragmentMethod = buildFragments(elements);

        try {
            String fragmentModuleClassName = Constant.FRAGMENT_CLASS_PREFIX + moduleName;
            JavaFile.builder(Constant.FRAGMENT_PACKAGE_NAME,
                    TypeSpec.classBuilder(fragmentModuleClassName)
                            .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Constant.FRAGMENT_MODULE)))
                            .addModifiers(PUBLIC)
                            .addMethod(fragmentMethod.build())
                            .build()
            ).indent("    ").build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private MethodSpec.Builder buildFragments(Set<? extends Element> elements) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("fragments");

        ParameterizedTypeName fragmentClass = ParameterizedTypeName.get(
                ClassName.get(Class.class),
                WildcardTypeName.subtypeOf(ClassName.get(typeElement(Constant.FRAGMENT))));

        ParameterizedTypeName mapType = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                ClassName.get(String.class),
                fragmentClass);


        methodBuilder.addStatement("$T<$T, $T> result = new $T()",
                Map.class,
                String.class,
                fragmentClass,
                mapType);
        for (Element element : elements) {
            FragmentRoute annotation = element.getAnnotation(FragmentRoute.class);
            methodBuilder.addStatement("result.put($S, $T.class)", annotation.path(), TypeName.get(element.asType()));
        }

        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                fragmentClass);

        methodBuilder.addStatement("return result");
        methodBuilder.addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(returnType);
        return methodBuilder;
    }


}
