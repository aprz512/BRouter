package com.aprz.wallet;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aprz.brouter.annotation.Bind;
import com.aprz.brouter.annotation.Route;
import com.aprz.brouter.api.core.BRouter;

@Route(path = Constants.RoutePath.WALLET_ACTIVITY)
public class WalletActivity extends AppCompatActivity {

    @Bind
    String message;

    @Bind(key = "count")
    int mCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BRouter.getInstance().inject(this);

        setContentView(R.layout.wallet_activity);

        Toast.makeText(this, "message = " + message + ", mCount = " + mCount, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            Toast.makeText(this, "哎呀，我崩溃了", Toast.LENGTH_SHORT).show();
            CrashMonitor.pageCrashed(Constants.RoutePath.WALLET_ACTIVITY);
            finish();
        }, 3000L);
    }

}
