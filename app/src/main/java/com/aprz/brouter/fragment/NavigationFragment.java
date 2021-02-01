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
import com.aprz.brouter.api.service.ServiceHelper;
import com.aprz.login.sdk.IUserService;
import com.aprz.login.sdk.User;
import com.aprz.wallet.sdk.WalletRouteUrl;

/**
 * 测试组件之间 activity 的跳转
 */
public class NavigationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.app_fragment_navigation, container, false);

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

        return view;
    }
}
