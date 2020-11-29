package com.aprz.brouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/27
 * <p>
 * Class desc:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Bind {

    /**
     * 页面跳转传递参数（Bundle）时指定的 key，如果没有设置那么就使用变量名
     */
    String key() default "";

}
