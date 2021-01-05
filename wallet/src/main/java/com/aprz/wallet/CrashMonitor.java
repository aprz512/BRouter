package com.aprz.wallet;

import androidx.annotation.UiThread;

import java.util.HashMap;

public class CrashMonitor {

    private static HashMap<String, Integer> pages = new HashMap<>();

    @UiThread
    public static void pageCrashed(String pagePath) {
        Integer crashCount = pages.get(pagePath);
        if (pages.containsKey(pagePath) && crashCount != null) {
            pages.put(pagePath, crashCount + 1);
        } else {
            pages.put(pagePath, 1);
        }
    }

    public static boolean needDegrade(String pagePath) {
        Integer crashCount = pages.get(pagePath);
        if (crashCount != null) {
            return crashCount >= 3;
        }

        return false;
    }

}
