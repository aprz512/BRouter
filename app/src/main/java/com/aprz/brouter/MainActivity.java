package com.aprz.brouter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.component_impl.ComponentManager;
import com.aprz.component_impl.fragment.FragmentCenter;
import com.aprz.component_impl.fragment.FragmentManager;
import com.aprz.component_impl.service.ServiceManager;
import com.example.component_base.ComponentConfig;
import com.example.component_base.events.LogoutEvent;
import com.example.module_login_export.service.UserInfoBean;
import com.example.module_login_export.service.UserService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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


        //根据用户的登录信息，显示对应UI
        UserService userService = ServiceManager.get(UserService.class);
        UserService.logoutEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    hideUserWelcomeView();
                }
            }
        });

        UserService.loginEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showUserWelcomeView(userService.getUserInfoBean());
                }
            }
        });
        if (userService != null) {
            UserInfoBean userInfoBean = userService.getUserInfoBean();
            if (userInfoBean != null) {
                showUserWelcomeView(userInfoBean);
            }
        }
    }

    private void hideUserWelcomeView() {
        TextView tvUserInfo = findViewById(R.id.user_info);
        tvUserInfo.setVisibility(View.GONE);
    }

    private void showUserWelcomeView(UserInfoBean userInfoBean) {
        TextView tvUserInfo = findViewById(R.id.user_info);
        tvUserInfo.setVisibility(View.VISIBLE);
        String user_info = "Hello," + userInfoBean.getName() + "\n" + "描述信息:" + userInfoBean.getDesc();
        tvUserInfo.setText(user_info);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        if (event.getStatus() == 1) {
            //登出成功
        }
    }

    ;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}