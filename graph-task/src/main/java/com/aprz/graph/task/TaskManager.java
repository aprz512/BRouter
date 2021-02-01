package com.aprz.graph.task;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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
        // 为子 graphTask 添加监听
        List<Task> allTask = new ArrayList<>();
        dfs(allTask, graphTask);
        for (Task task : allTask) {
            LogUtils.d("打印 dfs 任务： " + task.name);
            if (task instanceof GraphTask) {
                LogUtils.d("找到了 GraphTask 任务： " + task.name);
                ((GraphTask) task).addLifecycleListener(new GraphTaskLifecycleListener() {
                    @Override
                    public void onTaskDispatched(Task task) {
                        if (task.isRunInUiThread()) {
                            LogUtils.d("运行在主线程的任务 --> " + task.name + " <-- 被分派了，通知 waitUntilFinish 的阻塞线程去释放锁");
                            releaseWaitFinishLock();
                        }
                    }
                });
            }
        }

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

    private void dfs(List<Task> collect, Task root) {
        for (Task task : root.getSuccessorList()) {
            if (!collect.contains(task)) {
                collect.add(task);
            }
            dfs(collect, task);
        }
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
                if (isGraphTaskFinished) {
                    LogUtils.d("waitUntilFinish 的阻塞线程被唤醒了，任务图执行完了，线程继续往下执行");
                } else {
                    LogUtils.d("waitUntilFinish 的阻塞线程被唤醒了，去执行 Task");
                    Dispatcher.getInstance().runUiThreadTask();
                }
            }
        }
    }

}
