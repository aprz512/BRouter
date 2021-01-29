package com.aprz.graph.task;

import android.util.Log;

public class LogUtils {

    /**
     * 全局的日志过滤 TAG，graph-task 库输出的日志，都可以用该 TAG 过滤
     */
    public static final String GLOBAL_TAG = "--> GraphTask <--";

    /**
     * 日志输出开关，默认是打开的
     */
    private static boolean sIsLoggable = true;

    public static boolean isLoggable() {
        return sIsLoggable;
    }

    public static void setLoggable(boolean sIsLoggable) {
        LogUtils.sIsLoggable = sIsLoggable;
    }

    public static void d(String tag, Object obj) {
        if (isLoggable()) {
            Log.d(tag, obj.toString());
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (isLoggable()) {
            String formattedMsg = String.format(msg, args);
            Log.d(tag, formattedMsg);
        }
    }

    public static void d(String msg, Object... args) {
        d(GLOBAL_TAG, msg, args);
    }

    public static void e(String tag, Object obj) {
        if (isLoggable()) {
            Log.e(tag, obj.toString());
        }
    }


    public static void e(String tag, String msg, Object... args) {
        if (isLoggable()) {
            String formattedMsg = String.format(msg, args);
            Log.e(tag, formattedMsg);
        }
    }


    public static void i(String tag, Object obj) {
        if (isLoggable()) {
            Log.i(tag, obj.toString());
        }
    }

    public static void w(Exception e) {
        if (isLoggable()) {
            e.printStackTrace();
        }
    }

    public static void print(Object msg) {
        d(GLOBAL_TAG, msg);
    }

    public static void print(String msg, Object... args) {
        d(GLOBAL_TAG, msg, args);
    }
}
