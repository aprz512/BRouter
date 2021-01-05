package com.aprz.wallet;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aprz.brouter.annotation.Interceptor;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.brouter.api.core.Navigation;
import com.aprz.brouter.api.interceptor.IRouteInterceptor;

/**
 * 该拦截器用来监测，跳转到 wallet 时，传递的参数是否合规
 */
@Interceptor(path = Constants.RoutePath.WALLET_ACTIVITY)
public class WalletParamsInterceptor implements IRouteInterceptor {

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public void intercept(@NonNull Chain chain, @NonNull Callback callback) {
        Navigation navigate = chain.navigate();
        // 因为注解里面指定了path，不符合的不会走这个拦截器
        Bundle params = navigate.getParams();
        if (params != null && params.getLong("userId") > 0) {
            chain.proceed(navigate);
        } else {
            chain.interrupt(new IllegalArgumentException("参数不正确，userId 不合法!!! "));
            if (params == null) {
                Toast.makeText(BRouter.context(), "没有传递参数不正确，不合法!!!", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(BRouter.context(), "参数不正确，userId 不合法!!!" + params.getLong("userId"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
