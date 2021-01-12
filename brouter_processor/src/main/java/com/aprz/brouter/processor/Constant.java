package com.aprz.brouter.processor;

public class Constant {

    public static final String KEY_MODULE_NAME = "BROUTER_MODULE_NAME";

    public static final String APPLICATION = "android.app.Application";
    public static final String ACTIVITY = "android.app.Activity";
    public static final String ARRAY_LIST = "java.util.ArrayList";
    public static final String NAVIGATION = "com.aprz.brouter.api.core.Navigation";


    public static final String INTERCEPTOR_PACKAGE_NAME = "com.aprz.brouter.interceptors";
    public static final String INTERCEPTOR_CLASS_PREFIX = "BRouter$$Interceptor$$";
    public static final String INTERCEPTOR_MODULE = "com.aprz.brouter.api.interceptor.IModuleInterceptor";
    public static final String INTERCEPTOR_ROUTE = "com.aprz.brouter.api.interceptor.IRouteInterceptor";
    public static final String INTERCEPTOR = "com.aprz.brouter.annotation.Interceptor";
    public static final String INTERCEPTOR_HELPER = "com.aprz.brouter.api.interceptor.InterceptorHelper";


    public static final String MODULE_PACKAGE_NAME = "com.aprz.brouter.module";
    public static final String MODULE_CLASS_NAME_PREFIX = "BRouter$$Module$$";
    public static final String MODULE = "com.aprz.brouter.api.module.IModule";

    public static final String DEGRADE_PACKAGE_NAME = "com.aprz.brouter.degrades";
    public static final String DEGRADE_CLASS_PREFIX = "BRouter$$Degrade$$";
    public static final String DEGRADE_MODULE = "com.aprz.brouter.api.degrade.IModuleDegrade";
    public static final String DEGRADE_ROUTE = "com.aprz.brouter.api.degrade.IRouteDegrade";
    public static final String DEGRADE_HELPER = "com.aprz.brouter.api.degrade.DegradeHelper";


    public static final String SERVICE_PACKAGE_NAME = "com.aprz.brouter.services";
    public static final String SERVICE_CLASS_PREFIX = "BRouter$$Service$$";
    public static final String SERVICE_MODULE = "com.aprz.brouter.api.service.IModuleService";
    public static final String SERVICE_HELPER = "com.aprz.brouter.api.service.ServiceHelper";


}
