package com.aprz.login.sdkimpl;

import com.aprz.login.sdk.User;

public class UserManager {

    private static class Holder {
        private static final UserManager sInstance = new UserManager();
    }

    private UserManager() {
    }

    private User user = new User(false, "", -1L);

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
    }

    public void whenLogout() {
        this.user.setLogin(false);
        this.user.setUserName("");
        this.user.setUserId(-1L);
    }

    public User getUser() {
        // 返回一个 clone 对象
        return new User(user.isLogin(), user.getUserName(), user.getUserId());
    }
}
