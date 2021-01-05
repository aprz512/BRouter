package com.aprz.brouter.api.degrade;

import androidx.annotation.NonNull;

import com.aprz.brouter.api.core.Navigation;

public interface IRouteDegrade {

    /**
     * 是否匹配这个路由
     *
     * @param navigation 路由请求对象
     */
    boolean isMatch(@NonNull Navigation navigation);

    /**
     * 当路由失败的时候, 应该如何处理
     */
    void handleDegrade(@NonNull Navigation navigation);

}
