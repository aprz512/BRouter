package com.aprz.brouter.api.fragment;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

class FragmentStore {
    private static final Map<String, Class<? extends Fragment>> fragmentMap = new HashMap<>();

    public static void put(String key, Class<? extends Fragment> fragmentClass) {
        fragmentMap.put(key, fragmentClass);
    }

    public static Class<? extends Fragment> get(String key) {
        return fragmentMap.get(key);
    }
}
