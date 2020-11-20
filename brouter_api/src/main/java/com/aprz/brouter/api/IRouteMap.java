package com.aprz.brouter.api;

import com.aprz.brouter.api.core.Navigation;

import java.util.Map;

public interface IRouteMap {

    void loadMap(Map<String, Navigation> routeMap);

}
