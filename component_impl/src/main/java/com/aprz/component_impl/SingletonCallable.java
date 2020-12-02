package com.aprz.component_impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 单例模式
 **/
public abstract class SingletonCallable<T> implements Callable<T> {
    @Nullable
    private volatile T instance;

    public boolean isInit() {
        return instance != null;
    }

    @NonNull
    @Override
    public T get() {
        if (null == instance) {
            synchronized (this) {
                if (null == instance) {
                    instance = getSingleInstance();
                }
            }
        }
        return instance;
    }

    /**
     * 获取真正的对象
     */
    @NonNull
    protected abstract T getSingleInstance();
}
