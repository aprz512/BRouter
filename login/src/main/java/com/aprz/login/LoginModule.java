package com.aprz.login;

import android.app.Application;

import com.aprz.base.util.ToastUtils;
import com.aprz.brouter.annotation.Module;
import com.aprz.brouter.api.module.IModule;

@Module
public class LoginModule implements IModule {

    @Override
    public void onCreate(Application application) {
        // 测试一下 application
        ToastUtils.lShow(application.getApplicationContext(),
                application.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {

    }
}
