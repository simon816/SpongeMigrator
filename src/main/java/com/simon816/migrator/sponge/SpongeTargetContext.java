package com.simon816.migrator.sponge;

import java.nio.file.Files;
import java.nio.file.Path;

public class SpongeTargetContext {

    public final Path root;

    private final SpongeConfigAccessor confAccessor;

    public SpongeTargetContext(Path dest) {
        this.root = dest;
        Files2.begForgiveness(() -> Files.createDirectories(dest));
        this.confAccessor = new SpongeConfigAccessor(dest);
    }

    public SpongeConfigAccessor getConfig() {
        return this.confAccessor;
    }

    public void finishActions() {
        this.confAccessor.saveAll();
    }

}
