package com.simon816.migrator.task;

import com.simon816.migrator.sponge.SpongeTargetContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ActionGraph {

    private static final List<Action> actions = new ArrayList<>();
    private static List<Action> live;

    public static void add(Action action) {
        actions.add(action);
    }

    public static void mainLoop(SpongeTargetContext target) throws Exception {
        while (!actions.isEmpty()) {
            List<Action> current = new ArrayList<>(actions);
            actions.clear();
            live = current;
            for (Action action : current) {
                try {
                    action.runAction(target);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            live = null;
        }
    }

    public static boolean hasAction(Predicate<Action> predicate) {
        if (live != null) {
            for (Action action : live) {
                if (predicate.test(action)) {
                    return true;
                }
            }
        }
        for (Action action : actions) {
            if (predicate.test(action)) {
                return true;
            }
        }
        return false;
    }

}
