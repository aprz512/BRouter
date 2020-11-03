package com.aprz.brouter;

import android.app.Application;
import android.content.Context;

import com.aprz.brouter_api.core.BRouter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BRouter.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
