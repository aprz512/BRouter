package com.aprz.login.sdkimpl;

import com.aprz.brouter.annotation.Service;
import com.aprz.login.sdk.IUserService;
import com.aprz.login.sdk.User;

@Service(name = IUserService.NAME)
public class UserService implements IUserService {

    @Override
    public User getUserInfo() {
        return UserManager.getInstance().getUser();
    }

}
