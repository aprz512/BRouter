package com.aprz.brouter.api.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BRouter {

    private static final class Holder {
        private static final BRouter instance = new BRouter();
    }

    public static BRouter getInstance() {
        return Holder.instance;
    }

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Application context) {
        init(context, false);
    }

    public static void init(Application context, boolean registerByPlugin) {
        sContext = context;
        if (registerByPlugin) {
            RouteHelper.injectModuleByPlugin();
        } else {
            RouteHelper.loadRoute(context);
        }
    }

    public Navigation path(String path) {
        RouteStore.completion(path);
        Navigation navigation = RouteStore.getNavigation(path);
        navigation.setAppContext(sContext);
        return navigation;
    }

    public void inject(Activity activity) {
        try {
            Class<?> bind = Class.forName(activity.getClass().getCanonicalName() + "_Bind");
            Constructor<?> constructor = bind.getConstructor(activity.getClass());
            // 这里就已经赋值完成了
            constructor.newInstance(activity);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
