package com.aprz.component_impl.service;

import android.content.Context;

import com.aprz.component_impl.IComponentLifecycle;
import com.aprz.component_impl.IHost;

/**
 * 服务基类接口
 * 模块中暴露的服务可以实现该接口
 */
public interface IComponentService extends IComponentLifecycle, IHost {
}
