package com.aprz.graph.task;

import android.os.Looper;

public class ThreadUtils {

    public static boolean currentIsMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

}
