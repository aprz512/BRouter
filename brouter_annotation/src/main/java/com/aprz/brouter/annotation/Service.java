package com.aprz.brouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Service {
    /**
     * 组件对外提供的服务的名字，根据这个名字，可以获取这个服务
     */
    String name();
}
