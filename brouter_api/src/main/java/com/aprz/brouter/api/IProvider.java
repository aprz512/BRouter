package com.aprz.brouter.api;

import android.content.Context;

/**
 * 服务基类接口
 * 模块中暴露的服务可以实现该接口
 */
public interface IProvider {
    void init(Context context);
}
