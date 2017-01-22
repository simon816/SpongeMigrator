package com.simon816.migrator.task;

import com.simon816.migrator.migrations.ServerPropertiesMigration;
import com.simon816.migrator.migrations.WorldMigration;

import java.util.List;

public class TaskOrdering {

    public static void sort(List<Task> tasks) {
       // Collections.sort(tasks, null);
    }

    private static void dependenciesOf(Class<? extends Task> task, Class<? extends Task> dependency) {

    }

    static {

        dependenciesOf(WorldMigration.class, ServerPropertiesMigration.class);

    }

}
