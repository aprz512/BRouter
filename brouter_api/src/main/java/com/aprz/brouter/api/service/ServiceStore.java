package com.aprz.brouter.api.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceStore {

    private static final Map<String, Object> serviceMap = new HashMap<>(32);

    @SuppressWarnings("unchecked")
    public static <T> T getService(String name) {
        return (T) serviceMap.get(name);
    }

    public static void putService(String name, Object service) {
        serviceMap.put(name, service);
    }

}
