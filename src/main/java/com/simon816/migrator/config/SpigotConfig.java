package com.simon816.migrator.config;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;

public class SpigotConfig {

    public final Settings settings;
    public final Messages messages;
    public final Commands commands;
    public final Stats stats;
    public final WorldSettings worldSettings;

    public SpigotConfig(ConfigurationNode root) {
        this(new Settings(root.getNode("settings")), new Messages(root.getNode("messages")), new Commands(root.getNode("commands")),
                new Stats(root.getNode("stats")), new WorldSettings(root.getNode("world-settings")));
    }

    public SpigotConfig(Settings settings, Messages messages, Commands commands, Stats stats, WorldSettings worldSettings) {
        this.settings = settings;
        this.messages = messages;
        this.commands = commands;
        this.stats = stats;
        this.worldSettings = worldSettings;
    }

    public static class Settings {

        public final ConfigValue<Boolean> bungeecord;

        public Settings(ConfigurationNode node) {
            this.bungeecord = ConfigValue.bool(node.getNode("bungeecord"), false);
        }
    }

    public static class Messages {

        public final ConfigValue<String> whitelist;
        public final ConfigValue<String> unknownCommand;
        public final ConfigValue<String> serverFull;
        public final ConfigValue<String> outdatedClient;
        public final ConfigValue<String> outdatedServer;
        public final ConfigValue<String> restart;

        public Messages(ConfigurationNode node) {
            this.whitelist = ConfigValue.string(node.getNode("whitelist"), "You are not whitelisted on this server!");
            this.unknownCommand = ConfigValue.string(node.getNode("unknown-command"), "Unknown command. Type \"/help\" for help.");
            this.serverFull = ConfigValue.string(node.getNode("server-full"), "The server is full!");
            this.outdatedClient = ConfigValue.string(node.getNode("outdated-client"), "Outdated client! Please use {0}");
            this.outdatedServer = ConfigValue.string(node.getNode("outdated-server"), "Outdated server! I'm still on {0}");
            this.restart = ConfigValue.string(node.getNode("restart"), "Server is restarting");
        }
    }

    public static class Commands {

        public Commands(ConfigurationNode node) {
            // TODO Auto-generated constructor stub
        }
    }

    public static class Stats {

        public Stats(ConfigurationNode node) {
            // TODO Auto-generated constructor stub
        }
    }

    public static class WorldSettings {

        private final Map<String, WorldSetting> settings;

        public WorldSettings(ConfigurationNode node) {
            this.settings = new HashMap<>();
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getChildrenMap().entrySet()) {
                this.settings.put(entry.getKey().toString(), new WorldSetting(entry.getValue()));
            }
        }

        public Map<String, WorldSetting> getMap() {
            return new HashMap<>(this.settings);
        }

        public static class WorldSetting {

            public final ActivationRange activationRange;
            public final TrackingRange trackingRange;

            public final ConfigValue<Double> itemMergeRadius;
            public final ConfigValue<Integer> mobSpawnRange;

            public WorldSetting(ConfigurationNode node) {
                this.activationRange = new ActivationRange(node.getNode("entity-activation-range"));
                this.trackingRange = new TrackingRange(node.getNode("entity-tracking-range"));

                this.itemMergeRadius = ConfigValue.numDouble(node.getNode("merge-radius", "item"), 2.5D);
                this.mobSpawnRange = ConfigValue.numInt(node.getNode("mob-spawn-range"), 4);
            }

            public static class ActivationRange {

                public final ConfigValue<Integer> animals;
                public final ConfigValue<Integer> monsters;
                public final ConfigValue<Integer> misc;

                public ActivationRange(ConfigurationNode node) {
                    this.animals = ConfigValue.numInt(node.getNode("animals"), 32);
                    this.monsters = ConfigValue.numInt(node.getNode("monsters"), 32);
                    this.misc = ConfigValue.numInt(node.getNode("misc"), 16);
                }
            }

            public static class TrackingRange {

                public final ConfigValue<Integer> players;
                public final ConfigValue<Integer> animals;
                public final ConfigValue<Integer> monsters;
                public final ConfigValue<Integer> misc;
                public final ConfigValue<Integer> others;

                public TrackingRange(ConfigurationNode node) {
                    this.players = ConfigValue.numInt(node.getNode("players"), 48);
                    this.animals = ConfigValue.numInt(node.getNode("animals"), 48);
                    this.monsters = ConfigValue.numInt(node.getNode("monsters"), 48);
                    this.misc = ConfigValue.numInt(node.getNode("misc"), 32);
                    this.others = ConfigValue.numInt(node.getNode("others"), 64);
                }
            }
        }
    }
}
