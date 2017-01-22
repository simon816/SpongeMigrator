package com.simon816.migrator;

import com.simon816.migrator.migrations.BukkitYMLMigration;
import com.simon816.migrator.migrations.CommandYMLMigration;
import com.simon816.migrator.migrations.FileMigration;
import com.simon816.migrator.migrations.FolderMigration;
import com.simon816.migrator.migrations.HelpYMLMigration;
import com.simon816.migrator.migrations.PluginMigration;
import com.simon816.migrator.migrations.ServerPropertiesMigration;
import com.simon816.migrator.migrations.SpigotYMLMigration;
import com.simon816.migrator.migrations.WorldMigration;
import com.simon816.migrator.task.Task;
import com.simon816.migrator.task.TaskGraph;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Migrator implements Task {

    private final Path root;
    private List<Path> jarFiles = new ArrayList<>();

    public Migrator(Path serverRoot) {
        this.root = serverRoot;

        if (!Files.exists(serverRoot.resolve("server.properties"))) {
            throw new IllegalStateException("server.properties not found! Root: " + serverRoot);
        }
    }

    @Override
    public void run() throws Exception {
        this.jarFiles.clear();
        try (DirectoryStream<Path> dir = Files.newDirectoryStream(this.root)) {
            for (Path path : dir) {
                FileClaims.unclaimed(path);
                Task task = handlePath(path);
                if (task != null) {
                    FileClaims.claim(path);
                    TaskGraph.add(task);
                }
            }
        }
        TaskGraph.add(new JarResolverTask(this.jarFiles));
    }

    private Task handlePath(Path path) {
        String filename = path.getFileName().toString();
        switch (filename) {
            case "logs":
                return new FolderMigration(path);
            case "plugins":
                return new PluginMigration(path);
            case "banned-ips.json":
            case "banned-players.json":
            case "eula.txt":
            case "ops.json":
            case "usercache.json":
            case "whitelist.json":
                return new FileMigration(path);
            case "server.properties":
                return new ServerPropertiesMigration(path);
            case "bukkit.yml":
                return new BukkitYMLMigration(path);
            case "commands.yml":
                return new CommandYMLMigration(path);
            case "help.yml":
                return new HelpYMLMigration(path);
            case "spigot.yml":
                return new SpigotYMLMigration(path);
            default:
                if (Files.isDirectory(path)) {
                    if (Files.exists(path.resolve("level.dat"))) {
                        return new WorldMigration(path);
                    }
                } else {
                    if (filename.endsWith(".jar")) {
                        this.jarFiles.add(path);
                        return null;
                    }
                }
                return null;
//                return new UnknownPathTask(path);
        }
    }

}
