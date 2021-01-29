package com.aprz.graph.task;

/**
 * <p>
 * GraphTask 执行生命周期的回调。<br>
 * <strong>注意：</strong>回调接口要考虑线程安全问题。
 * </p>
 */
public interface GraphTaskLifecycleListener {

    /**
     * 当 GraphTask 开始执行时，调用该函数。<br>
     * <strong>注意：</strong>该回调函数在 Task 所在线程中回调，注意线程安全。
     */
    default void onStart() {
    }

    /**
     * 当 GraphTask 其中一个 Task 被分发到其他线程去执行是，调用该函数。<br>
     *
     * @param task 当前结束的 Task
     */
    default void onTaskDispatched(Task task) {
    }

    /**
     * 当 GraphTask 其中一个 Task 开始执行时，调用该函数。<br>
     * <strong>注意：</strong>该回调函数在 Task 所在线程中回调，注意线程安全。
     *
     * @param task 当前结束的 Task
     */
    default void onTaskStart(Task task) {
    }

    /**
     * 当 GraphTask 其中一个 Task 执行结束时，调用该函数。<br>
     * <strong>注意：</strong>该回调函数在 Task 所在线程中回调，注意线程安全。
     *
     * @param task 当前结束的 Task
     */
    default void onTaskFinish(Task task) {
    }

    /**
     * 当 GraphTask 执行结束时，调用该函数。<br>
     * <strong>注意：</strong>该回调函数在 Task 所在线程中回调，注意线程安全。
     */
    default void onFinish() {
    }
}
