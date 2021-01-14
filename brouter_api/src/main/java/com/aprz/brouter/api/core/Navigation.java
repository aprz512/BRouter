package com.aprz.brouter.api.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.aprz.brouter.api.interceptor.IRouteInterceptor;
import com.aprz.brouter.api.interceptor.InterceptorStore;
import com.aprz.brouter.api.interceptor.LastInterceptor;
import com.aprz.brouter.api.interceptor.RouteInterceptorChain;

import java.util.Collections;
import java.util.List;

/**
 * 用于跳转的类
 * 注解处理器会注入一些信息
 * 跳转时会注入一些信息
 * 这些信息最终会合并到一起，用于跳转
 */
public class Navigation {

    private Context appContext;
    private Bundle params;
    private final String path;
    private final String group;
    private final Class<? extends Activity> targetActivityClass;

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public Context getAppContext() {
        return appContext;
    }

    public Navigation(String path, String group, Class<? extends Activity> targetActivityClass) {
        this.path = path;
        this.group = group;
        this.targetActivityClass = targetActivityClass;
    }

    public Navigation params(Bundle bundle) {
        params = new Bundle();
        params.putAll(bundle);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Bundle getParams() {
        return params;
    }

    public boolean valid() {
        return targetActivityClass != null;
    }

    public boolean invalid() {
        return targetActivityClass == null;
    }

    public void navigate() {
        navigate(appContext);
    }

    public void navigate(final Context context) {
        navigate(context, null);
    }

    public void navigate(final Context context, final IRouteInterceptor.Callback callback) {
        List<IRouteInterceptor> interceptorList = InterceptorStore.getInterceptorList(path);
        // 最后的拦截器，不然没有 callback.onSuccess 回调
        interceptorList.add(new LastInterceptor());
        Collections.sort(interceptorList, (o1, o2) -> o1.priority() - o2.priority());
        IRouteInterceptor.Chain chain = new RouteInterceptorChain(
                interceptorList,
                0,
                this,
                new IRouteInterceptor.Callback() {
                    @Override
                    public void onSuccess(@NonNull Navigation navigation) {
                        // 执行真正的跳转逻辑
                        internalNavigate(context, navigation);
                        if (callback != null) {
                            callback.onSuccess(navigation);
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable exception) {
                        if (callback != null) {
                            callback.onFail(exception);
                        }
                    }
                },
                callback);
        chain.proceed(this);
    }

    public void internalNavigate(Context context, Navigation navigation) {
        if (navigation.valid() && context != null) {
            Intent intent = new Intent(context, targetActivityClass);
            if (params != null) {
                intent.putExtras(params);
            }
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
