package com.simon816.migrator.sponge;

import java.io.IOException;

public class Files2 {

    public static void begForgiveness(IORunnable runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            System.err.println("Forgiven");
            e.printStackTrace();
        }
    }

    public static interface IORunnable {

        void run() throws IOException;
    }

}
