package com.aprz.base.activity;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    protected void post(Runnable runnable) {
        handler.post(runnable);
    }

    protected void postDelay(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

}
