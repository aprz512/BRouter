package com.aprz.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aprz.brouter.api.fragment.FragmentHelper;
import com.aprz.card.sdk.CardRouteUrl;
import com.aprz.home.R;

/**
 * 测试获取其他组件的 Fragment
 */
public class FragmentRouteFragment extends BaseViewPagerFragment {

    @Override
    protected int getFragmentContentId() {
        return R.layout.home_fragment_fragment_route;
    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.btn_fragment).setOnClickListener(v -> {
            Fragment fragment = FragmentHelper.getFragment(CardRouteUrl.Fragment.PREVIEW);
            FragmentManager fragmentManager = getChildFragmentManager();
            if (fragmentManager.findFragmentByTag(fragment.getClass().getCanonicalName()) == null) {
                view.findViewById(R.id.fragment_content).setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.transparent));
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_content, fragment, fragment.getClass().getCanonicalName());
                fragmentTransaction.commitNowAllowingStateLoss();
            }
        });
    }

}
