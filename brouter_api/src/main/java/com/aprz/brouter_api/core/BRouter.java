package com.aprz.brouter_api.core;

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
            RouteHelper.injectRouteByPlugin();
        } else {
            RouteHelper.loadRoute(context);
        }
    }

    public void navigate(String path) {
        Class<? extends Activity> targetClass = RouteStore.getRouteMap().get(path);
        if (targetClass != null) {
            sContext.startActivity(new Intent(sContext, targetClass));
        }
    }

}
