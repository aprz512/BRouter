package com.aprz.brouter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.R;
import com.aprz.brouter.task.TaskCreator;
import com.aprz.graph.task.GraphTask;
import com.aprz.graph.task.TaskManager;

public class GraphTaskFragment extends Fragment {

    private static final String TAG = "GraphTaskFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.app_fragment_graph_task, container, false);

        root.findViewById(R.id.graph_task_test1)
                .setOnClickListener(v -> {
                    GraphTask.Builder builder = new GraphTask.Builder();
                    GraphTask graphTask = builder.name("GraphTask 1")
                            .taskCreator(new TaskCreator())
                            .add("TaskA").noDepends()
                            .build();
                    TaskManager.getInstance()
                            .addGraphTask(graphTask)
                            .start()
                            .waitUntilFinish();
                });

        root.findViewById(R.id.graph_task_test2)
                .setOnClickListener(v -> {
                    GraphTask.Builder builder = new GraphTask.Builder();
                    GraphTask graphTask = builder.name("GraphTask 2")
                            .taskCreator(new TaskCreator())
                            .add("TaskB").noDepends()
                            .build();
                    TaskManager.getInstance()
                            .addGraphTask(graphTask)
                            .start()
                            .waitUntilFinish();
                });

        return root;
    }
}
