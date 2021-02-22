package com.rock_mc.invitation_system;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Log.logger = getLogger();

        Log.server("Enable", "Active");

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand("invits").setExecutor(new PlayerCommand());

        Log.server("Enable", "Complete");
    }
}
