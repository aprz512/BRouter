package com.aprz.brouter.api.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FragmentHelper {

    private static final String TAG = "FragmentHelper";

    public static void addModuleFragment(String moduleName) {
        IModuleFragment moduleFragment = findModuleFragment(moduleName);
        Map<String, Class<? extends Fragment>> fragments = moduleFragment.fragments();
        Set<Map.Entry<String, Class<? extends Fragment>>> entries = fragments.entrySet();
        for (Map.Entry<String, Class<? extends Fragment>> entry : entries) {
            FragmentStore.put(entry.getKey(), entry.getValue());
        }
    }

    public static Fragment getFragment(String fragmentKey) {
        return getFragment(fragmentKey, null);
    }

    public static Fragment getFragment(String fragmentKey, Bundle args) {
        Class<? extends Fragment> fClass = FragmentStore.get(fragmentKey);
        if (fClass != null) {
            try {
                Fragment fragment = fClass.newInstance();
                if (args != null) {
                    fragment.setArguments(args);
                }
                return fragment;
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new IllegalArgumentException("没有找到与 " + fragmentKey + " 对应的 Fragment");
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static IModuleFragment findModuleFragment(String module) {
        try {
            Class<IModuleFragment> degradeClass =
                    (Class<IModuleFragment>) Class.forName("com.aprz.brouter.fragments.BRouter$$Fragment$$" + module);
            return degradeClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignore) {
            Log.e(TAG, "没有在 module 内部找到Fragment: " + module);
            return new EmptyModuleFragment();
        }
    }


    static class EmptyModuleFragment implements IModuleFragment {
        @Override
        public Map<String, Class<? extends Fragment>> fragments() {
            return new HashMap<>(0);
        }
    }

}
