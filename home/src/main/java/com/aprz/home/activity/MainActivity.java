package com.aprz.home.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aprz.base.activity.BaseActivity;
import com.aprz.brouter.annotation.Route;
import com.aprz.home.R;
import com.aprz.home.fragment.DegradeFragment;
import com.aprz.home.fragment.FragmentRouteFragment;
import com.aprz.home.fragment.GraphTaskFragment;
import com.aprz.home.fragment.InterceptorFragment;
import com.aprz.home.fragment.NavigationFragment;
import com.aprz.home.fragment.ServiceFragment;
import com.aprz.home.sdk.HomeRouteUrl;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = HomeRouteUrl.Activity.MAIN)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        List<TabPair> pairList = new ArrayList<>();
        pairList.add(new TabPair(tabLayout.newTab().setText("组件跳转"), new NavigationFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("拦截器"), new InterceptorFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("降级策略"), new DegradeFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("组件对外提供服务"), new ServiceFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("获取组件Fragment"), new FragmentRouteFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("测试 GraphTask 库"), new GraphTaskFragment()));

        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), pairList));
        tabLayout.setupWithViewPager(viewPager);
    }

    static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        private final List<TabPair> pairList;

        public TabFragmentPagerAdapter(@NonNull FragmentManager fm, List<TabPair> pairList) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.pairList = pairList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return pairList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pairList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pairList.get(position).getTab().getText();
        }
    }

    static class TabPair {
        TabLayout.Tab tab;
        Fragment fragment;

        public TabPair(TabLayout.Tab tab, Fragment fragment) {
            this.tab = tab;
            this.fragment = fragment;
        }

        public TabLayout.Tab getTab() {
            return tab;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }
}