package com.aprz.component_impl.fragment;

import androidx.annotation.NonNull;

import com.aprz.component_impl.Component;
import com.aprz.component_impl.IComponentHostFragment;
import com.example.component_api.ComponentUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理模块中Fragment的加载和卸载
 * <p>
 * 使用场景：
 * 此时你希望注册模块A中的所有fragment，让其暴露给其他模块调用，则调用{@link #register(IComponentHostFragment)}
 * 卸载模块中的fragment则调用{@link #unregister(String)}
 */
public class FragmentCenter {
    private Map<String, IComponentHostFragment> moduleFragmentMap = new HashMap<>();

    private FragmentCenter() {
    }

    private static volatile FragmentCenter instance;

    public static FragmentCenter getInstance() {
        if (instance == null) {
            synchronized (FragmentCenter.class) {
                if (instance == null) {
                    instance = new FragmentCenter();
                }
            }
        }
        return instance;
    }

    public void unregister(@NonNull String host) {
        IComponentHostFragment moduleService = moduleFragmentMap.get(host);
        if (moduleService != null) {
            unregister(moduleService);
        }
    }

    public void unregister(@NonNull IComponentHostFragment hostFragment) {
        moduleFragmentMap.remove(hostFragment.getName());
        hostFragment.onDestroy();
    }

    public void register(@NonNull IComponentHostFragment hostFragment) {
        if (!moduleFragmentMap.containsKey(hostFragment.getName())) {
            moduleFragmentMap.put(hostFragment.getName(), hostFragment);
            hostFragment.onCreate(Component.getApplication());
        }
    }

    public void register(@NonNull String host) {
        if (!moduleFragmentMap.containsKey(host)) {
            IComponentHostFragment moduleService = findModuleService(host);
            if (moduleService != null) {
                register(moduleService);
            }
        }
    }

    private IComponentHostFragment findModuleService(String host) {
        try {

            Class<? extends IComponentHostFragment> clazz = null;
            String className = ComponentUtil.genHostFragmentClassName(host);
            clazz = (Class<? extends IComponentHostFragment>) Class.forName(className);
            return clazz.newInstance();
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }
}
