package com.aprz.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.aprz.base.loading.Loading;
import com.aprz.base.util.ToastUtils;
import com.aprz.base.util.UiThreadUtils;
import com.aprz.brouter.annotation.Route;
import com.aprz.login.sdk.LoginRouteUrl;
import com.aprz.login.sdkimpl.UserManager;

@Route(path = LoginRouteUrl.LOGIN_ACTIVITY)
public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);

        Button loginButton = findViewById(R.id.btn_login);
        userNameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);

        loginButton.setOnClickListener(v -> {
            if (!checkValid()) {
                ToastUtils.sShow(this, "用户名与密码不能为空");
                return;
            }

            Loading.show(getSupportFragmentManager());
            UiThreadUtils.runDelay(() -> {
                Loading.dismiss(getSupportFragmentManager());
                UserManager.getInstance().whenLogin(userNameEditText.getText().toString(), 9527L);
                ToastUtils.sShow(this, "登录成功");
                finish();
            }, 1000L);
        });

    }

    private boolean checkValid() {
        return !TextUtils.isEmpty(userNameEditText.getText())
                && !TextUtils.isEmpty(passwordEditText.getText());
    }
}
