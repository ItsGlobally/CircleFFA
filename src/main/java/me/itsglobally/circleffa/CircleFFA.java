package me.itsglobally.circleffa;

import org.bukkit.plugin.java.JavaPlugin;

public final class CircleFFA extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new events(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
