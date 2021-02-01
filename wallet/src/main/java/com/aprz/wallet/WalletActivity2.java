package com.aprz.wallet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aprz.base.util.ToastUtils;
import com.aprz.brouter.annotation.Route;
import com.aprz.wallet.sdk.WalletRouteUrl;

@Route(path = WalletRouteUrl.Activity.MAIN_2)
public class WalletActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity_test);

        long userId = getIntent().getLongExtra("userId", -1L);
        int count = getIntent().getIntExtra("count", 0);
        String username = getIntent().getStringExtra("username");

        String stringBuilder = "接收到的参数：" +
                "userId = " + userId +
                ", count = " + count +
                ", username = " + username;
        ToastUtils.sShow(this, stringBuilder);
    }

}
