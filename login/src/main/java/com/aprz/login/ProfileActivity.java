package com.aprz.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aprz.brouter.annotation.Route;
import com.aprz.user.UserManager;
import com.example.component_base.events.LogoutEvent;
import com.example.module_login_export.service.UserInfoBean;
import com.example.module_login_export.service.UserService;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * 个人信息页
 */
@Route(path = "login/profile")
public class ProfileActivity extends AppCompatActivity {
    UserInfoBean userInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userInfoBean = getIntent().getParcelableExtra("userInfo");
        if (userInfoBean == null) {
            userInfoBean = UserManager.getInstance().getUserInfoBean();
        }
        UserManager.getInstance().updateUser(userInfoBean);
        refreshUI();
    }

    private void showUserInfoView() {
        findViewById(R.id.content).setVisibility(View.VISIBLE);
        View empty = findViewById(R.id.empty);
        if (empty.getVisibility() == View.VISIBLE) {
            empty.setVisibility(View.GONE);
        }
        TextView name = findViewById(R.id.name);
        TextView desc = findViewById(R.id.desc);
        name.setText(userInfoBean.getName());
        desc.setText(userInfoBean.getDesc());
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除本地的用户信息，刷新页面
                boolean flag = UserManager.getInstance().clearUserInfo();
                if (flag) {
                    userInfoBean = null;
                    refreshUI();
                    UserService.logoutEvent.setValue(true);
                }
            }
        });
    }

    private void refreshUI() {
        if (userInfoBean == null) {
            showEmptyView();
        } else {
            showUserInfoView();
        }
    }

    private void showEmptyView() {
        findViewById(R.id.empty).setVisibility(View.VISIBLE);
        View content = findViewById(R.id.content);
        if (content.getVisibility() == View.VISIBLE) {
            content.setVisibility(View.GONE);
        }
    }
}
