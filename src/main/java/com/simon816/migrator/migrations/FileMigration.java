package com.simon816.migrator.migrations;

import com.simon816.migrator.SpongeMigrator;
import com.simon816.migrator.task.ActionGraph;
import com.simon816.migrator.task.Task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileMigration implements Task {

    private final Path relPath;
    private final Path absPath;

    public FileMigration(Path file) {
        this.absPath = file.toAbsolutePath();
        this.relPath = SpongeMigrator.root.relativize(file);
    }

    @Override
    public void run() throws Exception {
        ActionGraph.add(sponge -> {
            Path newPath = sponge.root.resolve(this.relPath);
            Files.copy(this.absPath, newPath, StandardCopyOption.COPY_ATTRIBUTES);
        });
    }

}
