package me.itsglobally.circleffa;

import me.itsglobally.circleffa.commands.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class CircleFFA extends JavaPlugin {

    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        getServer().getPluginManager().registerEvents(new events(), this);
        getCommand("setLayout").setExecutor(new setLayout());
        getCommand("setLayout").setTabCompleter(new setLayout());
        getCommand("kms").setExecutor(new kms());
        getCommand("toggleBm").setExecutor(new toggleBm());
        data.setInstance(this);
        data.setPlugin(this);
        data.addMap(new Location(Bukkit.getWorld("ffa"), 0, 200, 0));
        data.setCurmap(data.getRandomMap());
        new BukkitRunnable() {
            @Override
            public void run() {
                data.setCurmap(data.getCurmap());
                Bukkit.broadcastMessage("map change");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    utils.handleKill(p.getUniqueId(), data.getLastHit(p.getUniqueId()));
                    utils.spawn(p.getUniqueId());
                }
            }
        }.runTaskTimer(this, 0L, 600L * 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BukkitAudiences adventure() {
        return this.adventure;
    }
}
