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
/*        // 原始输入
        final File dirInput = directoryInput.getFile();

        // input 不存在，需要删除对应的 output
        if (!dirInput.exists() && dirOutput.exists()) {
            if (dirOutput.isDirectory()) {
                FileUtils.deleteFolder(dirOutput);
            } else {
                FileUtils.delete(dirOutput);
            }
        }

        // https://xsfelvis.github.io/2019/05/02/gradle%E6%8F%92%E4%BB%B6%E4%B9%8BTransform/

        if (isIncremental) {
            Map<File, Status> fileStatusMap = directoryInput.getChangedFiles();
            final Map<File, Status> outChangedFiles = new HashMap<>();

            for (Map.Entry<File, Status> entry : fileStatusMap.entrySet()) {
                final Status status = entry.getValue();
                final File changedFileInput = entry.getKey();


                // 处理文件变化
                if (status == Status.ADDED || status == Status.CHANGED) {
                    map.put(changedFileInput, changedFileInput);
                } else if (status == Status.REMOVED) {
                    changedFileInput.delete();
                }

                outChangedFiles.put(changedFileOutput, status);
            }

            // 使用反射 替换directoryInput的  改动文件目录
            replaceChangedFile(directoryInput, outChangedFiles);

        } else {
            // 全量编译模式下，所有的Class文件都需要扫描
            dirInputOutMap.put(dirInput, dirOutput);
        }*/
    }
}
