package com.example.module_login_export.service;

import java.util.ArrayList;
import java.util.List;

public abstract class LoginServiceAbstract implements LoginService {
    List<ILoginCallBack> loginCallBacks = new ArrayList<>();

    void registCallback(ILoginCallBack callBack) {
        if (!loginCallBacks.contains(callBack)) {
            loginCallBacks.add(callBack);
        }
    }


    void unregistCallback(ILoginCallBack callBack) {
        loginCallBacks.remove(callBack);
    }

}
