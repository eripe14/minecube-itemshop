package com.eripe14.itemshop.hook;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Logger;

public class HookManager {

    private final Server server;
    private final Logger logger;

    public HookManager(Server server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    public void initialize(Hook hook) {
        PluginManager pluginManager = this.server.getPluginManager();

        if (pluginManager.isPluginEnabled(hook.pluginName())) {
            hook.initialize();

            this.logger.info("Hooked into " + hook.pluginName());
        }
    }

}