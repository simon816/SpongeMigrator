package com.simon816.migrator.migrations;

import com.simon816.migrator.YamlFileTask;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;

public class HelpYMLMigration extends YamlFileTask {

    public HelpYMLMigration(Path helpYml) {
        super(helpYml);
    }

    @Override
    protected void handleConfig(ConfigurationNode root) throws Exception {
        // TODO Auto-generated method stub

    }
}
