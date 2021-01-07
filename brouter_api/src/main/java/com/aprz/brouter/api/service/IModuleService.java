package com.aprz.brouter.api.service;

import java.util.Map;

/**
 * 该类给注解处理器使用
 */
public interface IModuleService {

    /**
     * 返回module里面的所有服务
     */
    Map<String, IRouteService> services();

}
