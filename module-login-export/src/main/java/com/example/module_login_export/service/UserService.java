package com.example.module_login_export.service;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public interface UserService {
    MutableLiveData<Boolean> logoutEvent = new MutableLiveData<>();
    MutableLiveData<Boolean> loginEvent = new MutableLiveData<>();


    @Nullable
     UserInfoBean getUserInfoBean();

     boolean isUserInfoValidate();

}
