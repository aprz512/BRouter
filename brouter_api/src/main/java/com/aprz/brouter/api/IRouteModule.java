package com.aprz.brouter.api;

import com.aprz.brouter.api.IRouteMap;

import java.util.List;

public interface IRouteModule {
    void loadModule(List<IRouteMap> moduleList);
}
