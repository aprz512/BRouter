package com.aprz.brouter.api.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterceptorHelper {

    private static final String TAG = "InterceptorHelper";

    /**
     * 这里添加每个 module 里面的自定义拦截器
     */
    public static void addModuleInterceptor(String module) {
        IModuleInterceptor moduleInterceptor = findModuleInterceptor(module);
        List<IRouteInterceptor> globalInterceptors = moduleInterceptor.globalInterceptors();
        for (IRouteInterceptor interceptor : globalInterceptors) {
            InterceptorStore.putGlobalInterceptor(interceptor);
        }
        Map<String, IRouteInterceptor> interceptors = moduleInterceptor.interceptors();
        for (Map.Entry<String, IRouteInterceptor> entry : interceptors.entrySet()) {
            InterceptorStore.putInterceptor(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static IModuleInterceptor findModuleInterceptor(String module) {
        try {
            Class<IModuleInterceptor> interceptorClass =
                    (Class<IModuleInterceptor>) Class.forName("com.aprz.brouter.interceptors.BRouter$$Interceptor$$" + module);
            return interceptorClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            Log.e(TAG, "没有在 module 内部找到拦截器: " + module);
            return new EmptyModuleInterceptor();
        }
    }


    static class EmptyModuleInterceptor implements IModuleInterceptor {

        @Override
        public List<IRouteInterceptor> globalInterceptors() {
            return new ArrayList<>(0);
        }

        @Override
        public Map<String, IRouteInterceptor> interceptors() {
            return new HashMap<>(0);
        }
    }

}
