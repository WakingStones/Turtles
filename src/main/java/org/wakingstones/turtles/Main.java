package org.wakingstones.turtles;

import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.launch.MixinBootstrap;

public class Main {

    public static void main(String[] args) throws Exception {

        String[] a = new String[] {
                "--tweakClass","org.wakingstones.turtles.Tweaker"
        };

        Launch.main(a);

        MixinBootstrap.init();

        //Server.main(args);
    }
}
