package com.aprz.brouter.api.core;

import android.app.Activity;
import android.webkit.HttpAuthHandler;

import com.aprz.brouter.api.IRouteMap;
import com.aprz.brouter.api.IRouteModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteStore {

    private static Map<String, Class<? extends Activity>> routeMap = new HashMap<>();
    private static Map<String, IRouteMap> moduleMap = new HashMap<>();

    public static void injectModule(IRouteModule routeModule) {
        routeModule.loadModule(moduleMap);
    }

    public static Map<String, Class<? extends Activity>> getRouteMap() {
        return routeMap;
    }

    public static void completion(String path) {
        if(routeMap.get(path) != null) {
            return;
        }
        String module = getModule(path);
        IRouteMap moduleRouteMap = moduleMap.get(module);
        moduleRouteMap.loadMap(routeMap);
    }

    /**
     * 要求跳转路径使用 二级结构，如：wallet/xxx
     * 因为注解处理器没有做复杂的分组功能，所以，还要求一级路径名为 module 名字
     *
     * @return 返回一级路径名
     */
    private static String getModule(String path) {
        if (!path.contains("/")) {
            throw new IllegalStateException("路径格式不对：" + path);
        }
        return path.substring(0, path.indexOf("/"));
    }
}
