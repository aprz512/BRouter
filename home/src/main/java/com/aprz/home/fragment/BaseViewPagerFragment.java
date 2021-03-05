package com.aprz.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.base.inflater.AsyncLayoutInflater;
import com.aprz.home.R;

public abstract class BaseViewPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_view_page_stub, container, false);
        // 异步加载布局试试
        // 发现每次加载布局都很耗时，可能是 material 的问题？？？
        new AsyncLayoutInflater(getActivity(), inflater)
                .inflate(getFragmentContentId(), (ViewGroup) root, (view, resId, parent) -> {
                    parent.addView(view);
                    initView(view);
                });

        return root;
    }

    @LayoutRes
    protected abstract int getFragmentContentId();

    protected abstract void initView(View view);
}
