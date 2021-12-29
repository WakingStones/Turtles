package org.wakingstones.turtles.core;

import cpw.mods.modlauncher.Launcher;

import java.io.File;

public class Main {

    private static final String[] DUPE_LIBRARIES = new String[]{
            "gson-2.2.3.jar",
            "guava-15.0.jar"
    };

    public static void main(String[] args) {
        String[] a = new String[]{
                "--launchTarget", "turtle"
        };

        for (String s : DUPE_LIBRARIES) {
            new File("lib", s).delete();
        }

        Launcher.main(a);
    }
}
