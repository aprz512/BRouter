package com.aprz.brouter.api.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * 用于跳转的类
 * 注解处理器会注入一些信息
 * 跳转时会注入一些信息
 * 这些信息最终会合并到一起，用于跳转
 */
public class Navigation {

    private Context appContext;
    private Bundle mBundle;
    private String path;
    private String group;
    private Class<? extends Activity> targetActivityClass;
    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public Navigation(String path, String group, Class<? extends Activity> targetActivityClass) {
        this.path = path;
        this.group = group;
        this.targetActivityClass = targetActivityClass;
        mBundle=new Bundle();
    }
    public Navigation withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }
    public Navigation params(Bundle bundle) {
        mBundle.putAll(bundle);
        return this;
    }
    public void navigate() {
        navigate(appContext);
    }

    public void navigate(Context context) {
        Navigation navigation = RouteStore.getNavigation(path);
        if (navigation != null && context != null) {
            Intent intent = new Intent(context, targetActivityClass);
            if (mBundle != null) {
                intent.putExtras(mBundle);
            }
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
