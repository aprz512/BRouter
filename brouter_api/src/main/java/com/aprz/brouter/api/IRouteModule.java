package com.aprz.brouter.api;

import java.util.Map;

public interface IRouteModule {
    void loadModule(Map<String, IRouteMap> moduleMap);
}
