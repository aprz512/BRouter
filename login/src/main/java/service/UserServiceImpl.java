package service;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.module_login_export.service.UserInfoBean;
import com.example.module_login_export.service.UserService;

public class UserServiceImpl implements UserService {
    @Nullable
    @Override
    public UserInfoBean getUserInfoBean() {
        return null;
    }

    @Override
    public boolean isUserInfoValidate() {
        return false;
    }

    @Override
    public void init(Context context) {

    }
}
