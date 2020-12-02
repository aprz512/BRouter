package com.aprz.brouter;

import android.app.Application;
import android.content.Context;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.component_impl.Component;
import com.aprz.component_impl.ComponentManager;
import com.aprz.component_impl.Config;
import com.aprz.component_impl.fragment.FragmentCenter;
import com.example.component_base.ComponentConfig;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BRouter.init(this, false);

        Component.init(
                BuildConfig.DEBUG,
                Config.with(this).build());

        //初始化组件信息
        FragmentCenter.getInstance().register("login");
        ComponentManager.getInstance().register(ComponentConfig.ComponentLogin.NAME);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
