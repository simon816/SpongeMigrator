package com.simon816.migrator.migrations;

import com.simon816.migrator.FileClaims;
import com.simon816.migrator.SpongeMigrator;
import com.simon816.migrator.YamlFileTask;
import com.simon816.migrator.config.BukkitConfig;
import com.simon816.migrator.sponge.SpongeConfig;
import com.simon816.migrator.task.ActionGraph;
import com.simon816.migrator.task.Actions;
import com.simon816.migrator.task.IssueResolutions;
import com.simon816.migrator.task.TaskGraph;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitYMLMigration extends YamlFileTask {

    public BukkitYMLMigration(Path ymlFile) {
        super(ymlFile);
    }

    @Override
    protected void handleConfig(ConfigurationNode root) throws Exception {
        BukkitConfig config = new BukkitConfig(root);

        Set<String> known = new HashSet<>(Arrays.asList("settings", "spawn-limits", "chunk-gc", "ticks-per", "aliases", "database"));
        Map<Object, ? extends ConfigurationNode> childrenMap = root.getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : childrenMap.entrySet()) {
            String key = entry.getKey().toString();
            if (!known.contains(key)) {
                System.out.println(key);
            }
        }
        config.settings.connThrottle.getOrDefault(v -> {
            if (v != 0) {
                ActionGraph.add(Actions.raiseIssue("A connection throttle is set in the bukkit config. This option is not available with sponge",
                        IssueResolutions.abort(), IssueResolutions.ignore()));
            }
        });
        config.settings.warnOnOverload.ifExists(warn -> {
            if (!warn) {
                ActionGraph
                        .add(Actions.raiseIssue("Warning on overload is set to false in the bukkit config. This option is not available with sponge",
                                IssueResolutions.abort(), IssueResolutions.ignore()));
            }
        });
        config.settings.permFile.getOrDefault(filename -> {
            Path path = SpongeMigrator.root.resolve(filename);
            FileClaims.claim(path);
            TaskGraph.add(new PermissionsYMLMigration(path));
        });
        config.settings.queryPlugins.getOrDefault(query -> {
            if (query) {
                ActionGraph.add(Actions.raiseIssue("Query plugins when queried remotely is enabled. This option is not available with sponge",
                        IssueResolutions.abort(), IssueResolutions.ignore()));
            }
        });
        config.settings.shutdownMessage.ifNotDefault(message -> {
            ActionGraph.add(Actions.raiseIssue("Shutdown message is set to a non-default message. This option is not available with sponge",
                    IssueResolutions.abort(), IssueResolutions.ignore()));
        });
//        config.settings.pluginProfiling

        ActionGraph.add(Actions.withConfig(conf -> {
            SpongeConfig global = conf.getGlobal();
            config.settings.allowEnd.ifExists(v -> {
                SpongeConfig endConf = conf.forWorld("the_end", "DIM1");
                endConf.world().getNode("world-enabled").setValue(v);
            });
        }));

        config.spawnLimits.monsters.getOrDefault(val -> {
            if (val != 70) {
                ActionGraph.add(Actions.raiseIssue("Monster spawn limit is not 70 (found " + val + "). This option is not available with sponge",
                        IssueResolutions.abort()));
            }
        });
        config.spawnLimits.animals.getOrDefault(val -> {
            if (val != 10) {
                ActionGraph.add(Actions.raiseIssue("Animal spawn limit is not 10 (found " + val + "). This option is not available with sponge",
                        IssueResolutions.abort()));
            }
        });
        config.spawnLimits.ambient.getOrDefault(val -> {
            if (val != 15) {
                ActionGraph
                        .add(Actions.raiseIssue("Ambient entity spawn limit is not 15 (found " + val + "). This option is not available with sponge",
                                IssueResolutions.abort()));
            }
        });
        config.spawnLimits.waterAnimals.getOrDefault(val -> {
            if (val != 5) {
                ActionGraph.add(Actions.raiseIssue("Water animal spawn limit is not 5 (found " + val + "). This option is not available with sponge",
                        IssueResolutions.abort()));
            }
        });
        ActionGraph.add(Actions.withConfig(conf -> {
            ConfigurationNode worldNode = conf.getGlobal().world();
            worldNode.getNode("chunk-gc-load-threshold").setValue(config.chunkGC.loadThreshold.getOrDefault());
            worldNode.getNode("chunk-gc-tick-interval").setValue(config.chunkGC.periodTicks.getOrDefault());
        }));

        int animalSpawnTicks = config.ticksPer.animalSpawns.getOrDefault();
        if (animalSpawnTicks != 400) {
            if (animalSpawnTicks == 0) {

            } else {

            }
        }
        int monsterSpawnTicks = config.ticksPer.monsterSpawns.getOrDefault();
        if (monsterSpawnTicks != 1) {
            if (monsterSpawnTicks == 0) {

            } else {

            }
        }
        ActionGraph.add(Actions.withConfig(conf -> {
            Integer autosaveTicks = config.ticksPer.autosave.getOrDefault();
            conf.getGlobal().world().getNode("auto-player-save-interval").setValue(autosaveTicks);
        }));
        ConfigurationNode aliases = config.aliases;
        if (!aliases.isVirtual() && !aliases.getString().equals("now-in-commands.yml")) {
            ActionGraph.add(Actions.raiseIssue("Aliases config value not converted. Please run a recent version of craftbukkit to migrate",
                    IssueResolutions.abort()));
        }
        if (config.database.isNotDefault()) {
            ActionGraph.add(Actions.withConfig(conf -> {
                ConfigurationNode sql = conf.getGlobal().sql();
                // Taken from SpongeCommon's SqlServiceImpl
                Pattern urlRegex = Pattern.compile("(?:jdbc:)?([^:]+):(//)?(?:([^:]+)(?::([^@]+))?@)?(.*)");
                Matcher match = urlRegex.matcher(config.database.url.getValue());
                if (match.matches()) {
                    String protocol = match.group(1);
                    boolean hasSlashes = match.group(2) != null;
                    String user = match.group(3);
                    String pass = match.group(4);
                    String serverDatabaseSpecifier = match.group(5);
                    if (user == null) {
                        user = config.database.username.getOrElse("");
                    }
                    if (pass == null) {
                        pass = config.database.password.getOrElse("");
                    }
                    String connection = new StringBuilder("jdbc:").append(protocol).append(hasSlashes ? "://" : ":")
                            .append(user).append(pass.isEmpty() ? "" : ":").append(pass)
                            .append(!user.isEmpty() ? "@" : "").append(serverDatabaseSpecifier).toString();
                    sql.getNode("aliases", "bukkit-migrated").setValue(connection);
                }
            }));
        }
    }
}
