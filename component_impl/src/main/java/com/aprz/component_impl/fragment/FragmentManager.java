package com.aprz.component_impl.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.annotation.FragmentAnno;
import com.aprz.component_impl.IComponentLifecycle;
import com.example.component_api.Function;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FragmentManager {
    /**
     * Service 的集合
     */
    private static Map<String, Function<Bundle, ? extends Fragment>> map =
            Collections.synchronizedMap(new HashMap<String, Function<Bundle, ? extends Fragment>>());

    /**
     * 你可以注册一个服务,服务的初始化可以是 懒加载的
     *
     * @param flag     用 {@link FragmentAnno} 标记 {@link Fragment} 的字符串
     * @param function function
     */
    @AnyThread
    public static void register(@NonNull String flag,
                                @NonNull Function<Bundle, ? extends Fragment> function) {
        map.put(flag, function);
    }

    @Nullable
    @AnyThread
    public static void unregister(@NonNull String flag) {
        map.remove(flag);
    }

    @AnyThread
    public static Fragment get(@NonNull final String flag) {
        return get(flag, null);
    }

    /**
     * @param flag
     * @param bundle
     * @return 注意返回的Fragment要和LoginFragment继承的一直（androidx）
     */
    @Nullable
    @AnyThread
    public static Fragment get(@NonNull final String flag, @Nullable final Bundle bundle) {
        Function<Bundle, ? extends Fragment> function = map.get(flag);
        if (function == null) {
            return null;
        } else {
            try {
                return function.apply(bundle);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
