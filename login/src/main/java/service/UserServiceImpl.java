package service;

import android.content.Context;

import androidx.annotation.Nullable;

import com.aprz.brouter.annotation.ServiceAnno;
import com.aprz.user.UserManager;
import com.example.module_login_export.service.UserInfoBean;
import com.example.module_login_export.service.UserService;

@ServiceAnno(value = UserService.class)
public class UserServiceImpl implements UserService {
    @Nullable
    @Override
    public UserInfoBean getUserInfoBean() {
        return UserManager.getInstance().getUserInfoBean();
    }

    @Override
    public boolean isUserInfoValidate() {
        return false;
    }


}
