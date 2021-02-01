package com.aprz.graph.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.UiThread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Dispatcher {

    private static final ThreadPoolExecutor taskExecutor;

    private final LinkedBlockingQueue<Task> uiThreadTaskQueue = new LinkedBlockingQueue<>();

    private static final Dispatcher dispatcher = new Dispatcher();

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        taskExecutor = new ThreadPoolExecutor(processors, processors,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger mCount = new AtomicInteger(1);

                    public Thread newThread(Runnable r) {
                        return new Thread(r, "GraphTask Thread #" + mCount.getAndIncrement());
                    }
                });
        taskExecutor.allowCoreThreadTimeOut(true);
    }

    private Dispatcher() {
    }

    public static Dispatcher getInstance() {
        return dispatcher;
    }

    public void init() {
        uiThreadTaskQueue.clear();
    }

    public void dispatch(Task task) {
        // 先将要在主线程运行的 task 都储存起来
        if (task.isRunInUiThread()) {
            LogUtils.d("将 " + task.name + "放入集合中");
            uiThreadTaskQueue.add(task);
        } else {
            taskExecutor.execute(task);
        }
    }

    @UiThread
    public void runUiThreadTask() {
        while (uiThreadTaskQueue.size() > 0) {
            Task task = uiThreadTaskQueue.poll();
            assert task != null;

            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                task.run();
            } else {
                new Handler(Looper.getMainLooper()).post(task);
            }
        }
    }

}
