package com.aprz.base.util;

import android.os.Handler;
import android.os.Looper;


public class UiThreadUtils {

    public static void run(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static void runDelay(Runnable runnable, long delay) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delay);
    }

}
