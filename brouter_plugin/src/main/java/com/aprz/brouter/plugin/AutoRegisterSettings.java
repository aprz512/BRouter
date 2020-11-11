package com.aprz.brouter.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AutoRegisterSettings {

    public static final String API = "com/aprz/brouter/api/core/RouteHelper.class";
    public static File injectFile;
    public static RouteGroup routeGroup;

    public static class RouteGroup {
        public List<String> classList = new ArrayList<>();
    }

}
