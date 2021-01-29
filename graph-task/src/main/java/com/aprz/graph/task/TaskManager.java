package com.aprz.graph.task;

import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * 暂时没有支持多进程
 */
public class TaskManager {

    private static TaskManager instance = null;
    private GraphTask graphTask;
    private volatile boolean isGraphTaskFinished = false;
    private static final byte[] waitFinishLock = new byte[0];

    private TaskManager() {
    }

    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;
    }

    public TaskManager addGraphTask(@NonNull GraphTask graphTask) {
        // reset
        isGraphTaskFinished = false;
        this.graphTask = graphTask;
        addListeners();
        return this;
    }

    public TaskManager start() {
        if (graphTask != null) {
            graphTask.start();
        } else {
            throw new IllegalStateException("graphTask is null !!!");
        }
        return this;
    }

    private void addListeners() {
        graphTask.addLifecycleListener(new GraphTaskLifecycleListener() {
            @Override
            public void onFinish() {
                isGraphTaskFinished = true;
                releaseWaitFinishLock();
            }

            @Override
            public void onTaskDispatched(Task task) {
                if (task.isRunInUiThread()) {
                    LogUtils.d("运行在主线程的任务 --> " + task.name + " <-- 被分派了，通知 waitUntilFinish 的阻塞线程去释放锁");
                    releaseWaitFinishLock();
                }
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
            while (!isGraphTaskFinished) {
                try {
                    waitFinishLock.wait();
                } catch (InterruptedException e) {
                    LogUtils.w(e);
                }
                LogUtils.d("waitUntilFinish 的阻塞线程被唤醒了，去执行主线程的工作");
                Dispatcher.getInstance().runUiThreadTask();
            }
        }
    }

}
