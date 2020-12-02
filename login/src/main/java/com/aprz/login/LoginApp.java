package com.aprz.login;

import android.app.Application;

import com.aprz.brouter.api.core.BRouter;

import service.AppService;

public class LoginApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BRouter.init(this, false);
        AppService.getInstance().initContext(this);
    }
}
