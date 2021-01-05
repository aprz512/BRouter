package com.aprz.brouter.api.interceptor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterceptorStore {

    private static Map<String, IRouteInterceptor> interceptorMap = new HashMap<>(32);
    private static List<IRouteInterceptor> globalInterceptors = new ArrayList<>(32);

    public static List<IRouteInterceptor> getInterceptorList(@NonNull String path) {
        List<IRouteInterceptor> interceptors = new ArrayList<>(globalInterceptors);
        for (Map.Entry<String, IRouteInterceptor> entry : interceptorMap.entrySet()) {
            if (path.equals(entry.getKey())) {
                interceptors.add(entry.getValue());
            }
        }
        return interceptors;
    }

    public static void putGlobalInterceptor(IRouteInterceptor interceptors) {
        globalInterceptors.add(interceptors);
    }

    public static void putInterceptor(String path, IRouteInterceptor interceptor) {
        interceptorMap.put(path, interceptor);
    }

}
