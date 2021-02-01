package com.aprz.brouter;

import android.app.Application;
import android.content.Context;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.module.ModuleHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BRouter.init(this, false);
        ModuleHelper.register("app");
        ModuleHelper.register("wallet");
        ModuleHelper.register("login");
        ModuleHelper.register("card");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
