package com.aprz.brouter.plugin;

import com.android.build.api.transform.Status;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class JarClassCollector implements Runnable {

    private ConcurrentHashMap<File, File> map;
    private File source;
    private File dest;
    private Status status;
    private boolean isIncremental;

    public JarClassCollector(ConcurrentHashMap<File, File> map, File file, File dest, Status status, boolean isIncremental) {
        this.map = map;
        this.source = file;
        this.dest = dest;
        this.status = status;
        this.isIncremental = isIncremental;
    }

    @Override
    public void run() {

    }

    private void transformJar(File source, File dest, Status status) {

    }

}
