package com.aprz.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aprz.brouter.LoginFragment;
import com.aprz.brouter.annotation.Route;

@Route(path = "login/main")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment,new LoginFragment()).commit();
    }
}
