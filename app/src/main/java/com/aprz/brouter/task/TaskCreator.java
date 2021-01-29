package com.aprz.brouter.task;

import com.aprz.graph.task.ITaskCreator;
import com.aprz.graph.task.Task;

public class TaskCreator implements ITaskCreator {
    @Override
    public Task createTask(String taskName) {
        switch (taskName) {
            case "TaskA":
                return new TaskA();
            case "TaskB":
                return new TaskB();
            case "TaskC":
                return new TaskC();
            case "TaskD":
                return new TaskD();
            case "TaskE":
                return new TaskE();
            case "TaskF":
                return new TaskF();
            case "TaskG":
                return new TaskG();
        }
        return null;
    }
}