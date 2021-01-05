package com.aprz.brouter.api.interceptor;

import androidx.annotation.NonNull;

import com.aprz.brouter.api.core.Navigation;

/**
 * 拦截器接口
 */
public interface IRouteInterceptor {

    /**
     * 拦截器的优先级，数值越小优先级越高，为了规范，数字不应该小于 0
     * 或者可以做一个拦截器的依赖图，后续有精力再搞
     */
    int priority();

    /**
     * 拦截器的拦截方法，Chain 作为参数，可以使用它来决定是否拦截
     */
    void intercept(@NonNull Chain chain, @NonNull Callback callback);


    interface Chain {
        /**
         * 如果拦截器决定不拦截，调用这个方法
         */
        void proceed(@NonNull Navigation navigation);

        /**
         * 如果拦截器决定拦截，调用这个方法
         */
        void interrupt(@NonNull Throwable exception);

        /**
         * 返回路由对象
         */
        Navigation navigate();
    }

    interface Callback {
        /**
         * 跳转成功
         */
        void onSuccess(@NonNull Navigation navigation);

        /**
         * 跳转失败，被拦截器拦下了
         */
        void onFail(@NonNull Throwable exception);
    }

}
