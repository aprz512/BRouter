package com.aprz.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.api.core.BRouter;
import com.aprz.home.R;
import com.aprz.wallet.sdk.WalletRouteUrl;

/**
 * 测试组件之间 activity 的跳转
 */
public class NavigationFragment extends BaseViewPagerFragment {

    @Override
    protected int getFragmentContentId() {
        return R.layout.home_fragment_navigation;
    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.navigate_to_wallet2).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("count", 10);
            bundle.putString("username", "who am i");
            bundle.putLong("userId", 1L);
            BRouter.getInstance().path(WalletRouteUrl.Activity.MAIN_2).params(bundle).navigate();
        });

        view.findViewById(R.id.navigate_to_wallet3).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("count", 100);
            bundle.putString("username", "monica");
            bundle.putLong("userId", 2L);
            BRouter.getInstance().path(WalletRouteUrl.Activity.MAIN_3).params(bundle).navigate();
        });
    }
}
