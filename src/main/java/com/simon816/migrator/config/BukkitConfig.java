package com.simon816.migrator.config;

import ninja.leaping.configurate.ConfigurationNode;

public class BukkitConfig {

    public final Settings settings;
    public final SpawnLimits spawnLimits;
    public final ChunkGC chunkGC;
    public final TicksPer ticksPer;
    public final ConfigurationNode aliases;
    public final Database database;

    public BukkitConfig(ConfigurationNode root) {
        this(new Settings(root.getNode("settings")), new SpawnLimits(root.getNode("spawn-limits")),
                new ChunkGC(root.getNode("chunk-gc")), new TicksPer(root.getNode("ticks-per")),
                root.getNode("aliases"), new Database(root.getNode("database")));
    }

    public BukkitConfig(Settings settings, SpawnLimits spawnLimits, ChunkGC chunkGC, TicksPer ticksPer, ConfigurationNode aliases,
            Database database) {
        this.settings = settings;
        this.spawnLimits = spawnLimits;
        this.chunkGC = chunkGC;
        this.ticksPer = ticksPer;
        this.aliases = aliases;
        this.database = database;
    }

    public static class Settings {

        public final ConfigValue<Boolean> allowEnd;
        public final ConfigValue<Boolean> warnOnOverload;
        public final ConfigValue<String> permFile;
        public final ConfigValue<Boolean> pluginProfiling;
        public final ConfigValue<Long> connThrottle;
        public final ConfigValue<Boolean> queryPlugins;
        public final ConfigValue<String> shutdownMessage;

        public Settings(ConfigurationNode settings) {
            this.allowEnd = ConfigValue.bool(settings.getNode("allow-end"), true);
            this.warnOnOverload = ConfigValue.bool(settings.getNode("warn-on-overload"), true);
            this.permFile = ConfigValue.string(settings.getNode("permissions-file"), "permissions.yml");
            this.pluginProfiling = ConfigValue.bool(settings.getNode("plugin-profiling"), false);
            this.connThrottle = ConfigValue.numLong(settings.getNode("connection-throttle"), 4000);
            this.queryPlugins = ConfigValue.bool(settings.getNode("query-plugins"), true);
            this.shutdownMessage = ConfigValue.string(settings.getNode("shutdown-message"), "Server closed");
        }
    }

    public static class SpawnLimits {

        public final ConfigValue<Integer> monsters;
        public final ConfigValue<Integer> animals;
        public final ConfigValue<Integer> waterAnimals;
        public final ConfigValue<Integer> ambient;

        public SpawnLimits(ConfigurationNode node) {
            this.monsters = ConfigValue.numInt(node.getNode("monsters"), 70);
            this.animals = ConfigValue.numInt(node.getNode("animals"), 15);
            this.waterAnimals = ConfigValue.numInt(node.getNode("water-animals"), 5);
            this.ambient = ConfigValue.numInt(node.getNode("ambient"), 15);
        }
    }

    public static class ChunkGC {

        public final ConfigValue<Integer> periodTicks;
        public final ConfigValue<Integer> loadThreshold;

        public ChunkGC(ConfigurationNode node) {
            this.periodTicks = ConfigValue.numInt(node.getNode("period-in-ticks"), 600);
            this.loadThreshold = ConfigValue.numInt(node.getNode("load-threshold"), 0);
        }
    }

    public static class TicksPer {

        public final ConfigValue<Integer> animalSpawns;
        public final ConfigValue<Integer> monsterSpawns;
        public final ConfigValue<Integer> autosave;

        public TicksPer(ConfigurationNode node) {
            this.animalSpawns = ConfigValue.numInt(node.getNode("animal-spawns"), 400);
            this.monsterSpawns = ConfigValue.numInt(node.getNode("monster-spawns"), 1);
            this.autosave = ConfigValue.numInt(node.getNode("autosave"), 6000);
        }

    }

    public static class Database {

        public final ConfigValue<String> username;
        public final ConfigValue<String> isolation;
        public final ConfigValue<String> driver;
        public final ConfigValue<String> password;
        public final ConfigValue<String> url;

        public Database(ConfigurationNode node) {
            this.username = ConfigValue.string(node.getNode("username"), "bukkit");
            this.isolation = ConfigValue.string(node.getNode("isolation"), "SERIALIZABLE");
            this.driver = ConfigValue.string(node.getNode("driver"), "org.sqlite.JDBC");
            this.password = ConfigValue.string(node.getNode("password"), "walrus");
            this.url = ConfigValue.string(node.getNode("url"), "jdbc:sqlite:{DIR}{NAME}.db");
        }

        public boolean isNotDefault() {
            return !this.username.isDefault() || !this.password.isDefault() || !this.url.isDefault();
        }
    }

}
