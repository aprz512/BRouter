package com.aprz.component_impl;

import android.app.Application;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

public class Component {

    /**
     * 配置对象
     */
    private static Config mConfig = null;
    private static boolean isInit=false;

    /**
     * 获取 Application
     *
     * @return Application
     */
    @NonNull
    @AnyThread
    public static Application getApplication() {
        checkInit();
        return mConfig.getApplication();
    }

    /**
     * 初始化
     *
     * @see Config 初始化的配置对象
     */
    @UiThread
    public static void init(boolean isDebug, @NonNull Config config) {
        // 做必要的检查
        if (isInit) {
            throw new RuntimeException("you have init Component already!");
        }
        mConfig = config;
        isInit = true;
    }

    private static void checkInit() {
        if (mConfig == null) {
            throw new RuntimeException("you must init Component first!");
        }
    }

}
