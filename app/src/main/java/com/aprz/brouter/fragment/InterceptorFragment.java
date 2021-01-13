package com.aprz.brouter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.R;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.service.ServiceHelper;
import com.aprz.login.sdk.IUserService;
import com.aprz.login.sdk.LoginRouteUrl;
import com.aprz.login.sdk.User;
import com.aprz.wallet.sdk.WalletRouteUrl;

public class InterceptorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.app_fragment_interceptor, container, false);


        Button navigateUnknownButton = root.findViewById(R.id.navigate_to_unknown);
        Button navigateLoginButton = root.findViewById(R.id.navigate_to_login);
        Button navigateWalletButton = root.findViewById(R.id.navigate_to_wallet_n);
        Button navigateWalletAfterLoginButton = root.findViewById(R.id.navigate_to_login_then_wallet);

        navigateUnknownButton.setOnClickListener(v -> BRouter.getInstance().path("随便一个url").navigate());

        navigateWalletButton.setOnClickListener(v -> BRouter.getInstance().path(WalletRouteUrl.WALLET_ACTIVITY).navigate());

        navigateLoginButton.setOnClickListener(v -> BRouter.getInstance().path(LoginRouteUrl.LOGIN_ACTIVITY).navigate());

        navigateWalletAfterLoginButton.setOnClickListener(v -> {
            IUserService userService = ServiceHelper.getService(IUserService.NAME);
            User user = userService.getUserInfo();
            Bundle bundle = new Bundle();
            bundle.putLong("userId", user.getUserId());
            bundle.putString("username", user.getUserName());
            BRouter.getInstance().path(WalletRouteUrl.WALLET_ACTIVITY).params(bundle).navigate();
        });


        return root;
    }
}
