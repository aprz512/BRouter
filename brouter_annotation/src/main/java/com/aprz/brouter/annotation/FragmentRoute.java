package com.aprz.brouter.annotation;

/**
 * 用于标记fragment
 */
public @interface FragmentRoute {
    /**
     * 这个 FragmentRoute 对应的唯一 ID
     *
     * @return 对应 FragmentRoute 的一个标记, 不能重复，与 Route 的规则一样
     */
    String path();
}
