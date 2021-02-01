package com.aprz.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aprz.base.activity.BaseActivity;
import com.aprz.base.loading.Loading;
import com.aprz.base.util.ToastUtils;
import com.aprz.brouter.annotation.Route;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.core.Navigation;
import com.aprz.brouter.api.interceptor.IRouteInterceptor;
import com.aprz.login.sdk.LoginRouteUrl;
import com.aprz.login.sdkimpl.UserManager;

@Route(path = LoginRouteUrl.Activity.MAIN)
public class LoginActivity extends BaseActivity {

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
            postDelay(() -> {
                Loading.dismiss(getSupportFragmentManager());
                UserManager.getInstance().whenLogin(userNameEditText.getText().toString(), 9527L);
                ToastUtils.sShow(this, "登录成功");
                finish();
            }, 1000L);
        });

        TextView linkSignUpText = findViewById(R.id.link_sign_up);
        linkSignUpText.setOnClickListener(v -> BRouter.getInstance()
                .path("login/signUp")
                .navigate(this, new IRouteInterceptor.Callback() {
                    @Override
                    public void onSuccess(@NonNull Navigation navigation) {

                    }

                    @Override
                    public void onFail(@NonNull Throwable exception) {
                        ToastUtils.sShow(linkSignUpText.getContext(), "暂不支持该功能");
                    }
                }));

    }

    private boolean checkValid() {
        return !TextUtils.isEmpty(userNameEditText.getText())
                && !TextUtils.isEmpty(passwordEditText.getText());
    }
}
