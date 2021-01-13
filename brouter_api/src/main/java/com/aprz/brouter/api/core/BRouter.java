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

    public static Application application() {
        return (Application) sContext;
    }

    public static Context context() {
        return sContext;
    }

    public Navigation path(String path) {
        String module = getModule(path);
        RouteStore.completion(path, module);
        Navigation navigation = RouteStore.getNavigation(path, module);
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

    /**
     * 要求跳转路径使用 二级结构，如：wallet/xxx
     * 因为注解处理器没有做复杂的分组功能，所以，还要求一级路径名为 module 名字
     *
     * @return 返回一级路径名
     */
    private static String getModule(String path) {
        if (!path.contains("/")) {
            // 有了 404， 这个就暂时不强制抛出异常了
            try {
                throw new IllegalStateException("路径格式不对：" + path);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            return "unknown";
        }
        return path.substring(0, path.indexOf("/"));
    }

}
