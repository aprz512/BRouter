package com.aprz.wallet;

import androidx.annotation.NonNull;

import com.aprz.brouter.annotation.Degrade;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.core.Navigation;
import com.aprz.brouter.api.degrade.IRouteDegrade;
import com.aprz.wallet.sdk.WalletRouteUrl;

@Degrade
public class WalletDegrade implements IRouteDegrade {

    @Override
    public boolean isMatch(@NonNull Navigation navigation) {
        // 当新的 activity crash 次数超过限制了之后，需要降级
        String targetPage = WalletRouteUrl.Activity.MAIN;

        return CrashMonitor.needDegrade(targetPage)
                && targetPage.equals(navigation.getPath());
    }

    @Override
    public void handleDegrade(@NonNull Navigation navigation) {
        // 降级，跳转到老的页面
        BRouter.getInstance()
                .path(WalletRouteUrl.Activity.OLD_MAIN)
                .params(navigation.getParams())
                .navigate();
    }

}
