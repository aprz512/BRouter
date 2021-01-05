package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.Interceptor;
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

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.Interceptor"})
public class InterceptorProcessor extends BaseProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (CollectionUtils.isEmpty(annotations)) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Interceptor.class);
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }


        MethodSpec.Builder interceptorsMethod = buildInterceptors(elements);
        MethodSpec.Builder globalInterceptorsMethod = buildGlobalInterceptors(elements);


        try {
            String interceptorModuleClassName = Constant.INTERCEPTOR_CLASS_PREFIX + moduleName;
            JavaFile.builder(Constant.INTERCEPTOR_PACKAGE_NAME,
                    TypeSpec.classBuilder(interceptorModuleClassName)
                            .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Constant.INTERCEPTOR_MODULE)))
                            .addModifiers(PUBLIC)
                            .addMethod(interceptorsMethod.build())
                            .addMethod(globalInterceptorsMethod.build())
                            .build()
            ).indent("    ").build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private MethodSpec.Builder buildInterceptors(Set<? extends Element> elements) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("interceptors");
        ParameterizedTypeName mapType = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                ClassName.get(String.class),
                ClassName.get(typeElement(Constant.INTERCEPTOR_ROUTE)));


        methodBuilder.addStatement("$T<$T, $T> result = new $T()",
                Map.class,
                String.class,
                typeElement(Constant.INTERCEPTOR_ROUTE),
                mapType);
        for (Element element : elements) {
            Interceptor annotation = element.getAnnotation(Interceptor.class);
            if (!annotation.path().equals("")) {
                methodBuilder.addStatement("result.put($S, new $T())", annotation.path(), TypeName.get(element.asType()));
            }
        }

        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(typeElement(Constant.INTERCEPTOR_ROUTE)));

        methodBuilder.addStatement("return result");
        methodBuilder.addModifiers(PUBLIC)
                .returns(returnType);
        return methodBuilder;
    }

    private MethodSpec.Builder buildGlobalInterceptors(Set<? extends Element> elements) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("globalInterceptors");
        ParameterizedTypeName listType = ParameterizedTypeName.get(
                ClassName.get(ArrayList.class),
                ClassName.get(typeElement(Constant.INTERCEPTOR_ROUTE)));


        methodBuilder.addStatement("List<$T> result = new $T()", typeElement(Constant.INTERCEPTOR_ROUTE),
                listType);
        for (Element element : elements) {
            Interceptor annotation = element.getAnnotation(Interceptor.class);
            if (annotation.path().equals("")) {
                methodBuilder.addStatement("result.add(new $T())", TypeName.get(element.asType()));
            }
        }

        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(List.class),
                ClassName.get(typeElement(Constant.INTERCEPTOR_ROUTE)));

        methodBuilder.addStatement("return result");
        methodBuilder.addModifiers(PUBLIC)
                .returns(returnType);
        return methodBuilder;
    }

}
