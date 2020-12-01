package com.example.component_base;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import java.util.concurrent.atomic.AtomicInteger;


public class Utils {
    public static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * 主线程的Handler
     */
    private static Handler h = new Handler(Looper.getMainLooper());

    /**
     * 在主线程延迟执行任务
     */
    @AnyThread
    public static void postDelayActionToMainThread(@NonNull @UiThread Runnable r, long delayMillis) {
        h.postDelayed(r, delayMillis);
    }

    /**
     * 在主线程执行任务
     */
    @AnyThread
    public static void postActionToMainThread(@NonNull Runnable r) {
        if (isMainThread()) {
            r.run();
        } else {
            h.post(r);
        }
    }
    /**
     * 是否是主线程
     */
    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
