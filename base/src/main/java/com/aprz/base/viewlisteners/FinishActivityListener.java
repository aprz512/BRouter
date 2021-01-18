package com.aprz.base.viewlisteners;

import android.app.Activity;
import android.view.View;

import java.lang.ref.WeakReference;

public class FinishActivityListener implements View.OnClickListener {

    private final WeakReference<Activity> target;

    public FinishActivityListener(Activity target) {
        this.target = new WeakReference<>(target);
    }

    @Override
    public void onClick(View v) {
        if (target.get() != null) {
            target.get().finish();
        }
    }

}
