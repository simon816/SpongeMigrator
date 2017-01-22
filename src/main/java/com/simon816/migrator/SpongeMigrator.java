package com.simon816.migrator;

import com.simon816.migrator.sponge.SpongeTargetContext;
import com.simon816.migrator.task.ActionGraph;
import com.simon816.migrator.task.TaskGraph;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class SpongeMigrator {

    public enum SpongeType {
        SPONGE_VANILLA,
        SPONGE_FORGE
    }

    public static final Path root = Paths.get("D:\\Temp\\spigot");

    public static SpongeType destType;

    public static void main(String[] args) throws Exception {
        destType = SpongeType.SPONGE_VANILLA;
        Migrator migrator = new Migrator(root);
        TaskGraph.add(migrator);
        TaskGraph.mainLoop();
        FileClaims.showUnclaimed();

        SpongeTargetContext target = new SpongeTargetContext(Paths.get("D:\\Temp\\sponge_" + String.valueOf(new Random().nextInt())));

        ActionGraph.mainLoop(target);
        target.finishActions();
    }

}
