package com.simon816.migrator.migrations;

import com.simon816.migrator.SpongeMigrator;
import com.simon816.migrator.task.ActionGraph;
import com.simon816.migrator.task.Task;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderMigration implements Task {

    private Path relPath;
    private Path absPath;

    public FolderMigration(Path folder) {
        this.relPath = SpongeMigrator.root.relativize(folder);
        this.absPath = folder.toAbsolutePath();
    }

    @Override
    public void run() throws Exception {
        ActionGraph.add(sponge -> {
            Files.walkFileTree(this.absPath, createVisitor(sponge.root));
        });
    }

    private FileVisitor<? super Path> createVisitor(Path destRoot) {
        Path destDir = destRoot.resolve(this.relPath);
        Path sourceRoot = this.absPath;
        return new SimpleFileVisitor<Path>() {


            private void copy(Path source) throws IOException {
                Files.copy(source, destDir.resolve(sourceRoot.relativize(source)), StandardCopyOption.COPY_ATTRIBUTES);
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                copy(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                copy(file);
                return FileVisitResult.CONTINUE;
            }

        };
    }

}
