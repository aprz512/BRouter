package com.aprz.brouter;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.aprz.base.activity.BaseActivity;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.home.sdk.HomeRouteUrl;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        postDelay(() -> {
                    BRouter.getInstance().path(HomeRouteUrl.Activity.MAIN).navigate(this);
                    finish();
                },
                1000L);
    }
}
