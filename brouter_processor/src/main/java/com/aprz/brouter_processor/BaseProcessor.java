package com.aprz.brouter_processor;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.aprz.brouter_processor.Constant.KEY_MODULE_NAME;

/**
 * 容易犯的错误：
 * 1. auto-service 需要同时配置 annotationProcessor 与 implementation，否则会报错/无法生效
 * 2. AutoService 注解不要导错包
 */
public abstract class BaseProcessor extends AbstractProcessor {


    /**
     * Filer/Types/Elements 这些都是比较常用的
     */
    Filer mFiler;
    Types types;
    Elements elementUtils;
    Messager messager;

    /**
     * 每个组件都需要配置一下 module 的名字 ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇
     * <p>
     * javaCompileOptions {
     * annotationProcessorOptions {
     * arguments = [BROUTER_MODULE_NAME: project.getName()]
     * }
     * }
     */
    // Module name, maybe its 'app' or others
    String moduleName = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();

        // 获取用户配置的 module 的名字
        // Attempt to get user configuration [moduleName]
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            moduleName = options.get(KEY_MODULE_NAME);
        }

        if (StringUtils.isNotEmpty(moduleName)) {
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
        } else {
            throw new RuntimeException("BRouter::Compiler >>> No module name, for more information, look at gradle log.");
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
