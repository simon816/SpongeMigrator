package com.simon816.migrator.sponge;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SpongeConfigAccessor {

    private final Path confRoot;

    private SpongeConfig global;

    private Map<String, DimensionConfig> dimConfigs = new HashMap<>();

    public SpongeConfigAccessor(Path root) {
        this.confRoot = root.resolve("config/sponge");
    }

    public SpongeConfig getGlobal() {
        if (this.global == null) {
            this.global = new SpongeConfig(this.confRoot.resolve("global.conf"));
        }
        return this.global;
    }

    public SpongeConfig forWorld(String dimId, String worldId) {
        DimensionConfig dimConf = this.dimConfigs.get(dimId);
        if (dimConf == null) {
            this.dimConfigs.put(dimId, dimConf = new DimensionConfig(this.confRoot, dimId));
        }
        return dimConf.worldConfig(worldId);
    }

    private static class DimensionConfig {

        private final Path dimRoot;
        private final Map<String, SpongeConfig> worldConfigs = new HashMap<>();

        public DimensionConfig(Path spongeRoot, String dimId) {
            this.dimRoot = spongeRoot.resolve("worlds").resolve(dimId);
        }

        public SpongeConfig worldConfig(String worldId) {
            SpongeConfig conf = this.worldConfigs.get(worldId);
            if (conf == null) {
                Path confPath = this.dimRoot.resolve(worldId).resolve("world.conf");
                this.worldConfigs.put(worldId, conf = new SpongeConfig(confPath));
            }
            return conf;
        }

        public void saveAll() {
            for (SpongeConfig worldConf : this.worldConfigs.values()) {
                worldConf.save();
            }
        }
    }

    public void saveAll() {
        if (this.global != null) {
            this.global.save();
        }
        for(DimensionConfig dim : this.dimConfigs.values()) {
            dim.saveAll();
        }
    }
}
