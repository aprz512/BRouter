package com.aprz.wallet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aprz.brouter.annotation.Route;
import com.aprz.wallet.sdk.WalletRouteUrl;

@Route(path = WalletRouteUrl.WALLET_ACTIVITY)
public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);
    }

}
