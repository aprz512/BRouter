package com.aprz.component_impl;

import androidx.annotation.NonNull;

/**
 * 懒加载设计
 * time   : 2018/11/27
 *
 * @author : xiaojinzi
 */
public interface Callable<T> {

    /**
     * 获取实际的兑现
     *
     * @return 获取实现对象
     */
    @NonNull
    T get();

}
