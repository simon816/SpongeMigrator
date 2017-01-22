package com.simon816.migrator;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class FileClaims {

    private static Set<Path> unclaimed = new HashSet<>();

    public static void unclaimed(Path path) {
        path = path.toAbsolutePath();
//        System.out.println("UNCLAIMED " + path);
        unclaimed.add(path);
    }

    public static void claim(Path path) {
        path = path.toAbsolutePath();
//        System.out.println("CLAIM " + path);
        unclaimed.remove(path);
    }

    public static void showUnclaimed() {
        for (Path path : unclaimed) {
            System.out.println(path);
        }
    }

}
