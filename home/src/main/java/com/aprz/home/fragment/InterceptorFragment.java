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
import com.aprz.brouter.api.service.ServiceHelper;
import com.aprz.home.R;
import com.aprz.login.sdk.IUserService;
import com.aprz.login.sdk.LoginRouteUrl;
import com.aprz.login.sdk.User;
import com.aprz.wallet.sdk.WalletRouteUrl;

/**
 * 测试拦截器
 */
public class InterceptorFragment extends BaseViewPagerFragment {

    @Override
    protected int getFragmentContentId() {
        return R.layout.home_fragment_interceptor;
    }

    @Override
    protected void initView(View view) {

        Button navigateUnknownButton = view.findViewById(R.id.navigate_to_unknown);
        Button navigateLoginButton = view.findViewById(R.id.navigate_to_login);
        Button navigateWalletButton = view.findViewById(R.id.navigate_to_wallet_n);
        Button navigateWalletAfterLoginButton = view.findViewById(R.id.navigate_to_login_then_wallet);

        navigateUnknownButton.setOnClickListener(v -> BRouter.getInstance().path("随便一个url").navigate());

        navigateWalletButton.setOnClickListener(v -> BRouter.getInstance().path(WalletRouteUrl.Activity.MAIN).navigate());

        navigateLoginButton.setOnClickListener(v -> BRouter.getInstance().path(LoginRouteUrl.Activity.MAIN).navigate());

        navigateWalletAfterLoginButton.setOnClickListener(v -> {
            IUserService userService = ServiceHelper.getService(IUserService.NAME);
            User user = userService.getUser();
            Bundle bundle = new Bundle();
            bundle.putLong("userId", user.getUserId());
            bundle.putString("username", user.getUserName());
            BRouter.getInstance().path(WalletRouteUrl.Activity.MAIN).params(bundle).navigate();
        });


    }
}
