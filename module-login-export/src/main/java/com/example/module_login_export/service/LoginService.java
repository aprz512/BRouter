package com.example.module_login_export.service;

/**
 * 模块中的服务，如果要暴露给其他模块使用
 * 一般会继承一个预先定义好的接口
 */
public interface LoginService{

    LoginResponseBean login(String userName, String psw);

    boolean isUserNameValidate(String userName);
}
