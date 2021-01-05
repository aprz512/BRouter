package com.aprz.brouter.api.module;

import android.app.Application;

public interface IModule {

    void onCreate(Application application);

    void onDestroy();

}
