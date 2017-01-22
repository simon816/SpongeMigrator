package com.simon816.migrator.task;

import java.util.ArrayList;
import java.util.List;

public class TaskGraph {

    private static final List<Task> tasksToRun = new ArrayList<>();

    public static void add(Task task) {
        tasksToRun.add(task);
    }

    public static void mainLoop() throws Exception {
        while (!tasksToRun.isEmpty()) {
            List<Task> tasks = new ArrayList<>(tasksToRun);
            tasksToRun.clear();
            for (Task task : tasks) {
                task.run();
            }
        }
    }

}
