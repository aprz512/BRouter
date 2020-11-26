package com.aprz.component_impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.component_api.ComponentUtil;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {

    private static final String TAG = "ComponentManager";
    /**
     * 单例对象
     */
    private static volatile ComponentManager instance;

    public ComponentManager() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static ComponentManager getInstance() {
        if (instance == null) {
            synchronized (ComponentManager.class) {
                if (instance == null) {
                    instance = new ComponentManager();
                }
            }
        }
        return instance;
    }

    /**
     * 维护所有注册了生命周期的类
     */
    private static Map<String, IComponentLifecycle> moduleApplicationMap = new HashMap<>();

    public void register(@NonNull String host) {
        if (moduleApplicationMap.containsKey(host)) {
            Log.i(TAG, "The module \"" + host + "\" is already registered");
        } else {
            IComponentLifecycle componentApplication = findComponentApplication(host);
            if (componentApplication == null) {
                Log.i(TAG, "模块 \"" + host + "\" 加载失败");
            } else {
                register(componentApplication);
            }
        }
    }

    public static void unregister(@NonNull String host) {

        IComponentLifecycle moduleApp = moduleApplicationMap.get(host);
        if (moduleApp == null) {
            Log.i(TAG, "模块 '" + host + "' 卸载失败");
        } else {
            Log.i(TAG, "模块 '" + host + "' 卸载成功");
            unregister(moduleApp);
        }
    }

    private static void unregister(IComponentLifecycle moduleApp) {
        moduleApplicationMap.remove(moduleApp.getName());
        moduleApp.onDestroy();
    }

    private void register(IComponentLifecycle componentApplication) {
        if (moduleApplicationMap.containsKey(componentApplication.getName())) {
        } else {
            moduleApplicationMap.put(componentApplication.getName(), componentApplication);
            componentApplication.onCreate(Component.getApplication());
        }
    }

    private IComponentLifecycle findComponentApplication(String host) {
        IComponentLifecycle result = null;
        try {
            /**
             *找到注解生成的辅助类
             */
            Class<?> clazz = Class.forName(ComponentUtil.genHostModuleApplicationClassName(host));
            result = (IComponentLifecycle) clazz.newInstance();
        } catch (Exception ignore) {
            // ignore
        }
        return result;
    }
}
