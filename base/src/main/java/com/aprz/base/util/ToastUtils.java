package com.aprz.base.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void sShow(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void lShow(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
