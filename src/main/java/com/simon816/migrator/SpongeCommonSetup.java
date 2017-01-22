package com.simon816.migrator;

import com.google.common.base.Throwables;
import com.simon816.migrator.task.Action;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class SpongeCommonSetup implements Action {

    private static final URL API_ROOT;
    static {
        try {
            API_ROOT = new URL("https://dl-api.spongepowered.org/v1/");
        } catch (MalformedURLException e) {
            throw Throwables.propagate(e);
        }
    }

    protected URL fetchLatest(String type, String artifactId) throws IOException {
        URL apiUrl = new URL(API_ROOT, artifactId);
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setURL(apiUrl).build();
        CommentedConfigurationNode root = loader.load();
        String latest = root.getNode("buildTypes", type, "latest", "version").getString();
        URL fileInfoUrl = new URL(API_ROOT, artifactId + "/downloads/" + latest);
        loader = HoconConfigurationLoader.builder().setURL(fileInfoUrl).build();
        root = loader.load();
        String fileUrl = root.getNode("artifacts", "", "url").getString();
        return new URL(fileUrl);
    }

}
