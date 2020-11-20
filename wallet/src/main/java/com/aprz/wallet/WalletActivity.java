package com.aprz.wallet;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aprz.brouter.annotation.Route;

@Route(path = "wallet/main")
public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        Toast.makeText(this, getIntent().getExtras().getString("key"), Toast.LENGTH_SHORT).show();
    }

}
