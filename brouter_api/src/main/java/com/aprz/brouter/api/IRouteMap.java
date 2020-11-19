package com.aprz.brouter.api;

import android.app.Activity;

import java.util.Map;

public interface IRouteMap {

    void loadMap(Map<String, Class<? extends Activity>> routeMap);

}
