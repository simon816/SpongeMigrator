package com.simon816.migrator;

import com.simon816.migrator.sponge.SpongeTargetContext;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpongeVanillaSetup extends SpongeCommonSetup {

    @Override
    public void runAction(SpongeTargetContext sponge) throws Exception {
        URL jarUrl = fetchLatest("stable", "org.spongepowered/spongevanilla");
        String jarName = Paths.get(jarUrl.getPath()).getFileName().toString();
        Path target = sponge.root.resolve(jarName);
        Files.copy(jarUrl.openStream(), target);
    }

}
