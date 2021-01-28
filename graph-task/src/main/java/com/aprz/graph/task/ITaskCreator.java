package com.aprz.graph.task;

public interface ITaskCreator {
    /**
     * 根据 Task 名称，创建 Task 实例。这个接口需要使用者自己实现。创建后的实例会被缓存起来。
     *
     * @param taskName Task名称
     * @return Task实例
     */
    public Task createTask(String taskName);
}
