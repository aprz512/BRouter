package com.aprz.base.loading;

import androidx.fragment.app.FragmentManager;

public class Loading {

    public static void show(FragmentManager fragmentManager) {
        LoadingFragment fragment = new LoadingFragment();
        fragment.showNow(fragmentManager, LoadingFragment.class.getCanonicalName());
    }

    public static void dismiss(FragmentManager fragmentManager) {
        LoadingFragment fragment = (LoadingFragment) fragmentManager.findFragmentByTag(LoadingFragment.class.getCanonicalName());
        if (fragment != null) {
            fragment.dismissAllowingStateLoss();
        }
    }

}
