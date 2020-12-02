package com.aprz.component_impl.service;

import androidx.annotation.NonNull;

import com.aprz.component_impl.Component;
import com.aprz.component_impl.IComponentHostFragment;
import com.example.component_api.ComponentUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理所有模块的服务
 * <p>
 * 使用场景：
 * 比如UserModule和LoginModule
 * 模块名host为key,对应的辅助类，如LoginServiceGenerated为value
 * <p>
 * 卸载模块中的fragment则调用{@link #unregister(String host)}
 */
public class ServiceCenter {
    private Map<String, IComponentService> moduleFragmentMap = new HashMap<>();

    private ServiceCenter() {
    }

    private static volatile ServiceCenter instance;

    public static ServiceCenter getInstance() {
        if (instance == null) {
            synchronized (ServiceCenter.class) {
                if (instance == null) {
                    instance = new ServiceCenter();
                }
            }
        }
        return instance;
    }

    public void unregister(@NonNull String host) {
        IComponentService moduleService = moduleFragmentMap.get(host);
        if (moduleService != null) {
            unregister(moduleService);
        }
    }

    public void unregister(@NonNull IComponentService hostFragment) {
        moduleFragmentMap.remove(hostFragment.getName());
        hostFragment.onDestroy();
    }

    public void register(@NonNull IComponentService hostFragment) {
        if (!moduleFragmentMap.containsKey(hostFragment.getName())) {
            moduleFragmentMap.put(hostFragment.getName(), hostFragment);
            hostFragment.onCreate(Component.getApplication());
        }
    }

    public void register(@NonNull String host) {
        if (!moduleFragmentMap.containsKey(host)) {
            IComponentService moduleService = findModuleService(host);
            if (moduleService != null) {
                register(moduleService);
            }
        }
    }

    private IComponentService findModuleService(String host) {
        try {

            Class<? extends IComponentService> clazz = null;
            String className = ComponentUtil.genHostServiceClassName(host);
            clazz = (Class<? extends IComponentService>) Class.forName(className);
            return clazz.newInstance();
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }
}
