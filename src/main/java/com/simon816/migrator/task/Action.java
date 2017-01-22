package com.simon816.migrator.task;

import com.simon816.migrator.sponge.SpongeTargetContext;

public interface Action {

    void runAction(SpongeTargetContext sponge) throws Exception;

}
