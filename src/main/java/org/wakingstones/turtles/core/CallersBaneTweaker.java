package org.wakingstones.turtles.core;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import static org.spongepowered.asm.mixin.MixinEnvironment.Side.SERVER;

public class CallersBaneTweaker implements ITweaker {
    private List<String> args;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = args;
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader loader) {
        configureLaunchClassLoader(loader);

        // Configure class transformers
        configureMixinEnvironment();

        Logger.getGlobal().info("Initialization finished. Starting server...");
    }

    @Override
    public String getLaunchTarget() {
        return "platform.Server";
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[0]);
    }

    private static void configureLaunchClassLoader(LaunchClassLoader loader) {
    }

    private static void configureMixinEnvironment() {
        Logger.getGlobal().info("Initializing Mixin environment...");

        MixinBootstrap.init();

        // Register common mixin configurations
        Mixins.addConfiguration("mixins.common.core.json");

        MixinEnvironment.getDefaultEnvironment().setSide(SERVER);
    }
}
