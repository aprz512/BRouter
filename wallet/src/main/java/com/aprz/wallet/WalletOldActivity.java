package com.aprz.wallet;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aprz.brouter.annotation.Bind;
import com.aprz.brouter.annotation.Route;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.wallet.sdk.WalletRouteUrl;

@Route(path = WalletRouteUrl.WALLET_OLD_ACTIVITY)
public class WalletOldActivity extends AppCompatActivity {

    @Bind
    String message;

    @Bind(key = "count")
    int mCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BRouter.getInstance().inject(this);

        setContentView(R.layout.wallet_old_activity);

        Toast.makeText(this, "message = " + message + ", mCount = " + mCount, Toast.LENGTH_SHORT).show();
    }

}
