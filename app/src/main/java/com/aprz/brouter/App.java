package com.aprz.brouter;

import android.app.Application;
import android.content.Context;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.module.ModuleHelper;
import com.aprz.component_impl.Component;
import com.aprz.component_impl.Config;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BRouter.init(this, false);
        ModuleHelper.register("app");
        ModuleHelper.register("wallet");

        Component.init(
                BuildConfig.DEBUG,
                Config.with(this).build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
