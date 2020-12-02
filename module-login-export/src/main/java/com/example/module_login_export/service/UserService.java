package com.example.module_login_export.service;

import androidx.annotation.Nullable;

public interface UserService {

    @Nullable
    public UserInfoBean getUserInfoBean();

    public boolean isUserInfoValidate();

}
