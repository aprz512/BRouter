package com.aprz.brouter.api.degrade;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aprz.brouter.api.core.Navigation;

import java.util.ArrayList;
import java.util.List;

public class DegradeHelper {

    private static final String TAG = "DegradeHelper";

    public static void addModuleDegrade(String moduleName) {
        IModuleDegrade moduleDegrade = findModuleDegrade(moduleName);
        List<IRouteDegrade> degrades = moduleDegrade.degrades();
        for(IRouteDegrade degrade : degrades) {
            DegradeStore.addRouteDegrade(degrade);
        }
    }

    public static IRouteDegrade getRouteDegrade(Navigation navigation) {
        List<IRouteDegrade> degradeList = DegradeStore.getDegradeList();
        for (IRouteDegrade degrade : degradeList) {
            if (degrade.isMatch(navigation)) {
                return degrade;
            }
        }

        return null;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    private static IModuleDegrade findModuleDegrade(String module) {
        try {
            Class<IModuleDegrade> degradeClass =
                    (Class<IModuleDegrade>) Class.forName("com.aprz.brouter.degrades.BRouter$$Degrade$$" + module);
            return degradeClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignore) {
            Log.e(TAG, "没有在 module 内部找到降级策略: " + module);
            return new EmptyModuleDegrade();
        }
    }


    static class EmptyModuleDegrade implements IModuleDegrade {
        @Override
        public List<IRouteDegrade> degrades() {
            return new ArrayList<>(0);
        }
    }


}
