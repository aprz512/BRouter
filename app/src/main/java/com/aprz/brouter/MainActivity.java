package com.aprz.brouter;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.aprz.brouter.api.core.BRouter;

public class MainActivity extends AppCompatActivity {
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.fragmentContainer);
        findViewById(R.id.navigate_to_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("count", 30);
                bundle.putString("message", "hello");
                bundle.putLong("userId", 9527);
                BRouter.getInstance().path("wallet/main").params(bundle).navigate();
            }
        });

        findViewById(R.id.jump2Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().path("login/main").navigate();
            }
        });


        findViewById(R.id.btn_test_interceptors).setOnClickListener((v) -> {
            BRouter.getInstance().path("xxx/404").navigate(this);
        });
    }
}