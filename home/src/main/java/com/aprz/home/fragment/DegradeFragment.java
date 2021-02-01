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
public class DegradeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment_degrade, container, false);

        Button testWalletDegradeButton = root.findViewById(R.id.test_wallet_degrade);
        testWalletDegradeButton.setOnClickListener(v-> {
            Bundle bundle = new Bundle();
            bundle.putInt("count", 10);
            bundle.putString("message", "hello brouter");
            bundle.putLong("userId", 1L);
            BRouter.getInstance().path(WalletRouteUrl.Activity.MAIN).params(bundle).navigate();
        });

        return root;
    }
}
