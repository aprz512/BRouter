package com.aprz.graph.task;

import java.util.HashMap;
import java.util.Map;

public class TaskFactory {

    /**
     * Task 的键值对。key 为 Task 的 name（taskName 不能重复），value 为 Task
     */
    private final Map<String, Task> taskMap = new HashMap<>();

    /**
     * Task 工厂，根据 task 的 name 来创建一个 task
     */
    private final ITaskCreator creator;

    public TaskFactory(ITaskCreator creator) {
        this.creator = creator;
    }

    public synchronized Task getTask(String taskName) {
        Task task = taskMap.get(taskName);
        if (task != null) {
            return task;
        }

        task = creator.createTask(taskName);
        if (task == null) {
            throw new IllegalArgumentException("Create task fail, there is no task corresponding to the task name. Make sure you have create a task instance in TaskCreator.");
        }
        taskMap.put(taskName, task);

        return task;
    }
}
