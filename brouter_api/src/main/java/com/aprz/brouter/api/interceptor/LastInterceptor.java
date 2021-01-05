package com.aprz.brouter.api.interceptor;

import androidx.annotation.NonNull;

/**
 * 该拦截器应该在所有拦截器的最后添加
 * 作用：
 * 通知拦截回调，拦截链走完了，回调成功
 */
public class LastInterceptor implements IRouteInterceptor {
    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void intercept(@NonNull Chain chain, @NonNull Callback callback) {
        callback.onSuccess(chain.navigate());
    }
}
