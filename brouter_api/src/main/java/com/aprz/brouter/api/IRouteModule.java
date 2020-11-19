package com.aprz.brouter.api;

import com.aprz.brouter.api.IRouteMap;

import java.util.List;
import java.util.Map;

public interface IRouteModule {
    void loadModule(Map<String, IRouteMap> moduleMap);
}
