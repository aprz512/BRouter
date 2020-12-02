package com.example.component_api;

public class ComponentUtil {
    /**
     * 1.这是注解驱动器生成类的时候的目录
     */
    public static final String IMPL_OUTPUT_PKG = "com.aprz.component_impl";
    /**
     * 点
     */
    public static final String DOT = ".";

    /**
     * 生成的文件名称的后缀
     */
    public static final String COMPONENT_APPLICATION_SUFFIX = "ComponentApplicationGenerated";
    /**
     * 生成的文件名称的后缀
     */
    public static final String Fragment = "FragmentGenerated";

    /**
     * 生成的文件名称的后缀
     */
    public static final String SERVICE = "ServiceGenerated";

    public static final String COMPONENT_APPLICATION_IMPL_CLASS_NAME = IMPL_OUTPUT_PKG  + DOT + "ComponentApplicationImpl";
    public static final String FRAGMENT_IMPL_CLASS_NAME = IMPL_OUTPUT_PKG + DOT + "fragment" + DOT + "ComponentFragmentImpl";;
    public static final String SERVICE_IMPL_CLASS_NAME = IMPL_OUTPUT_PKG + DOT + "service" + DOT + "IComponentServiceDefaultImpl";

    public static String genHostModuleApplicationClassName(String host) {
        return IMPL_OUTPUT_PKG + DOT + "application" + DOT + firstCharUpperCase(host) + COMPONENT_APPLICATION_SUFFIX;
    }

    public static String genHostFragmentClassName(String host) {
        return IMPL_OUTPUT_PKG + DOT + "fragment" + DOT + firstCharUpperCase(host) + Fragment;
    }

    public static String genHostServiceClassName(String host) {
        return IMPL_OUTPUT_PKG + DOT + "service" + DOT + firstCharUpperCase(host) + SERVICE;
    }

    /**
     * 首字母小写
     */
    public static String firstCharUpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
