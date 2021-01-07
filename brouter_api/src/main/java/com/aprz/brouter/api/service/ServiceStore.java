package com.aprz.brouter.api.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceStore {

    private static Map<String, IRouteService> serviceMap = new HashMap<>(32);

    public static IRouteService getService(String name) {
        return serviceMap.get(name);
    }

    public static void putService(String name, IRouteService service) {
        serviceMap.put(name, service);
    }

}
