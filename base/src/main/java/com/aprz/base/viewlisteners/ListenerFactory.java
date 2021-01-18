package com.aprz.base.viewlisteners;

import android.app.Activity;

public class ListenerFactory {

    public static FinishActivityListener finish(Activity target) {
        return new FinishActivityListener(target);
    }

}
