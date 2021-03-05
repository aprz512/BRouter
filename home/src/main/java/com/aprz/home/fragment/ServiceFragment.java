package com.aprz.home.fragment;

import android.view.View;
import android.widget.TextView;

import com.aprz.base.ext.ObserverExt;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.service.ServiceHelper;
import com.aprz.home.R;
import com.aprz.login.sdk.IUserService;
import com.aprz.login.sdk.LoginRouteUrl;
import com.aprz.login.sdk.User;

/**
 * 测试组件服务
 */
public class ServiceFragment extends BaseViewPagerFragment {

    @Override
    protected int getFragmentContentId() {
        return R.layout.home_fragment_service;
    }

    @Override
    protected void initView(View view) {

        TextView userName = view.findViewById(R.id.username);
        TextView userId = view.findViewById(R.id.user_id);

        IUserService userService = ServiceHelper.getService(IUserService.NAME);

        // fix memory leak
        userService.getUserStream().observe(getViewLifecycleOwner(), (ObserverExt<User>) user -> {
            userName.setText(user.getUserName());
            userId.setText(String.valueOf(user.getUserId()));
        });

        view.findViewById(R.id.login)
                .setOnClickListener(v -> BRouter.getInstance().path(LoginRouteUrl.Activity.MAIN).navigate());

    }
}
