package com.aprz.brouter.annotation;

/**
 * 用于标记fragment
 */
public @interface FragmentAnno {
    /**
     * 这个 Fragment 对应的唯一 ID
     *
     * @return 对应 Fragment 的一个标记, 不能重复
     */
    String value();
}
