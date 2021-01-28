package com.aprz.graph.task;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 表示一个任务
 * <p>这个类将一个个关联的{@link Task}，组织成PERT网路图的方式进行执行。可以通过{@link GraphTask.Builder}
 * 将{@link Task}组装成完整的{@code GraphTask}，该{@code GraphTask}可以直接执行，也可以嵌套在另外一个{@code GraphTask}
 * 中作为其中一个{@link Task}执行。</p>
 * <p>
 * 概念介绍
 * 紧前：如果 task A 依赖 task B，则 B 是 A 的紧前任务
 * 紧后：如果 task A 依赖 task B，则 A 是 B 的紧后任务
 */
public abstract class Task {

    /**
     * 执行状态，尚未执行
     */
    public static final int STATE_IDLE = 0;

    /**
     * 执行状态，正在执行中
     */
    public static final int STATE_RUNNING = 1;

    /**
     * 执行状态，已经执行完毕
     */
    public static final int STATE_FINISHED = 2;

    /**
     * 执行状态，等待执行
     */
    public static final int STATE_WAIT = 3;

    /**
     * 任务当前的状态
     */
    private volatile int currentState = STATE_IDLE;

    /**
     * 是否在主线程执行
     */
    private boolean isRunInUiThread;

    /**
     * 主线程的 handler
     */
    private static final Handler sUiThreadHandler = new Handler(Looper.getMainLooper());

    private static final ExecutorService sTaskExecutor = getDefaultExecutor();

    /**
     * 该任务的“紧后”任务
     */
    private final List<Task> successorList = new ArrayList<>();

    /**
     * 该任务的“紧前”任务
     */
    protected Set<Task> predecessorSet = new HashSet<>();

    /**
     * 该任务的名字
     */
    protected String name;

    /**
     * 该任务实际运行的内容被封装在这个 runnable 里面
     */
    private Runnable internalRunnable;

    /**
     * 该 Task 结束时的回调
     */
    private final List<TaskLifecycleListener> taskLifecycleListeners = new ArrayList<>();


    /**
     * 构造方法
     *
     * @param name task的名字
     */
    public Task(String name) {
        this.name = name;
    }

    /**
     * 构造方法
     *
     * @param name            task的名字
     * @param isRunInUiThread 是否在UI线程执行，true表示在UI线程执行，false表示在非UI线程执行，默认在非UI线程执行。
     */
    public Task(String name, boolean isRunInUiThread) {
        this.name = name;
        this.isRunInUiThread = isRunInUiThread;
    }

    public boolean isRunInUiThread() {
        return isRunInUiThread;
    }

    /**
     * 执行 task 的线程池，声明的任务，只要不是在主线程运行的，都会被放到这里线程池里面执行
     *
     * @return 线程池
     */
    private static ExecutorService getDefaultExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger mCount = new AtomicInteger(1);

                    public Thread newThread(Runnable r) {
                        return new Thread(r, "Graph-Task Thread #" + mCount.getAndIncrement());
                    }
                });
        executor.allowCoreThreadTimeOut(true);

        return executor;
    }


    /**
     * 在其中实现该 task 具体执行的逻辑。<br>
     * <strong>注意：</strong>该函数应该只由框架的{@link #start()}来调用。
     */
    public abstract void run();

    /**
     * 执行当前 task 的任务，这里会调用用户自定义的 {@link #run()}。
     */
    public synchronized void start() {
        if (currentState != STATE_IDLE) {
            throw new RuntimeException("You try to run task " + name + " twice, is there a circular dependency?");
        }

        switchState(STATE_WAIT);

        if (internalRunnable == null) {
            internalRunnable = () -> {
                notifyStarted();
                long startTime = System.currentTimeMillis();

                switchState(STATE_RUNNING);
                Task.this.run();
                switchState(STATE_FINISHED);

                long finishTime = System.currentTimeMillis();

                LogUtils.d("任务 " + name + " 耗时：" + (finishTime - startTime) + "ms，在 " + Thread.currentThread().getName() + " 线程中执行。");

                notifyFinished();
                recycle();
            };
        }

        if (isRunInUiThread) {
            // 这里使用 handler post 的方式，如果主线程调用了 waitUntilFinish，
            // 主线程会卡住，所以 runnable 永远无法执行
            sUiThreadHandler.post(internalRunnable);
        } else {
            sTaskExecutor.execute(internalRunnable);
        }
    }

    private void notifyStarted() {
        for (TaskLifecycleListener listener : taskLifecycleListeners) {
            listener.onTaskStart(this);
        }
    }

    private void switchState(int state) {
        currentState = state;
    }


    /**
     * 通知所有紧后 Task 以及 TaskLifecycleListener 自己执行完成了。
     */
    void notifyFinished() {
        if (!successorList.isEmpty()) {
            for (Task task : successorList) {
                task.onPredecessorFinished(this);
            }
        }

        for (TaskLifecycleListener listener : taskLifecycleListeners) {
            listener.onTaskFinish(this);
        }
    }

    /**
     * 这个函数在执行结束时被调用，及时释放占用的资源
     */
    void recycle() {
        successorList.clear();
        taskLifecycleListeners.clear();
    }


    /**
     * 对于当前的 Task 来说，某一个紧前 Task 执行完成时，如果该 Task 所有的紧前 Task 都执行完成，则调用自己的{@link #start()}
     * 执行自己的任务。<br>
     * 该函数由紧前 Task 的{@link #notifyFinished()}来调用。
     */

    synchronized void onPredecessorFinished(Task beforeTask) {
        if (predecessorSet.isEmpty()) {
            return;
        }
        predecessorSet.remove(beforeTask);
        if (predecessorSet.isEmpty()) {
            start();
        }
    }

    /**
     * 移除紧前 Task
     *
     * @param task 紧前 Task
     */
    void removePredecessor(Task task) {
        this.predecessorSet.remove(task);
        task.successorList.remove(this);
    }


    /**
     * 增加紧后 Task，随便也将紧前关系加上
     *
     * @param task 紧后 Task
     */
    void addSuccessor(Task task) {
        if (task == this) {
            throw new RuntimeException("A task should not after itself.");
        }
        task.predecessorSet.add(this);
        this.successorList.add(task);
    }

    /**
     * Task 生命周期的的监听
     * 注意：回调函数在 Task 所在线程中回调，注意线程安全。
     *
     * @param listener 监听 Task 生命周期的 listener
     */
    public void addTaskLifecycleListener(TaskLifecycleListener listener) {
        if (!taskLifecycleListeners.contains(listener)) {
            taskLifecycleListeners.add(listener);
        }
    }

    /**
     * 一个task完成时的回调
     */
    public interface TaskLifecycleListener {

        /**
         * 当 task 开始时，会回调这个函数。
         * 注意：该函数会在 Task 所在线程中回调，注意线程安全。
         *
         * @param task 当前启动的 Task
         */
        default void onTaskStart(Task task) {
        }

        /**
         * 当 task 完成时，会回调这个函数。
         * 注意：该函数会在 Task 所在线程中回调，注意线程安全。
         *
         * @param task 当前结束的 Task
         */
        default void onTaskFinish(Task task) {
        }
    }

}
