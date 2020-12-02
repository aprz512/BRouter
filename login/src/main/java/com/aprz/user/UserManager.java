package com.aprz.user;

import com.aprz.component_impl.fragment.FragmentCenter;
import com.aprz.utils.SPUtil;
import com.example.module_login_export.service.UserInfoBean;
import com.google.gson.Gson;

import service.AppService;

/**
 * 管理用户信息
 * 模块内部使用
 */
public class UserManager {
    private UserInfoBean _userInfoBean;
    private static final String KEY_USER = "user";

    private UserManager() {
    }

    private static volatile UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    public void updateUser(UserInfoBean userInfoBean) {
        if (_userInfoBean == null) {
            _userInfoBean = userInfoBean;
        }
        if (userInfoBean == null) {
            return;
        }

        if (_userInfoBean.equals(userInfoBean)) {
            return;
        }
        //保存到sp
        try {
            SPUtil.saveData(AppService.getInstance().getContext(), KEY_USER, new Gson().toJson(userInfoBean));
        } catch (Exception e) {

        }
    }

    //清除当前的用户信息
    public boolean clearUserInfo() {
        if (_userInfoBean == null) {
            return true;
        }
        _userInfoBean = null;
        SPUtil.saveData(AppService.getInstance().getContext(), KEY_USER, new Gson().toJson(_userInfoBean));
        return true;
    }

    public UserInfoBean getUserInfoBean() {
        if (_userInfoBean != null) {
            return _userInfoBean;
        } else {
            return fetchUserInfoFromSp();
        }
    }

    private UserInfoBean fetchUserInfoFromSp() {
        return new Gson().fromJson(SPUtil.getStringData(AppService.getInstance().getContext(), KEY_USER), UserInfoBean.class);
    }
}
