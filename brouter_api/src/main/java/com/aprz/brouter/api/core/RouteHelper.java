package com.aprz.brouter.api.core;

import android.content.Context;
import android.content.pm.PackageManager;

import com.aprz.brouter.api.IRouteMap;
import com.aprz.brouter.api.IRouteModule;
import com.aprz.brouter.api.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class RouteHelper {

    public static void loadRoute(Context context) {
        try {
            // 找到指定包名下的所有类
            Set<String> fileNameByPackageName = ClassUtils.getFileNameByPackageName(context, "com.aprz.brouter.routes");
            for (String className : fileNameByPackageName) {
                if(className.startsWith("com.aprz.brouter.routes.BRouter$$RouteModule")) {
                    IRouteModule module  = (IRouteModule) Class.forName(className).getConstructor().newInstance();
                    RouteStore.injectModule(module);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void injectRouteByPlugin() {
        // 在这个方法里面生成下面这样的字节码
        // register(new BRouter$$Group$$xxx());
        // register(new BRouter$$Group$$yyy());
        // register(new BRouter$$Group$$zzz());
    }

    /**
     * 纯粹是为了简化 asm 的编写
     */
    private static void register(IRouteMap group) {
        group.loadMap(RouteStore.getRouteMap());
    }

}
