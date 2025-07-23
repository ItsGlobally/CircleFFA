package me.itsglobally.circleffa;

import me.itsglobally.circleffa.commands.setLayout;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class CircleFFA extends JavaPlugin {

    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        getServer().getPluginManager().registerEvents(new events(), this);
        getCommand("setLayout").setExecutor(new setLayout());
        getCommand("setLayout").setTabCompleter(new setLayout());
        data.setInstance(this);
        data.setPlugin(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BukkitAudiences adventure() {
        return this.adventure;
    }
}
