package com.aprz.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.home.R;
import com.aprz.wallet.sdk.WalletRouteUrl;


/**
 * 测试降级策略
 */
public class DegradeFragment extends BaseViewPagerFragment {

    @Override
    protected void initView(View view) {
        Button testWalletDegradeButton = view.findViewById(R.id.test_wallet_degrade);
        testWalletDegradeButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("count", 10);
            bundle.putString("message", "hello brouter");
            bundle.putLong("userId", 1L);
            BRouter.getInstance().path(WalletRouteUrl.Activity.MAIN).params(bundle).navigate();
        });
    }

    @Override
    protected int getFragmentContentId() {
        return R.layout.home_fragment_degrade;
    }
}
