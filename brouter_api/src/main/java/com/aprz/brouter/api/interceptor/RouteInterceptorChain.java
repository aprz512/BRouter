package com.aprz.brouter.api.interceptor;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.aprz.brouter.api.core.Navigation;

import java.util.List;

public class RouteInterceptorChain implements IRouteInterceptor.Chain {

    /**
     * 所有要执行的拦截器列表
     */
    @NonNull
    private final List<IRouteInterceptor> interceptors;

    /**
     * 拦截器的下标
     */
    private final int index;

    /**
     * 要执行的跳转对象
     */
    private final Navigation navigation;

    /**
     * 拦截结果回调
     */
    private final IRouteInterceptor.Callback callback;


    public RouteInterceptorChain(@NonNull List<IRouteInterceptor> interceptors,
                                 int index,
                                 Navigation navigation,
                                 @NonNull IRouteInterceptor.Callback callback) {
        this.interceptors = interceptors;
        this.index = index;
        this.navigation = navigation;
        this.callback = callback;
    }

    @Override
    public void proceed(@NonNull Navigation navigation) {
        handleInterceptors();
    }

    private void handleInterceptors() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (index >= interceptors.size()) {
                    // todo 抛出异常
                    return;
                }

                IRouteInterceptor interceptor = interceptors.get(index);
                RouteInterceptorChain next = new RouteInterceptorChain(interceptors, index + 1, navigation, callback);
                interceptor.intercept(next, callback);
            }
        });
    }

    @Override
    public void interrupt(@NonNull Throwable exception) {
        callback.onFail(exception);
    }

    @Override
    public Navigation navigate() {
        return navigation;
    }


}
