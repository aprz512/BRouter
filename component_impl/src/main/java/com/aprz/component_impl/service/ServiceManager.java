package com.aprz.component_impl.service;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aprz.component_impl.CallNullable;
import com.aprz.component_impl.Callable;
import com.aprz.component_impl.exception.NotSupportException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jacky.peng
 */
public class ServiceManager {
    private ServiceManager() {
        throw new NotSupportException("can't to create");
    }

    /**
     * Service 的集合. 线程安全的
     */
    private static Map<Class, Callable<?>> serviceMap =
            Collections.synchronizedMap(new HashMap<Class, Callable<?>>());


    /**
     * 你可以注册一个服务,服务的初始化可以是懒加载的
     * 注册的时候, 不会初始化目标 Service 的
     * {@link #get(Class)} 方法内部才会初始化目标 Service
     */
    @AnyThread
    public static <T> void register(@NonNull Class<T> tClass, @NonNull Callable<? extends T> callable) {
        serviceMap.put(tClass, callable);
    }

    @Nullable
    @AnyThread
    public static <T> void unregister(@NonNull Class<T> tClass) {
        // 需要判断到是否已经初始化了, 如果还没初始化就返回 null
        Callable<?> callable = serviceMap.remove(tClass);
        if (callable == null) {
            return;
        }
    }

    @Nullable
    @AnyThread
    public static <T> T get(@NonNull final Class<T> tClass) {
        Callable<?> callable = serviceMap.get(tClass);
        if (callable == null) {
            return null;
        } else {
            // 如果没创建, 这时候会创建了目标 service 对象
            T t = (T) callable.get();
            return t;
        }
    }

}
