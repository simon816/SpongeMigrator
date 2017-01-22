package com.simon816.migrator.task;

import com.simon816.migrator.sponge.SpongeConfigAccessor;

import java.util.function.Consumer;

public class Actions {

    public static Action withConfig(Consumer<SpongeConfigAccessor> configConsumer) {
        return sponge -> configConsumer.accept(sponge.getConfig());
    }

    public static Action raiseIssue(String message, Operation... operations) {
        return (IssueAction) sponge -> {
            System.out.println(message);
        };
    }

    public static Action afterIssues(Action action) {
        return sponge -> {
            if (ActionGraph.hasAction(a -> a instanceof IssueAction)) {
                ActionGraph.add(afterIssues(action));
            } else {
                action.runAction(sponge);
            }
        };
    }

    private static interface IssueAction extends Action {
    }

}
