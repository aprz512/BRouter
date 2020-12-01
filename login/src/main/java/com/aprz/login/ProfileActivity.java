package com.aprz.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aprz.brouter.annotation.Route;
import com.example.module_login_export.service.UserInfoBean;

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
            showEmptyView();
        } else {
            showUserInfoView();
        }
    }

    private void showUserInfoView() {
        findViewById(R.id.content).setVisibility(View.VISIBLE);
        TextView name = findViewById(R.id.name);
        TextView desc = findViewById(R.id.desc);
        name.setText(userInfoBean.getName());
        desc.setText(userInfoBean.getDesc());
    }

    private void showEmptyView() {
        findViewById(R.id.empty).setVisibility(View.VISIBLE);
    }
}
