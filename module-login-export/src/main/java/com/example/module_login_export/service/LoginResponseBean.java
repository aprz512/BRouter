package com.example.module_login_export.service;

public class LoginResponseBean {
    private String userName;
    private String errorMsg;
    private boolean isSuccess;

    public LoginResponseBean(String userName) {
        this.userName = userName;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public LoginResponseBean() {

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUserName() {
        return userName;
    }
}
