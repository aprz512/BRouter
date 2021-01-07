package com.aprz.brouter.api.service;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServiceHelper {

    private static final String TAG = "ServiceHelper";

    public static void addModuleService(String moduleName) {
        IModuleService moduleService = findModuleService(moduleName);
        Map<String, IRouteService> services = moduleService.services();
        Set<Map.Entry<String, IRouteService>> entries = services.entrySet();
        for (Map.Entry<String, IRouteService> entry : entries) {
            ServiceStore.putService(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static IModuleService findModuleService(String module) {
        try {
            Class<IModuleService> degradeClass =
                    (Class<IModuleService>) Class.forName("com.aprz.brouter.degrades.BRouter$$Service$$" + module);
            return degradeClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignore) {
            Log.e(TAG, "没有在 module 内部找到服务: " + module);
            return new EmptyModuleService();
        }
    }


    static class EmptyModuleService implements IModuleService {

        @Override
        public Map<String, IRouteService> services() {
            return new HashMap<>(0);
        }
    }

}
