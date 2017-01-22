package com.simon816.migrator.migrations;

import com.simon816.migrator.YamlFileTask;
import com.simon816.migrator.config.SpigotConfig;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;

public class SpigotYMLMigration extends YamlFileTask {

    public SpigotYMLMigration(Path spigotYml) {
        super(spigotYml);
    }

    @Override
    protected void handleConfig(ConfigurationNode root) throws Exception {
        SpigotConfig config = new SpigotConfig(root);
    }
}
