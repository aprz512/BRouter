package com.aprz.component_impl;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import java.util.Set;

public interface IComponentHostFragment extends ILifecycle,IHost{
    @NonNull
    @UiThread
    Set<String> getNameSet();
}
