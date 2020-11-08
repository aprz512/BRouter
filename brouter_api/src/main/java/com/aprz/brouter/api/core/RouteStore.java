package com.aprz.brouter.api.core;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class RouteStore {

    private static Map<String, Class<? extends Activity>> routeMap = new HashMap<>();

    public static void injectRoute(Map<String, Class<? extends Activity>> moduleRouteMap) {
        routeMap.putAll(moduleRouteMap);
    }

    public static Map<String, Class<? extends Activity>> getRouteMap() {
        return routeMap;
    }

}
