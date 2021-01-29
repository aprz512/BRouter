package com.aprz.graph.task;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GraphTask extends Task implements GraphTaskLifecycleListener {

    /**
     * 该任务图的起点 task
     */
    private AnchorTask startTask;
    /**
     * 该任务图的终点 task
     */
    private AnchorTask finishTask;

    /**
     * 该任务图的执行的开始时间
     */
    private long startTime;

    private final List<GraphTaskLifecycleListener> lifecycleListener = new ArrayList<>();

    public GraphTask(String name) {
        super(name, false);
    }

    @Override
    public void call() {
        // do nothing
    }

    @Override
    public void start() {
        startTask.start();
    }

    @Override
    synchronized void addSuccessor(Task task) {
        finishTask.addSuccessor(task);
    }

    @Override
    public void addTaskLifecycleListener(final TaskLifecycleListener listener) {
        finishTask.addTaskLifecycleListener(listener);
    }

    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onTaskDispatched(Task task) {
        for (GraphTaskLifecycleListener listener : lifecycleListener) {
            listener.onTaskDispatched(task);
        }
    }

    @Override
    public void onTaskStart(Task task) {
        for (GraphTaskLifecycleListener listener : lifecycleListener) {
            listener.onTaskStart(task);
        }
    }

    @Override
    public void onTaskFinish(Task task) {
        for (GraphTaskLifecycleListener listener : lifecycleListener) {
            listener.onTaskFinish(task);
        }
    }

    @Override
    public void onFinish() {
        LogUtils.d("GraphTask: " + name + " 耗时 " + (System.currentTimeMillis() - startTime) + "ms");
        for (GraphTaskLifecycleListener listener : lifecycleListener) {
            listener.onFinish();
        }
    }

    /**
     * 设置 GraphTask 执行生命周期的回调，可以监听到 GraphTask 开始执行与结束执行，GraphTask 内部的 Task 执行结束.
     *
     * @param listener GraphTask执行生命周期的回调
     */
    public void addLifecycleListener(GraphTaskLifecycleListener listener) {
        lifecycleListener.add(listener);
    }

    void setStartTask(AnchorTask startTask) {
        this.startTask = startTask;
    }

    void setFinishTask(AnchorTask finishTask) {
        this.finishTask = finishTask;
    }

    @Override
    void recycle() {
        super.recycle();
        lifecycleListener.clear();
    }

    @Override
    public List<Task> getSuccessorList() {
        return startTask.getSuccessorList();
    }

    /**
     * <p>通过 Builder将多个 Task 组成一个 Project 。它可以单独拿出去执行，也可以作为
     * 一个子 Task 嵌入到另外一个 Project 中。</p>
     */
    public static class Builder {
        /**
         * 正在添加的目标任务
         * 使用 add 方法添加任务，然后使用 dependsOn 或者 noDepends 来修饰该任务
         */
        private Task targetTask;

        private final AnchorTask finishTask;
        private final AnchorTask startTask;
        private TaskFactory taskFactory;
        private GraphTask graphTask;
        private String graphTaskName;

        public Builder() {
            startTask = new AnchorTask("--> GraphTask-StartTask <--");
            finishTask = new AnchorTask("--> GraphTask-FinishTask <--");
        }

        /**
         * 设置 graphTask 的名称
         *
         * @param graphTaskName graphTask 的名称
         * @return Builder 对象，可以继续添加属性或者组装 Task 。
         */
        public Builder name(String graphTaskName) {
            this.graphTaskName = graphTaskName;
            return Builder.this;
        }

        /**
         * 利用 TaskCreator，之后可以直接用 taskName 来操作 add 和 after 等逻辑。
         */
        public Builder taskCreator(ITaskCreator creator) {
            taskFactory = new TaskFactory(creator);
            return Builder.this;
        }


        /**
         * 用 Task 名称进行操作，需要提前调用{@link #taskCreator(ITaskCreator)} 创建名称对应的 task 实例
         *
         * @param taskName 增加的 Task 对象的名称。
         * @return Builder 对象，可以继续添加属性或者组装 Task 。
         */
        public Builder add(String taskName) {
            checkTaskFactory();

            Task task = taskFactory.getTask(taskName);
            add(task);

            return Builder.this;
        }

        /**
         * 增加一个 Task，在调用该方法后，需要调用{@link #dependsOn(String...)} (Task)}来确定它在图中的位置，
         * 如果该 Task 没有依赖的任务，则需要调用 {@link #noDepends()} 方法。
         *
         * @param task 增加的 Task 对象.
         */
        public Builder add(Task task) {
            targetTask = task;
            targetTask.addSuccessor(finishTask);
            targetTask.addTaskLifecycleListener(new TaskLifecycleListener() {
                @Override
                public void onTaskStart(Task task) {
                    graphTask.onTaskStart(task);
                }

                @Override
                public void onTaskFinish(Task task) {
                    graphTask.onTaskFinish(task);
                }

                @Override
                public void onTaskDispatched(Task task) {
                    graphTask.onTaskDispatched(task);
                }
            });
            return this;
        }

        /**
         * 指定紧前 Task，必须等这些指定的 Task 执行完后才能执行自己。
         * 如果没有紧前 Task，必须调用 {@link #noDepends()} 方法
         *
         * @param taskNames 所有的紧前 Task
         * @return Builder 对象，可以继续添加属性或者组装 Task 。
         */
        public Builder dependsOn(@NonNull String... taskNames) {
            checkTaskFactory();
            checkTaskNames(taskNames);

            Task[] tasks = new Task[taskNames.length];
            for (int i = 0, len = taskNames.length; i < len; i++) {
                String taskName = taskNames[i];
                Task task = taskFactory.getTask(taskName);
                tasks[i] = task;
            }
            dependsOn(tasks);
            return Builder.this;
        }

        /**
         * 如果没有紧前 Task，必须调用该方法
         *
         * @return Builder 对象，可以继续添加属性或者组装 Task 。
         */
        public Builder noDepends() {
            checkTaskFactory();
            dependsOn(new Task[0]);
            return Builder.this;
        }

        public Builder dependsOn(@NonNull Task... tasks) {
            if (tasks.length <= 0) {
                startTask.addSuccessor(targetTask);
            } else {
                for (Task task : tasks) {
                    task.addSuccessor(targetTask);
                    finishTask.removePredecessor(task);
                }
            }

            return Builder.this;
        }

        public void checkTaskFactory() {
            if (taskFactory == null) {
                throw new IllegalAccessError("You should set a ITaskCreator with taskCreator() !!!");
            }
        }

        private void checkTaskNames(String... taskNames) {
            if (taskNames.length <= 0) {
                throw new IllegalAccessError("taskNames should not be empty !!!");
            }
        }

        public GraphTask build() {
            graphTask = new GraphTask(graphTaskName);
            startTask.addTaskLifecycleListener(new TaskLifecycleListener() {
                @Override
                public void onTaskStart(Task task) {
                    graphTask.onStart();
                }
            });
            finishTask.addTaskLifecycleListener(new TaskLifecycleListener() {
                @Override
                public void onTaskFinish(Task task) {
                    graphTask.onFinish();
                }
            });
            graphTask.setStartTask(startTask);
            graphTask.setFinishTask(finishTask);
            return graphTask;
        }
    }


    /**
     * <p>从图的执行角度来讲，应该要有唯一的开始位置和唯一的结束位置。这样就可以准确衡量一个图的开始和结束。并且可以
     * 通过开始点和结束点，方便地将这个图嵌入到另外一个图中去。</p>
     * <p>但是从用户的角度来理解，他可能会有多个 task 可以同时开始，也可以有多个 task 作为结束点。</p>
     * <p>为了解决这个矛盾，框架提供一个默认的开始节点和默认的结束节点。并且将这两个点称为这个 GraphTak 的锚点。
     * 用户添加的 task 都是添加在开始锚点后，用户的添加的 task 后也都会有一个默认的结束锚点。</p>
     * <p>如前面提到，锚点的作用有两个：
     * <li>标记一个 GraphTak 的开始和结束。</li>
     * <li>当 GraphTak 需要作为一个 task 嵌入到另外一个 GraphTak 里面时，锚点可以用来和其他 task
     * 进行连接。</li>
     * </p>
     */
    private static class AnchorTask extends Task {

        public AnchorTask(String name) {
            super(name, false);
        }

        @Override
        public void call() {

        }

    }

}
