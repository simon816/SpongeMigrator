package com.simon816.migrator;

import com.simon816.migrator.task.Action;
import com.simon816.migrator.task.ActionGraph;
import com.simon816.migrator.task.Task;

import java.nio.file.Path;
import java.util.List;

public class JarResolverTask implements Task {

    private Path[] jarFiles;

    public JarResolverTask(List<Path> candidates) {
        this.jarFiles = candidates.toArray(new Path[candidates.size()]);
    }

    @Override
    public void run() throws Exception {
        if (this.jarFiles.length == 0) {
            throw new Exception("No jar files found in server root");
        }
        if (this.jarFiles.length > 1) {
            throw new Exception("Multiple jars found. Don't know what to do");
        }

        Path jar = this.jarFiles[0];
        FileClaims.claim(jar);
        Action action = null;
        switch (SpongeMigrator.destType) {
            case SPONGE_FORGE:
                action = new SpongeForgeSetup();
                break;
            case SPONGE_VANILLA:
                action = new SpongeVanillaSetup();
                break;
        }
        if (action != null) {
            ActionGraph.add(action);
        }
    }

}
