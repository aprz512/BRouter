package com.aprz.home.fragment;

import android.view.View;

import com.aprz.graph.task.GraphTask;
import com.aprz.graph.task.TaskManager;
import com.aprz.home.R;
import com.aprz.home.task.TaskCreator;

/**
 * 测试 GraphTask 库
 */
public class GraphTaskFragment extends BaseViewPagerFragment {

    @Override
    protected int getFragmentContentId() {
        return R.layout.home_fragment_graph_task;
    }

    @Override
    protected void initView(View view) {
        testCase1(view);

        testCase2(view);

        testCase3(view);

        testCase4(view);

        testCase5(view);

        testCase6(view);
    }

    /**
     * A 在子线程
     */
    private void testCase1(View root) {
        root.findViewById(R.id.graph_task_test1)
                .setOnClickListener(v -> {
                    GraphTask.Builder builder = new GraphTask.Builder();
                    GraphTask graphTask = builder.name("GraphTask TestCase 1")
                            .taskCreator(new TaskCreator())
                            .add("TaskA").noDepends()
                            .build();
                    TaskManager.getInstance()
                            .addGraphTask(graphTask)
                            .start()
                            .waitUntilFinish();
                });
    }

    /**
     * B 在主线程
     */
    private void testCase2(View root) {
        root.findViewById(R.id.graph_task_test2)
                .setOnClickListener(v -> {
                    GraphTask.Builder builder = new GraphTask.Builder();
                    GraphTask graphTask = builder.name("GraphTask TestCase 2")
                            .taskCreator(new TaskCreator())
                            .add("TaskB").noDepends()
                            .build();
                    TaskManager.getInstance()
                            .addGraphTask(graphTask)
                            .start()
                            .waitUntilFinish();
                });
    }

    /**
     * A 在子线程
     * B 在主线程
     * B 依赖于 A
     */
    private void testCase3(View root) {
        root.findViewById(R.id.graph_task_test3)
                .setOnClickListener(v -> {
                    GraphTask.Builder builder = new GraphTask.Builder();
                    GraphTask graphTask = builder.name("GraphTask TestCase 3")
                            .taskCreator(new TaskCreator())
                            .add("TaskA").noDepends()
                            .add("TaskB").dependsOn("TaskA")
                            .build();
                    TaskManager.getInstance()
                            .addGraphTask(graphTask)
                            .start()
                            .waitUntilFinish();
                });
    }

    /**
     * A --> F --|--> G
     * C --|--> G
     */
    private void testCase4(View root) {
        root.findViewById(R.id.graph_task_test4)
                .setOnClickListener(v -> {
                    GraphTask.Builder builder = new GraphTask.Builder();
                    GraphTask graphTask = builder.name("GraphTask TestCase 4")
                            .taskCreator(new TaskCreator())
                            .add("TaskA").noDepends()
                            .add("TaskF").dependsOn("TaskA")
                            .add("TaskC").noDepends()
                            .add("TaskG").dependsOn("TaskF", "TaskC")
                            .build();
                    TaskManager.getInstance()
                            .addGraphTask(graphTask)
                            .start()
                            .waitUntilFinish();
                });
    }

    private void testCase5(View root) {
        root.findViewById(R.id.graph_task_test5)
                .setOnClickListener(v -> {
                    GraphTask.Builder builder = new GraphTask.Builder();
                    GraphTask graphTask = builder.name("GraphTask TestCase 5")
                            .taskCreator(new TaskCreator())
                            .add("TaskA").noDepends()
                            .add("TaskB").dependsOn("TaskA")
                            .add("TaskC").dependsOn("TaskA")
                            .add("TaskD").dependsOn("TaskC")
                            .build();
                    TaskManager.getInstance()
                            .addGraphTask(graphTask)
                            .start()
                            .waitUntilFinish();
                });
    }

    private void testCase6(View root) {
        root.findViewById(R.id.graph_task_test6)
                .setOnClickListener(v -> {
                    GraphTask subGraphTask = new GraphTask.Builder().name("GraphTask SubTask")
                            .taskCreator(new TaskCreator())
                            .add("TaskA").noDepends()
                            .add("TaskB").dependsOn("TaskA")
                            .add("TaskC").dependsOn("TaskA")
                            .add("TaskD").dependsOn("TaskC", "TaskB")
                            .build();
                    GraphTask.Builder builder = new GraphTask.Builder().name("GraphTask TestCase 6")
                            .taskCreator(new TaskCreator())
                            .add(subGraphTask).noDepends()
                            .add("TaskE").dependsOn(subGraphTask)
                            .add("TaskF").noDepends()
                            .add("TaskG").dependsOn("TaskF");
                    TaskManager.getInstance()
                            .addGraphTask(builder.build())
                            .start()
                            .waitUntilFinish();
                });
    }
}
