package com.aprz.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aprz.brouter.annotation.Route;
import com.aprz.login.sdkimpl.UserManager;

@Route(path = "login/main")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserManager.getInstance().whenLogin("aprz512", 1L);
    }
}
