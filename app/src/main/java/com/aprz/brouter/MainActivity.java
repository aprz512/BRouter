package com.aprz.brouter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.component_impl.Component;
import com.aprz.component_impl.ComponentManager;
import com.aprz.component_impl.fragment.FragmentCenter;
import com.aprz.component_impl.fragment.FragmentManager;
import com.example.component_api.ComponentConstants;
import com.example.component_base.ComponentConfig;

public class MainActivity extends AppCompatActivity {
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.fragmentContainer);
        findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", "value");
                BRouter.getInstance().path("wallet/main").params(bundle).navigate();
            }
        });

        findViewById(R.id.jump2Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().path("login/main").navigate();
            }
        });

        findViewById(R.id.jump2Profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().path("login/profile").navigate();
            }
        });

        //加载登录模块
        findViewById(R.id.registLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCenter.getInstance().register("login");
                ComponentManager.getInstance().register(ComponentConfig.ComponentLogin.NAME);
            }
        });
        //卸载登录模块
        findViewById(R.id.unregistLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCenter.getInstance().unregister("login");
                ComponentManager.unregister(ComponentConfig.ComponentLogin.NAME);
            }
        });
        //加载登录fragment
        findViewById(R.id.loadLoginFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = FragmentManager.get("login/loginFragment");
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
                } else {
                    Toast.makeText(MainActivity.this, "can not find path login/loginFragment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}