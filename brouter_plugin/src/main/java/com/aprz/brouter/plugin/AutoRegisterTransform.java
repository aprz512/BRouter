package com.aprz.brouter.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/4
 * <p>
 * Class desc: 实现一个 transform， 支持增量，支持多线程
 */
public class AutoRegisterTransform extends Transform {

    private static final String TAG = "BRouterAutoRegisterTransform";
    private ExecutorService executor = Executors.newFixedThreadPool(16);

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        long start = System.currentTimeMillis();
        try {
            doTransform(transformInvocation);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // 计算耗时
        long cost = System.currentTimeMillis() - start;
    }

    private void doTransform(TransformInvocation transformInvocation) throws ExecutionException, InterruptedException {
        // 是否增量编译
        final boolean isIncremental = transformInvocation.isIncremental() && this.isIncremental();

        /*
         * 这个 transform 要做的事情
         * 1. 找到所有包名为 com.aprz.brouter.routes 的类，记录下来，这里采用线程池来做这个事情
         */
        List<Future> futures = new LinkedList<>();
        // 需要一个同步集合，所以用了 ConcurrentHashMap，其实可以用 Vector 同步，不知道效率是否会高点
        ConcurrentHashMap<File, File> map = new ConcurrentHashMap<>();
        Collection<TransformInput> inputs = transformInvocation.getInputs();

        for (TransformInput input : inputs) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                futures.add(executor.submit(new DirectoryClassCollector(map, directoryInput)));
            }
            for (JarInput jarInput : input.getJarInputs()) {
                futures.add(executor.submit(new JarClassCollector(map, jarInput)));
            }
        }

        // 等待线程池的任务做完
        for (Future f : futures) {
            f.get();
        }
        futures.clear();


    }
}
