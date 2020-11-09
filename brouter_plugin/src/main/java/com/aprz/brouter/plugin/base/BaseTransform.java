package com.aprz.brouter.plugin.base;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/9
 * <p>
 * Class desc: transform 封装了增量与并发
 */
public abstract class BaseTransform extends Transform {

    private BaseWeaver weaver;
    /**
     * 尽量的敲诈电脑的资源
     */
    private ExecutorService executor = Executors.newFixedThreadPool(16);

    public BaseTransform(BaseWeaver weaver) {
        this.weaver = weaver;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        // 是否增量编译
        final boolean isIncremental = transformInvocation.isIncremental() && this.isIncremental();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        //如果非增量，则清空旧的输出内容
        if (!isIncremental) {
            outputProvider.deleteAll();
        }


        Collection<TransformInput> transformInputs = transformInvocation.getInputs();


        for (TransformInput transformInput : transformInputs) {
            for (DirectoryInput directoryInput : transformInput.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);
                FileUtils.forceMkdir(dest);

                if (isIncremental) {
                    String srcDirPath = directoryInput.getFile().getAbsolutePath();
                    String destDirPath = dest.getAbsolutePath();
                    Map<File, Status> fileStatusMap = directoryInput.getChangedFiles();
                    for (Map.Entry<File, Status> changedFile : fileStatusMap.entrySet()) {
                        Status status = changedFile.getValue();
                        File inputFile = changedFile.getKey();
                        String destFilePath = inputFile.getAbsolutePath().replace(srcDirPath, destDirPath);
                        File destFile = new File(destFilePath);
                        switch (status) {
                            case NOTCHANGED:
                                // do nothing
                                break;
                            case REMOVED:
                                if (destFile.exists()) {
                                    //noinspection ResultOfMethodCallIgnored
                                    destFile.delete();
                                }
                                break;
                            case ADDED:
                            case CHANGED:
                                try {
                                    FileUtils.touch(destFile);
                                } catch (IOException e) {
                                    //maybe mkdirs fail for some strange reason, try again.
                                    FileUtils.forceMkdirParent(destFile);
                                }
                                transformFile(inputFile, destFile, srcDirPath);
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    transformDir(directoryInput.getFile(), dest);
                }

            }
            for (JarInput jarInput : transformInput.getJarInputs()) {
                Status status = jarInput.getStatus();
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);

                if (isIncremental) {
                    switch (status) {
                        case NOTCHANGED:
                            // do nothing
                            break;
                        case ADDED:
                        case CHANGED:
                            transformJar(jarInput.getFile(), dest);
                            break;
                        case REMOVED:
                            if (dest.exists()) {
                                FileUtils.forceDelete(dest);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    transformJar(jarInput.getFile(), dest);
                }
            }
        }

    }

    protected void transformJar(final File file, final File dest) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    weaver.weaveJar(file, dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    protected void transformFile(final File inputFile, final File outputFile, final String inputDir) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    weaver.weaveClass(inputFile, outputFile, inputDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void transformDir(final File inputDir, final File outputDir) {
        final String inputDirPath = inputDir.getAbsolutePath();
        final String outputDirPath = outputDir.getAbsolutePath();
        if (inputDir.isDirectory()) {
            for (final File file : com.android.utils.FileUtils.getAllFiles(inputDir)) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String filePath = file.getAbsolutePath();
                            File dest = new File(filePath.replace(inputDirPath, outputDirPath));
                            transformFile(file, dest, inputDirPath);
                            weaver.weaveClass(file, dest, inputDirPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
