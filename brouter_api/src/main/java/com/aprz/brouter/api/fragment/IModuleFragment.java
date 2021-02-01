package com.aprz.brouter.api.fragment;

import androidx.fragment.app.Fragment;

import java.util.Map;

public interface IModuleFragment {

    Map<String, Class<? extends Fragment>> fragments();

}
