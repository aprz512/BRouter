package com.aprz.brouter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.base.ext.ObserverExt;
import com.aprz.brouter.R;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.service.ServiceHelper;
import com.aprz.login.sdk.IUserService;
import com.aprz.login.sdk.LoginRouteUrl;
import com.aprz.login.sdk.User;

public class ServiceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.app_fragment_service, container, false);

        TextView userName = root.findViewById(R.id.username);
        TextView userId = root.findViewById(R.id.user_id);

        IUserService userService = ServiceHelper.getService(IUserService.NAME);

        userService.getUserStream().observe(this, (ObserverExt<User>) user -> {
            userName.setText(user.getUserName());
            userId.setText(String.valueOf(user.getUserId()));
        });

        root.findViewById(R.id.login)
                .setOnClickListener(v -> BRouter.getInstance().path(LoginRouteUrl.LOGIN_ACTIVITY).navigate());

        return root;
    }
}
