package com.simon816.migrator.migrations;

import com.simon816.migrator.PropertyFileTask;
import com.simon816.migrator.task.ActionGraph;
import com.simon816.migrator.task.Actions;
import com.simon816.migrator.task.IssueResolutions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ServerPropertiesMigration extends PropertyFileTask {

    public ServerPropertiesMigration(Path propertiesFile) {
        super(propertiesFile);
    }

    @Override
    protected void handleProperties(Properties properties) throws Exception {
        Set<Object> remaining = new HashSet<>(properties.keySet());
        for (Map.Entry<String, PropertyRule> entry : propertyRules.entrySet()) {
            String value = properties.getProperty(entry.getKey());
            entry.getValue().stageMigration(value);
            remaining.remove(entry.getKey());
        }
        for (Object o : remaining) {
            String key = o.toString();
            ActionGraph.add(Actions.raiseIssue("Found unknown key '" + key + "' in server.properties. What do?", IssueResolutions.abort(),
                    IssueResolutions.performTask("Copy across anyway", () -> {

                    }), IssueResolutions.performTask("Skip property", () -> {
                    })));
        }
        ActionGraph.add(Actions.afterIssues(sponge -> {
            Files.copy(this.propertyFile, sponge.root.resolve("server.properties"), StandardCopyOption.COPY_ATTRIBUTES);
        }));
    }

    private static interface PropertyRule {

        void stageMigration(String value);
    }

    private static PropertyRule NO_CAPTURE = v -> {
    };

    private static Map<String, PropertyRule> propertyRules = new HashMap<>();

    static {
        propertyRules.put("online-mode", NO_CAPTURE);
        propertyRules.put("prevent-proxy-connections", NO_CAPTURE);
        propertyRules.put("server-ip", NO_CAPTURE);
        propertyRules.put("spawn-animals", NO_CAPTURE);
        propertyRules.put("spawn-npcs", NO_CAPTURE);
        propertyRules.put("pvp", NO_CAPTURE);
        propertyRules.put("allow-flight", NO_CAPTURE);
        propertyRules.put("resource-pack", NO_CAPTURE);
        propertyRules.put("motd", NO_CAPTURE);
        propertyRules.put("force-gamemode", NO_CAPTURE);
        propertyRules.put("player-idle-timeout", NO_CAPTURE);
        propertyRules.put("difficulty", NO_CAPTURE);
        propertyRules.put("generate-structures", NO_CAPTURE);
        propertyRules.put("server-port", NO_CAPTURE);
        propertyRules.put("level-name", NO_CAPTURE);
        propertyRules.put("level-seed", NO_CAPTURE);
        propertyRules.put("level-type", NO_CAPTURE);
        propertyRules.put("generator-settings", NO_CAPTURE);
        propertyRules.put("max-build-height", NO_CAPTURE);
        propertyRules.put("enable-query", NO_CAPTURE);
        propertyRules.put("enable-rcon", NO_CAPTURE);
        propertyRules.put("resource-pack-hash", NO_CAPTURE);
        propertyRules.put("resource-pack-sha1", NO_CAPTURE);
        propertyRules.put("hardcore", NO_CAPTURE);
        propertyRules.put("allow-nether", NO_CAPTURE);
        propertyRules.put("spawn-monsters", NO_CAPTURE);
        propertyRules.put("snooper-enabled", NO_CAPTURE);
        propertyRules.put("enable-command-block", NO_CAPTURE);
        propertyRules.put("spawn-protection", NO_CAPTURE);
        propertyRules.put("op-permission-level", NO_CAPTURE);
        propertyRules.put("broadcast-rcon-to-op", NO_CAPTURE);
        propertyRules.put("broadcast-console-to-ops", NO_CAPTURE);
        propertyRules.put("announce-player-achievements", NO_CAPTURE);
        propertyRules.put("max-world-size", NO_CAPTURE);
        propertyRules.put("network-compression-threshold", NO_CAPTURE);
        propertyRules.put("max-tick-time", NO_CAPTURE);
        propertyRules.put("view-distance", NO_CAPTURE);
        propertyRules.put("max-players", NO_CAPTURE);
        propertyRules.put("white-list", NO_CAPTURE);
        propertyRules.put("rcon.port", NO_CAPTURE);
        propertyRules.put("rcon.password", NO_CAPTURE);
        propertyRules.put("query.port", NO_CAPTURE);
        propertyRules.put("debug", NO_CAPTURE);
        propertyRules.put("gamemode", NO_CAPTURE);
    }

}
