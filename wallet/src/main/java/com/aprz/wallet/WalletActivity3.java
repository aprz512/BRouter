package com.aprz.wallet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aprz.base.util.ToastUtils;
import com.aprz.brouter.annotation.Bind;
import com.aprz.brouter.annotation.Route;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.wallet.sdk.WalletRouteUrl;

@Route(path = WalletRouteUrl.WALLET_ACTIVITY3)
public class WalletActivity3 extends AppCompatActivity {

    @Bind
    String username;

    @Bind(key = "count")
    int mCount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BRouter.getInstance().inject(this);
        setContentView(R.layout.wallet_activity_test);

        long userId = getIntent().getLongExtra("userId", -1L);

        ToastUtils.sShow(this,
                "接收到的参数：" +
                        "userId = " + userId +
                        ", username = " + username);
    }

}
