package com.aprz.brouter_api;

import android.app.Activity;

import java.util.Map;

public interface IRouteGroup {

    void loadInto(Map<String, Class<? extends Activity>> routeMap);

}
