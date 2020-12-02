package com.example.component_api;

/**
 * 用户保存组件化相关的常量
 */
public interface ComponentConstants {
    //生命周期接口
    String APPLICATION_LIFECYCLE_INTERFACE_CLASS_NAME = "com.aprz.component_impl.ILifecycle";

    public static final String ANDROID_APPLICATION = "android.app.Application";
    public static final String JAVA_EXCEPTION = "java.lang.Exception";
    public static final String JAVA_STRING = "java.lang.String";
    public static final String JAVA_INTEGER = "java.lang.Integer";
    public static final String JAVA_MAP = "java.util.Map";
    public static final String JAVA_LIST = "java.util.List";
    public static final String JAVA_COLLECTIONS = "java.util.Collections";
    public static final String JAVA_ARRAYLIST = "java.util.ArrayList";
    public static final String JAVA_HASHMAP = "java.util.HashMap";
    public static final String JAVA_HASHSET = "java.util.HashSet";
    public static final String ANDROID_BUNDLE = "android.os.Bundle";

    public static final String COMPONENT_MANAGER_CALL_CLASS_NAME = "com.aprz.component_impl.ComponentManager";
    public static final String FRAGMENT_MANAGER_CALL_CLASS_NAME = "com.aprz.component_impl.fragment.FragmentManager";
    public static final String SERVICE_MANAGER_CALL_CLASS_NAME = "com.aprz.component_impl.service.ServiceManager";
    public static final String CALLABLE_CLASS_NAME = "com.aprz.component_impl.Callable";
    public static final String SINGLETON_CALLABLE_CLASS_NAME = "com.aprz.component_impl.SingletonCallable";

    public static final String CENTER_SERVICE_CLASS_NAME = "com.aprz.component_impl.service.ServiceCenter";

    public static final String FUNCTION_CLASS_NAME = "com.example.component_api.Function";
}
