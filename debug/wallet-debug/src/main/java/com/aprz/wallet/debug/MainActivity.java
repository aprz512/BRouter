package com.aprz.wallet.debug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.wallet.sdk.WalletRouteUrl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.wallet_test).setOnClickListener(v -> {
            BRouter.getInstance().path(WalletRouteUrl.WALLET_ACTIVITY2).navigate(this);
        });
    }
}