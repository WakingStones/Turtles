package org.wakingstones.turtles.core;

import net.minecraft.launchwrapper.Launch;

import java.io.File;

public class Main {

    private static final String[] DUPE_LIBRARIES = new String[]{
            "gson-2.2.3.jar",
            "guava-15.0.jar"
    };

    public static void main(String[] args) {
        String[] a = new String[]{
                "--tweakClass", "org.wakingstones.turtles.core.CallersBaneTweaker"
        };

        for (String s : DUPE_LIBRARIES) {
            new File("lib", s).delete();
        }

        Launch.main(a);
    }
}
