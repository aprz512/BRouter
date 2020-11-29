package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.Bind;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/27
 * <p>
 * Class desc: 该类是用来生成自动绑定跳转时传递参数的类
 * 类似于ButterKnife，只不过 ButterKnife 是绑定的控件
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.Bind"})
public class BindProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (!CollectionUtils.isEmpty(annotations)) {
            Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(Bind.class);

            try {
                this.parseBind(routeElements);
            } catch (Exception ignored) {
                return false;
            }
            return true;
        }

        return false;
    }

    private void parseBind(Set<? extends Element> elements) throws IllegalAccessException {
        if (CollectionUtils.isEmpty(elements)) {
            return;
        }

        TypeElement hostElement = (TypeElement) elements.iterator().next().getEnclosingElement();
        String qualifiedName = hostElement.getQualifiedName().toString();
        String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(hostElement.getSimpleName().toString() + "_Bind")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(hostElement.asType()), "activity");

        constructorBuilder
                .beginControlFlow("if(activity.getIntent() != null && activity.getIntent().getExtras() != null)");

        for (Element element : elements) {

            if (element.getModifiers().contains(Modifier.PRIVATE)) {
                throw new IllegalAccessException("字段不能是私有的！！ -->  " + element.getSimpleName().toString());
            }

            Bind bind = element.getAnnotation(Bind.class);

            String fieldKey = bind.key();
            String fieldName = element.getSimpleName().toString();
            if (StringUtils.isEmpty(fieldKey)) {
                fieldKey = fieldName;
            }
            TypeName name = TypeName.get(element.asType());
            ClassName string = ClassName.get("java.lang", "String");
            if (!(name.isPrimitive() || name.equals(string))) {
                throw new IllegalAccessException("自动绑定字段功能暂时只支持基本类型与String！！！");
            }

            if (name.isPrimitive()) {
                String methodName = name.toString();
                methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                constructorBuilder.addStatement(
                        "activity.$L = activity.getIntent().getExtras().get$L($S)", fieldName, methodName, fieldKey
                );
            } else {
                constructorBuilder.addStatement(
                        "activity.$L = activity.getIntent().getExtras().getString($S)", fieldName, fieldKey
                );
            }
        }

        constructorBuilder.endControlFlow();
        classBuilder.addMethod(constructorBuilder.build());


        try {
            JavaFile.builder(packageName, classBuilder.addModifiers(PUBLIC).build())
                    .build()
                    .writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}