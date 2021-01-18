package com.aprz.login.sdkimpl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aprz.login.sdk.User;

public class UserManager {

    private static class Holder {
        private static final UserManager sInstance = new UserManager();
    }

    private UserManager() {
        userStream.postValue(user);
    }

    private final User user = new User(false, "", -1L);

    private final MutableLiveData<User> userStream = new MutableLiveData<>();

    public static UserManager getInstance() {
        return Holder.sInstance;
    }

    private Object readResolve() {
        return Holder.sInstance;
    }

    public void whenLogin(String userName, long userId) {
        this.user.setLogin(true);
        this.user.setUserName(userName);
        this.user.setUserId(userId);
        userStream.postValue(new User(true, userName, userId));
    }

    public void whenLogout() {
        this.user.setLogin(false);
        this.user.setUserName("");
        this.user.setUserId(-1L);
        userStream.postValue(new User(false, "", -1L));
    }

    public LiveData<User> userStream() {
        return userStream;
    }

    public User getUser() {
        // 返回一个 clone 对象
        return new User(user.isLogin(), user.getUserName(), user.getUserId());
    }
}
