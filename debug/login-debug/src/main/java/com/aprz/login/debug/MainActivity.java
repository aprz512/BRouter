package com.aprz.login.debug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.login.sdk.LoginRouteUrl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(v -> {
            BRouter.getInstance().path(LoginRouteUrl.LOGIN_ACTIVITY).navigate(this);
        });
    }
}