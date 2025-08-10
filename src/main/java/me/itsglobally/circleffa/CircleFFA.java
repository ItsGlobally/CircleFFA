package me.itsglobally.circleffa;

import me.itsglobally.circleffa.commands.ffa;
import me.itsglobally.circleffa.commands.setLayout;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class CircleFFA extends JavaPlugin {

    File layoutFile;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        getServer().getPluginManager().registerEvents(new events(), this);
        getCommand("setLayout").setExecutor(new setLayout());
        getCommand("setLayout").setTabCompleter(new setLayout());
        getCommand("ffa").setExecutor(new ffa());
        getCommand("ffa").setTabCompleter(new ffa());
        data.setInstance(this);
        data.setPlugin(this);
        data.addMap(new Location(Bukkit.getWorld("ffa"), 0.5, 201, 0.5));
        data.addMap(new Location(Bukkit.getWorld("ffa"), 1000.5, 201, 1000.5));
        data.addMap(new Location(Bukkit.getWorld("ffa"), 1000.5, 201, 0.5));

        data.setCurmap(data.getRandomMap());
        utils.changeMap();
        utils.startMapRotation();

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        layoutFile = new File(getDataFolder(), "data.yml");
        if (!layoutFile.exists()) {
            saveResource(layoutFile.getAbsolutePath(), false);
        }

        data.loadLayouts(layoutFile);


        circleCoreApiUtils.setChatHandleByCore(false);


        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    utils.updateScoreBoard(p.getUniqueId());
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

