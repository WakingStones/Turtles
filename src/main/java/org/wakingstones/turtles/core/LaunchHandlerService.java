package org.wakingstones.turtles.core;

import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import cpw.mods.modlauncher.api.ITransformingClassLoaderBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.jar.Manifest;

import static org.spongepowered.asm.mixin.MixinEnvironment.Side.SERVER;

public class LaunchHandlerService implements ILaunchHandlerService {
    @Override
    public String name() {
        return "turtle";
    }

    private static final String JAVA_HOME_PATH = System.getProperty("java.home");
    protected final Logger logger = LogManager.getLogger("launch");

    @Override
    public void configureTransformationClassLoader(final ITransformingClassLoaderBuilder builder) {
    }

    @Override
    public Callable<Void> launchService(String[] arguments, ITransformingClassLoader launchClassLoader) {
        return () -> {
            logger.info("Initializing Mixin environment...");

            // Register common mixin configurations
            Mixins.addConfiguration("mixins.common.core.json");
            MixinEnvironment.getDefaultEnvironment().setSide(SERVER);

            Method meth = Class.forName("platform.Server", true, launchClassLoader.getInstance())
                    .getMethod("main", String[].class);
            meth.invoke(null, (Object) arguments);
            return null;
        };

    }
}
