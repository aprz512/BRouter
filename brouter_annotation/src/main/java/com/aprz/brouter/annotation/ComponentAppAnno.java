package com.aprz.brouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户标识组件中的生命周期类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ComponentAppAnno {
}
