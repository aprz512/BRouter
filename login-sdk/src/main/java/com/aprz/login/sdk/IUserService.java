package com.aprz.login.sdk;


import androidx.lifecycle.LiveData;

public interface IUserService {

    String NAME = "service/user";

    LiveData<User> getUserStream();

    User getUser();

}
