package me.itsglobally.circleffa;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class data {
    private static final HashMap<UUID, Boolean> bm = new HashMap<>();
    private static final HashMap<UUID, Integer> block = new HashMap<>();
    private static final HashMap<UUID, Integer> sword = new HashMap<>();
    private static final HashMap<UUID, Integer> tool = new HashMap<>();
    private static JavaPlugin plugin;
    private static CircleFFA instance;

    public static void setBm(UUID p) {
        bm.put(p, !bm.get(p));
    }

    public static Boolean getBm(UUID p) {
        return bm.get(p);
    }

    public static void setLayout(UUID p, String b, Integer e) {
        switch (b) {
            case "block" -> block.put(p, e);
            case "sword" -> sword.put(p, e);
            case "tool" -> tool.put(p, e);
        }
    }

    public static Integer getLayout(UUID p, String b) {
        switch (b) {
            case "block" -> {
                return block.getOrDefault(p, 3);
            }
            case "sword" -> {
                return sword.getOrDefault(p, 1);
            }
            case "tool" -> {
                return tool.getOrDefault(p, 2);
            }
        }
        return null;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void setPlugin(JavaPlugin plugin) {
        data.plugin = plugin;
    }

    public static CircleFFA getInstance() {
        return instance;
    }

    public static void setInstance(CircleFFA instance) {
        data.instance = instance;
    }

    private static final HashMap<UUID, Integer> ks = new HashMap<>();
    public static void setks(UUID p, Integer k) {
        ks.put(p, k);
    }
    public static void addks(UUID p) {
        ks.put(p, ks.get(p) + 1);
    }
}
