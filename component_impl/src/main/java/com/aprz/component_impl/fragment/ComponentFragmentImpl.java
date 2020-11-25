package com.aprz.component_impl.fragment;

import android.app.Application;

import androidx.annotation.NonNull;

import com.aprz.component_impl.IComponentHostFragment;
import com.aprz.component_impl.IComponentLifecycle;

import java.util.Collections;
import java.util.Set;

/**
 * 该类作为Fragment注解生成的辅助类的父类，定义相关规范
 */
public class ComponentFragmentImpl implements IComponentHostFragment {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void onCreate(@NonNull Application app) {

    }

    @Override
    public void onDestroy() {

    }

    @NonNull
    @Override
    public Set<String> getNameSet() {
        return Collections.EMPTY_SET;
    }
}
