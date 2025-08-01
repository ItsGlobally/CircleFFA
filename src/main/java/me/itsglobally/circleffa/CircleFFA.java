package me.itsglobally.circleffa;

import me.itsglobally.circleffa.commands.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class CircleFFA extends JavaPlugin {

    private BukkitAudiences adventure;

    File layoutFile;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        getServer().getPluginManager().registerEvents(new events(), this);
        getCommand("setLayout").setExecutor(new setLayout());
        getCommand("setLayout").setTabCompleter(new setLayout());
        getCommand("kms").setExecutor(new kms());
        getCommand("toggleBm").setExecutor(new toggleBm());
        getCommand("changeMap").setExecutor(new changeMap());
        getCommand("statsreset").setExecutor(new statsreset());
        getCommand("setStar").setExecutor(new setStar());
        getCommand("setXp").setExecutor(new setXp());
        data.setInstance(this);
        data.setPlugin(this);
        data.addMap(new Location(Bukkit.getWorld("ffa"), 0.5, 201, 0.5));
        data.addMap(new Location(Bukkit.getWorld("ffa"), 1000.5, 201, 1000.5));

        data.setCurmap(data.getRandomMap());
        utils.changeMap();

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        layoutFile = new File(getDataFolder(), "data.yml");
        if (!layoutFile.exists()) {
            saveResource(layoutFile.getAbsolutePath(), false);
        }



        circleCoreApiUtils.setChatHandleByCore(false);


        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    utils.updateScoreBorad(p.getUniqueId());
                }
            }
        }.runTaskTimer(this, 0, 20);

    }

    @Override
    public void onDisable() {
        data.saveLayouts(layoutFile);
    }

    public BukkitAudiences adventure() {
        return this.adventure;
    }
}

