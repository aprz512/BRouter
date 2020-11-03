package com.aprz.brouter_api.core;

import android.content.Context;
import android.content.pm.PackageManager;

import com.aprz.brouter_api.IRouteGroup;
import com.aprz.brouter_api.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class RouteHelper {

    public static void loadRoute(Context context) {
        try {
            // 找到指定报名下的所有类
            Set<String> fileNameByPackageName = ClassUtils.getFileNameByPackageName(context, "com.aprz.brouter.routes");
            for (String className : fileNameByPackageName) {
                ((IRouteGroup) (Class.forName(className).getConstructor().newInstance())).loadInto(RouteStore.getRouteMap());
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

}
