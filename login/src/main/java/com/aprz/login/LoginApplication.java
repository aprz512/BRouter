package com.aprz.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.aprz.brouter.annotation.ComponentAppAnno;
import com.aprz.component_impl.IComponentLifecycle;

import service.AppService;

/**
 * 组件中用户和控制中心打交道的类
 * <p>
 * 问题1：该模块的实例由谁来创建?
 * 问题2：如何正确找到每个组件中的这种类(确保唯一性和正确性)   用注解
 * <p>
 * 通过注解来扫描，找到每个模块中的类似于stub的类
 * <p>
 * 模块被加载意味着什么？
 * <p>
 * 1.注解处理器会生成一个xxApplicationGenerated的实例
 * 2.该实例会创建并持有LoginApplication的实例引用
 * 3.调用Application的onCreate()
 * 4.辅助类会主动注册模块中相关的服务，如{@link com.example.module_login_export.service.UserService}
 * <p>
 * 此时，模块中的服务已经注册，其他客户端可以通过服务中心去获取
 */
@ComponentAppAnno
public class LoginApplication implements IComponentLifecycle {
    @Override
    public void onCreate(@NonNull Application app) {
        Log.i("LoginApplication", "onCreate");
        /**
         * 得知自己被加载后，可以获取一些主程序的信息，比如版本信息，包名等等
         */
        AppService.getInstance().initContext(app.getApplicationContext());
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
