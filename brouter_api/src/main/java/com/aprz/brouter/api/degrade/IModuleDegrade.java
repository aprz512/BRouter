package com.aprz.brouter.api.degrade;

import java.util.List;

/**
 * 仅供注解处理器生成类使用
 * {@link com.aprz.brouter.processor.DegradeProcessor}
 */
public interface IModuleDegrade {

    /**
     * 获取module下面的所有 IRouteDegrade
     */
    List<IRouteDegrade> degrades();

}
