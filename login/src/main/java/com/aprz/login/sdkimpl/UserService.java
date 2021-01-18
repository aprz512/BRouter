package com.aprz.login.sdkimpl;

import androidx.lifecycle.LiveData;

import com.aprz.brouter.annotation.Service;
import com.aprz.login.sdk.IUserService;
import com.aprz.login.sdk.User;

@Service(name = IUserService.NAME)
public class UserService implements IUserService {

    @Override
    public LiveData<User> getUserStream() {
        return UserManager.getInstance().userStream();
    }

    @Override
    public User getUser() {
        return UserManager.getInstance().getUser();
    }
}
