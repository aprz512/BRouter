package com.aprz.brouter.api.core;

import com.aprz.brouter.api.IRouteMap;
import com.aprz.brouter.api.IRouteModule;

import java.util.HashMap;
import java.util.Map;

public class RouteStore {

    private static final String TAG = "RouteStore";

    private static final Map<String, Navigation> routeMap = new HashMap<>();
    private static final Map<String, IRouteMap> moduleMap = new HashMap<>();
    private static Map<String, String> pathModuleMap = new HashMap<>();

    public static void injectModule(IRouteModule routeModule) {
        routeModule.loadModule(moduleMap);
    }

    public static Navigation getNavigation(String path, String module) {
        Navigation navigation = routeMap.get(path);
        if (navigation == null) {
            navigation = new Navigation(path, module, null);
        }
        return navigation;
    }

    public static void completion(String path, String module) {
        if (routeMap.get(path) != null) {
            return;
        }
        IRouteMap moduleRouteMap = moduleMap.get(module);
        if (moduleRouteMap != null) {
            moduleRouteMap.loadMap(routeMap);
        }
    }

}
