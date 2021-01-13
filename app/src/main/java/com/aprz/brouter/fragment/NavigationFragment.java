package com.aprz.brouter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.R;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.wallet.sdk.WalletRouteUrl;

public class NavigationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.app_fragment_navigation, container, false);

        view.findViewById(R.id.navigate_to_login).setOnClickListener(v -> BRouter.getInstance().path("login/main").navigate());
        view.findViewById(R.id.navigate_to_wallet).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("count", 10);
            bundle.putString("message", "hello");
            bundle.putLong("userId", 9527L);
            BRouter.getInstance().path(WalletRouteUrl.WALLET_ACTIVITY).params(bundle).navigate();
        });
        view.findViewById(R.id.navigate_to_wallet2).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("count", 100);
            bundle.putString("message", "world");
            bundle.putLong("userId", 9527L);
            BRouter.getInstance().path(WalletRouteUrl.WALLET_ACTIVITY2).params(bundle).navigate();
        });

        return view;
    }
}
