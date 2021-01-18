package com.aprz.base.ext;

import androidx.lifecycle.Observer;

public interface ObserverExt<T> extends Observer<T> {

    @Override
    default void onChanged(T t) {
        if (t != null) {
            onChangedIfNotNull(t);
        }
    }

    void onChangedIfNotNull(T t);
}
