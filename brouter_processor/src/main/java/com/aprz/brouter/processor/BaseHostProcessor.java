package com.aprz.brouter.processor;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * 解析组件中配置的HOST属性
 */
public abstract class BaseHostProcessor extends BaseProcessor {
    // 在每一个 module 中配置的 HOST 的信息
    protected String componentHost = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Map<String, String> options = processingEnv.getOptions();
        if (options != null) {
            componentHost = options.get("HOST");
        }
    }
}
