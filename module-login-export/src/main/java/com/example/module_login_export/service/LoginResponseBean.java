package com.example.module_login_export.service;

public class LoginResponseBean {
    private String userName;

    public LoginResponseBean(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
