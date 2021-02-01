package com.aprz.home.task;

import android.os.SystemClock;

import com.aprz.graph.task.Task;

import java.util.Random;

public abstract class BaseTask extends Task {

    /**
     * 构造方法
     *
     * @param name            task的名字
     * @param isRunInUiThread 是否在UI线程执行，true表示在UI线程执行，false表示在非UI线程执行，默认在非UI线程执行。
     */
    public BaseTask(String name, boolean isRunInUiThread) {
        super(name, isRunInUiThread);
    }

    @Override
    public void call() {
        SystemClock.sleep(new Random(System.currentTimeMillis()).nextInt(500));
    }

}
