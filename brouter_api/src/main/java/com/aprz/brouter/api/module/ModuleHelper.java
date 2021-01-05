package com.aprz.brouter.api.module;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aprz.brouter.api.core.BRouter;

import java.util.HashMap;
import java.util.Map;

public class ModuleHelper {

    private static final String TAG = "ModuleHelper";

    private static final Map<String, IModule> modules = new HashMap<>(16);

    public static void register(String moduleName) {
        if (modules.containsKey(moduleName)) {
            Log.e(TAG, "发现了重复注册的module (默认替换为新的): " + moduleName);
        }
        IModule module = findModuleByName(moduleName);
        // 执行 module 的 onCreate 方法
        module.onCreate(BRouter.application());
        modules.put(moduleName, module);
    }

    public static void unregister(String moduleName) {
        if (!modules.containsKey(moduleName)) {
            Log.e(TAG, "没有找到指定的 module : " + moduleName);
            return;
        }
        IModule remove = modules.remove(moduleName);
        if (remove != null) {
            // 执行 module 的 onDestroy 方法
            remove.onDestroy();
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static IModule findModuleByName(String moduleName) {
        try {
            Class<IModule> moduleClass = (Class<IModule>) Class.forName("com.aprz.brouter.module.BRouter$$Module$$" + moduleName);
            return moduleClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            // 简单处理一下异常
            throw new IllegalArgumentException("找不到module: " + moduleName);
        }
    }

}
