package com.aprz.brouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 因为我们的demo，只需要在 java -> class 的时候，让注解处理器搞一下，所以不需要到  class 级别
 * ARouter 的是 class，那可能是插件里面搞了啥
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Route {
    String path();
}
