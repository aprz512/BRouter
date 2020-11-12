package com.aprz.brouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.component_impl.Component;
import com.aprz.component_impl.ComponentManager;
import com.example.component_api.ComponentConstants;
import com.example.component_base.ComponentConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().navigate("wallet/main");
            }
        });

        findViewById(R.id.jump2Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().navigate("login/main");
            }
        });
        //加载登录模块
        findViewById(R.id.registLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentManager.getInstance().register(ComponentConfig.ComponentLogin.NAME);
            }
        });
        //卸载登录模块
        findViewById(R.id.unregistLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentManager.getInstance().unregister(ComponentConfig.ComponentLogin.NAME);
            }
        });
    }
}