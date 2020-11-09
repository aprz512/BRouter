package com.aprz.brouter.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Status;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DirectoryClassCollector implements Runnable {

   private ConcurrentHashMap<File, File> map;
    private  DirectoryInput directoryInput;

    public DirectoryClassCollector(ConcurrentHashMap<File, File> map, DirectoryInput directoryInput) {
        this.directoryInput = directoryInput;
        this.map = map;
    }

    @Override
    public void run() {

    }
}
