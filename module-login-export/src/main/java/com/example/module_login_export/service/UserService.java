package com.example.module_login_export.service;

import androidx.annotation.Nullable;

import com.aprz.brouter.api.IProvider;

public interface UserService extends IProvider {

    @Nullable
    public UserInfoBean getUserInfoBean();

    public boolean isUserInfoValidate();

}
