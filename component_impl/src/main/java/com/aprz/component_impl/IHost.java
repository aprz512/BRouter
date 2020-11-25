package com.aprz.component_impl;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

/**
 * 获取 Host 的接口
 */
public interface IHost {

    /**
     * 获取模块的 host
     */
    @NonNull
    @UiThread
    String getName();

}
