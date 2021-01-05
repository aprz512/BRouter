package com.aprz.brouter.api.interceptor;

import java.util.List;
import java.util.Map;

/**
 * 仅供注解处理器生成类使用
 */
public interface IModuleInterceptor {

    /**
     * 获取 module 内的所有拦截器，全局的
     */
    List<IRouteInterceptor> globalInterceptors();

    /**
     * 获取 module 中的拦截器，非全局
     */
    Map<String, IRouteInterceptor> interceptors();

}
