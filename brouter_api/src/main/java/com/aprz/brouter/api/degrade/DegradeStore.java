package com.aprz.brouter.api.degrade;

import java.util.ArrayList;
import java.util.List;

public class DegradeStore {

    private static List<IRouteDegrade> degradeList = new ArrayList<>();

    public static List<IRouteDegrade> getDegradeList() {
        return new ArrayList<>(degradeList);
    }

    public static void addRouteDegrade(IRouteDegrade degrade) {
        degradeList.add(degrade);
    }

}
