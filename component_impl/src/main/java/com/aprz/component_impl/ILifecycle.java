package com.aprz.component_impl;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

public interface ILifecycle {
    @UiThread
    void onCreate(@NonNull Application app);

    @UiThread
    void onDestroy();
}
