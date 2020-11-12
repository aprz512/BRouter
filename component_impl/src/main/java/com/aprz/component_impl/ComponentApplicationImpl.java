package com.aprz.component_impl;

import android.app.Application;

import androidx.annotation.NonNull;

public abstract class ComponentApplicationImpl implements IComponentLifecycle {

    /**
     * 是否初始化了list,懒加载
     */
    protected boolean hasInitComponentApp = false;
    //组件的生命周期类
    protected IComponentLifecycle componentLifecycle;

    //初始化组件的生命周期类，由具体的子类实现
    protected void initComponentLifecycle() {

    }

    @Override
    public void onCreate(@NonNull Application app) {
        if (!hasInitComponentApp) {
            initComponentLifecycle();
        }
        if (componentLifecycle != null) {
            componentLifecycle.onCreate(app);
        }
    }

    @Override
    public void onDestroy() {
        if (componentLifecycle != null) {
            componentLifecycle.onDestroy();
        }
    }
}
