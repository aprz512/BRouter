package com.aprz.login.debug;

import android.app.Application;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.module.ModuleHelper;

public class LoginDebugApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BRouter.init(this, false);
        ModuleHelper.register("login");
    }

}
