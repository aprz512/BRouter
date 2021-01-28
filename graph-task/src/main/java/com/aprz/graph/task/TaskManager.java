package com.aprz.graph.task;

import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * 暂时没有支持多进程
 */
public class TaskManager {

    private static TaskManager instance = null;
    private GraphTask graphTask;
    private volatile boolean isStartupFinished = false;
    private static final byte[] waitFinishLock = new byte[0];

    private TaskManager() {
    }

    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;
    }

    public void addGraphTask(@NonNull GraphTask graphTask) {
        this.graphTask = graphTask;
        addListeners();
    }

    public void start() {
        if (graphTask != null) {
            graphTask.start();
        } else {
            LogUtils.d("You should add task first!!!");
        }
    }

    private void addListeners() {
        graphTask.addTaskLifecycleListener(new Task.TaskLifecycleListener() {
            @Override
            public void onTaskFinish(Task task) {
                isStartupFinished = true;
                releaseWaitFinishLock();
            }
        });
    }

    private void releaseWaitFinishLock() {
        synchronized (waitFinishLock) {
            waitFinishLock.notifyAll();
        }
    }

    /**
     * <p>阻塞当前线程，直到初始化任务完成。</p>
     */
    public void waitUntilFinish() {
        synchronized (waitFinishLock) {
            while (!isStartupFinished) {
                try {
                    waitFinishLock.wait();
                } catch (InterruptedException e) {
                    LogUtils.w(e);
                }
            }
        }
    }

    private boolean isMainThread(Thread thread) {
        return thread == Looper.getMainLooper().getThread();
    }


}
