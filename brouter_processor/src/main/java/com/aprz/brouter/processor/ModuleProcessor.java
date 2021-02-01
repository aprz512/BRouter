package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.Module;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.Module"})
public class ModuleProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> moduleElements = roundEnvironment.getElementsAnnotatedWith(Module.class);
            if (moduleElements.size() != 1) {
                messager.printMessage(Diagnostic.Kind.ERROR, "at least have one Module class！！！");
                return false;
            }
            createProxy(moduleElements.iterator().next());
            return true;
        }
        return false;
    }

    private void createProxy(Element element) {
        //pkg
        String pkg = Constant.MODULE_PACKAGE_NAME;
        //simpleName
        String cn = Constant.MODULE_CLASS_NAME_PREFIX + moduleName;

        // 接口
        ClassName superClass = ClassName.get(typeElement(Constant.MODULE));

        MethodSpec constructorMethod = generateConstructorMethod(element);
        MethodSpec onCreateMethod = generateOnCreateMethod();
        MethodSpec onDestroyMethod = generateOnDestroyMethod();
        TypeSpec typeSpec = TypeSpec.classBuilder(cn)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.FINAL)
                .addSuperinterface(superClass)
                .addField(FieldSpec.builder(ClassName.get(typeElement(Constant.MODULE)), "module", Modifier.PRIVATE).build())
                .addMethod(constructorMethod)
                .addMethod(onCreateMethod)
                .addMethod(onDestroyMethod)
                .build();
        try {
            JavaFile
                    .builder(pkg, typeSpec)
                    .indent("    ")
                    .build().writeTo(mFiler);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private MethodSpec generateConstructorMethod(Element element) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.module = new $T()", element.asType());
        return builder.build();
    }


    private MethodSpec generateOnCreateMethod() {
        TypeName returnType = TypeName.VOID;
        ClassName applicationName = ClassName.get(typeElement(Constant.APPLICATION));
        ParameterSpec parameterSpec = ParameterSpec.builder(applicationName, "application")
                .build();
        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("onCreate")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addParameter(parameterSpec)
                .addModifiers(Modifier.PUBLIC);
        methodSpecBuilder.addStatement("this.module.onCreate(application)");
        methodSpecBuilder.addStatement("$T.addModuleInterceptor($S)", typeElement(Constant.INTERCEPTOR_HELPER), moduleName);
        methodSpecBuilder.addStatement("$T.addModuleDegrade($S)", typeElement(Constant.DEGRADE_HELPER), moduleName);
        methodSpecBuilder.addStatement("$T.addModuleService($S)", typeElement(Constant.SERVICE_HELPER), moduleName);
        methodSpecBuilder.addStatement("$T.addModuleFragment($S)", typeElement(Constant.FRAGMENT_HELPER), moduleName);
        return methodSpecBuilder.build();
    }

    private MethodSpec generateOnDestroyMethod() {
        TypeName returnType = TypeName.VOID;
        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("onDestroy")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        methodSpecBuilder.addStatement("this.module.onDestroy()");
        return methodSpecBuilder.build();
    }

}
