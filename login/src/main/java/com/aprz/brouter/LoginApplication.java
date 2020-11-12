package com.aprz.brouter;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.aprz.brouter.annotation.ComponentAppAnno;
import com.aprz.component_impl.IComponentLifecycle;

/**
 * 组件中用户和控制中心打交道的类
 * <p>
 * 问题1：该模块的实例由谁来创建?
 * 问题2：如何正确找到每个组件中的这种类(确保唯一性和正确性)   用注解
 * <p>
 * 通过注解来扫描，找到每个模块中的类似于stub的类
 */
@ComponentAppAnno
public class LoginApplication implements IComponentLifecycle {
    @Override
    public void onCreate(@NonNull Application app) {
        Log.i("LoginApplication", "onCreate");
        /**
         * 得知自己被加载后，可以获取一些主程序的信息，比如版本信息，包名等等
         */

    }

    @Override
    public void onDestroy() {
        Log.i("LoginApplication", "onDestroy");
    }

    @Override
    public String getName() {
        return "login";
    }
}
