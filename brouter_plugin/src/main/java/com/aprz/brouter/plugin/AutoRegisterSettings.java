package com.aprz.brouter.plugin;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class AutoRegisterSettings {

    public static final String ROUTE_GROUP_PACKAGE = "com/aprz/brouter/routes/";
    public static final String ROUTE_HELPER_CLASS = "com/aprz/brouter/api/core/RouteHelper.class";
    public static File injectFile;
    public static RouteGroup routeGroup;

    public static class RouteGroup {
        public List<String> classList = new Vector<>();

        public void addClass(String className) {
            if (!classList.contains(className)) {
                classList.add(className);
            }
        }
    }

}
