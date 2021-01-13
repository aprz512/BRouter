package com.aprz.brouter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aprz.brouter.fragment.InterceptorFragment;
import com.aprz.brouter.fragment.NavigationFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        List<TabPair> pairList = new ArrayList<>();
        pairList.add(new TabPair(tabLayout.newTab().setText("组件跳转"), new NavigationFragment()));
        pairList.add(new TabPair(tabLayout.newTab().setText("拦截器"), new InterceptorFragment()));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
        });

        tabLayout.setupWithViewPager(viewPager);

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