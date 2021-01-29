package com.aprz.brouter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aprz.base.activity.BaseActivity;
import com.aprz.brouter.fragment.DegradeFragment;
import com.aprz.brouter.fragment.GraphTaskFragment;
import com.aprz.brouter.fragment.InterceptorFragment;
import com.aprz.brouter.fragment.NavigationFragment;
import com.aprz.brouter.fragment.ServiceFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        List<TabPair> pairList = new ArrayList<>();
        pairList.add(new TabPair(tabLayout.newTab().setText("组件跳转"), new NavigationFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("拦截器"), new InterceptorFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("降级策略"), new DegradeFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("组件对外提供服务"), new ServiceFragment()));
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