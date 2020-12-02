package com.aprz.brouter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.annotation.FragmentAnno;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.login.R;
import com.aprz.login.R2;
import com.example.component_base.Utils;
import com.example.module_login_export.service.LoginResponseBean;
import com.example.module_login_export.service.UserInfoBean;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import service.LoginServiceImpl;

@FragmentAnno(value = "login/loginFragment")
public class LoginFragment extends Fragment {

    TextInputEditText etId;
    protected TextInputLayout tilId;
    protected TextInputEditText etPassword;
    protected TextInputLayout tilPassword;
    protected View btnSignUp;
    protected View pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_login, null);
        return contentView;
    }

    public static String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etId = view.findViewById(R.id.etId);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        tilId = view.findViewById(R.id.tilId);
        etPassword = view.findViewById(R.id.etPassword);
        tilPassword = view.findViewById(R.id.tilPassword);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        pbLoading = view.findViewById(R.id.pbLoading);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                String error;
                Utils.postDelayActionToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginServiceImpl loginService = new LoginServiceImpl();
                        LoginResponseBean response = loginService.login(etId.getText().toString(), etPassword.getText().toString());
                        if (response.isSuccess()) {
                            //登录成功，跳转到个人信息页面
                            UserInfoBean bean = new UserInfoBean(response.getUserName(), "没什么可说的");
                            BRouter.getInstance().path("login/profile").withParcelable("userInfo", bean).navigate(getContext());
                        } else {
                            //提示错误信息
                            Toast.makeText(getContext(), response.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                    }
                }, 1000);
            }
        });
    }

    private void showProgress(boolean show) {
        pbLoading.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        btnSignUp.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }
}
