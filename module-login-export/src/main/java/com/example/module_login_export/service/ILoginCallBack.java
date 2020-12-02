package com.example.module_login_export.service;

public interface ILoginCallBack {
    void onLoginSuccess(LoginResponseBean responseBean);

    void onLoginFailed(int errorCode, String msg);
}
