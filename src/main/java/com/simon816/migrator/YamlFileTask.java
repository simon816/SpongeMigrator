package com.simon816.migrator;

import com.simon816.migrator.task.Task;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.nio.file.Path;

public abstract class YamlFileTask implements Task {

    private YAMLConfigurationLoader loader;

    public YamlFileTask(Path yamlFile) {
        this.loader = YAMLConfigurationLoader.builder().setPath(yamlFile).build();
    }

    @Override
    public void run() throws Exception {
        ConfigurationNode root = this.loader.load();
        handleConfig(root);
    }

    protected abstract void handleConfig(ConfigurationNode root) throws Exception;

}
