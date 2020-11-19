package com.aprz.brouter.api.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

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

    public void navigate(String path) {
        navigate(sContext, path);
    }

    public void navigate(Context context, String path) {
        RouteStore.completion(path);
        Class<? extends Activity> targetClass = RouteStore.getRouteMap().get(path);
        if (targetClass != null && context != null) {
            Intent intent = new Intent(context, targetClass);
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

}
