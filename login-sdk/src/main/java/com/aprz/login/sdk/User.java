package com.aprz.login.sdk;

import androidx.annotation.NonNull;

public class User {

    private boolean isLogin;

    private String userName;
    private long userId;

    public User(boolean isLogin, String userName, long userId) {
        this.isLogin = isLogin;
        this.userName = userName;
        this.userId = userId;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "isLogin=" + isLogin +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
