package com.simon816.migrator.migrations;

import com.simon816.migrator.YamlFileTask;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;

public class CommandYMLMigration extends YamlFileTask {

    public CommandYMLMigration(Path commandYml) {
        super(commandYml);
    }

    @Override
    protected void handleConfig(ConfigurationNode root) throws Exception {
        // TODO Auto-generated method stub

    }
}
