package com.simon816.migrator.sponge;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SpongeConfig {

    private HoconConfigurationLoader loader;
    private ConfigurationNode root;

    public SpongeConfig(Path confPath) {
        Files2.begForgiveness(() -> Files.createDirectories(confPath.getParent()));
        Files2.begForgiveness(() -> Files.createFile(confPath));
        this.loader = HoconConfigurationLoader.builder().setPath(confPath).build();
    }

    private ConfigurationNode root() {
        if (this.root == null) {
            try {
                this.root = this.loader.load();
            } catch (IOException e) {
                this.root = this.loader.createEmptyNode();
            }
        }
        return this.root.getNode("sponge");
    }

    public ConfigurationNode world() {
        return root().getNode("world");
    }

    public void save() {
        if (this.root == null) {
            return;
        }
        try {
            this.loader.save(this.root);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ConfigurationNode sql() {
        return root().getNode("sql");
    }

}
