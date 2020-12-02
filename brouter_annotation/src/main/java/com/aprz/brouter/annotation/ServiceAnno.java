package com.aprz.brouter.annotation;

/**
 * 用于标记模块提供的服务
 * 比如Login模块提供的{@link com.example.module_login_export.service.UserService}
 * 相关的processor会识别出来，然后将该服务向ServiceManager注册
 */
public @interface ServiceAnno {
    /**
     * 这个服务对应的接口
     */
    Class value();

    /**
     * 是否是单例,默认是单例模式的
     */
    boolean singleTon() default true;
}
