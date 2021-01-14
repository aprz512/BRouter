package com.aprz.brouter.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aprz.brouter.annotation.Interceptor;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.core.Navigation;
import com.aprz.brouter.api.degrade.DegradeHelper;
import com.aprz.brouter.api.degrade.IRouteDegrade;
import com.aprz.brouter.api.ex.RouteNotFoundException;
import com.aprz.brouter.api.interceptor.IRouteInterceptor;

/**
 * 本来想将这个拦截器写在库里面的，但是发现怎么写都不爽，后来才想明白，这个拦截器应该给使用者实现才是对的
 * 当没有匹配的 url 的时候，跳转到一个 404 页面
 */
@Interceptor
public class NotFoundInterceptor implements IRouteInterceptor {

    private static final String TAG = "NotFoundInterceptor";

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void intercept(@NonNull Chain chain, @NonNull Callback callback) {
        Log.e(TAG, "NotFoundInterceptor run on " + Thread.currentThread().getName());
        Navigation navigate = chain.navigate();
        if (navigate.invalid()) {
            // 这里 interrupt 了，如果用户设置了回调，后面应该没有逻辑才对
            chain.interrupt(new RouteNotFoundException("没有找到匹配的路由地址：" + navigate.getPath()));

            // 用户设置了回调，那么就走用户的回调
            if (chain.userCallback() == null) {
                // 有降级策略，走降级
                IRouteDegrade routeDegrade = DegradeHelper.getRouteDegrade(navigate);
                if (routeDegrade != null) {
                    routeDegrade.handleDegrade(navigate);
                }
                // 没有降级策略，走默认页面
                else {
                    // 不要搞成死循环了
                    BRouter.getInstance().path("app/not_found").navigate();
                }
            }
        } else {
            // 有降级策略，走降级
            IRouteDegrade routeDegrade = DegradeHelper.getRouteDegrade(navigate);
            if (routeDegrade != null) {
                routeDegrade.handleDegrade(navigate);
            }
            // 没有降级策略，继续执行原来的跳转
            else {
                chain.proceed(navigate);
            }
        }
    }
}
