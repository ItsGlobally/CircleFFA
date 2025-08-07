package me.itsglobally.circleffa;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class data {
    private static final HashMap<UUID, Boolean> bm = new HashMap<>();
    private static final HashMap<UUID, Integer> block = new HashMap<>();
    private static final HashMap<UUID, Integer> sword = new HashMap<>();
    private static final HashMap<UUID, Integer> tool = new HashMap<>();
    private static final HashMap<UUID, Integer> bow = new HashMap<>();
    private static final HashMap<UUID, Integer> arrow = new HashMap<>();
    private static final HashMap<UUID, Integer> pearl = new HashMap<>();
    private static JavaPlugin plugin;
    private static CircleFFA instance;

    public static void toggleBm(UUID p) {
        bm.put(p, !bm.getOrDefault(p, false));
    }

    public static Boolean getBm(UUID p) {
        return bm.getOrDefault(p, false);
    }

    public static void setLayout(UUID p, String b, Integer e) {
        switch (b) {
            case "block" -> block.put(p, e);
            case "sword" -> sword.put(p, e);
            case "tool" -> tool.put(p, e);
            case "bow" -> bow.put(p, e);
            case "arrow" -> arrow.put(p, e);
            case "pearl" -> pearl.put(p, e);
        }
    }
    public static void saveLayouts(File file) {
        FileConfiguration config = new YamlConfiguration();

        for (UUID uuid : sword.keySet()) {
            String path = "layouts." + uuid.toString();
            config.set(path + ".sword", sword.get(uuid));
        }
        for (UUID uuid : block.keySet()) {
            config.set("layouts." + uuid + ".block", block.get(uuid));
        }
        for (UUID uuid : tool.keySet()) {
            config.set("layouts." + uuid + ".tool", tool.get(uuid));
        }
        for (UUID uuid : bow.keySet()) {
            config.set("layouts." + uuid + ".bow", bow.get(uuid));
        }
        for (UUID uuid : arrow.keySet()) {
            config.set("layouts." + uuid + ".arrow", arrow.get(uuid));
        }
        for (UUID uuid : pearl.keySet()) {
            config.set("layouts." + uuid + ".pearl", pearl.get(uuid));
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer getLayout(UUID p, String b) {
        switch (b) {
            case "block" -> {
                return block.getOrDefault(p, 3);
            }
            case "sword" -> {
                return sword.getOrDefault(p, 0);
            }
            case "tool" -> {
                return tool.getOrDefault(p, 1);
            }
            case "bow" -> {
                return bow.getOrDefault(p, 2);
            }
            case "arrow" -> {
                return arrow.getOrDefault(p, 4);
            }
            case "pearl" ->{
                return pearl.getOrDefault(p, 5);
            }
        }
        return null;
    }
    public static void loadLayouts(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection layouts = config.getConfigurationSection("layouts");
        if (layouts == null) return;

        for (String uuidString : layouts.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            ConfigurationSection section = layouts.getConfigurationSection(uuidString);
            if (section == null) continue;

            if (section.contains("sword")) sword.put(uuid, section.getInt("sword"));
            if (section.contains("block")) block.put(uuid, section.getInt("block"));
            if (section.contains("tool")) tool.put(uuid, section.getInt("tool"));
            if (section.contains("bow")) bow.put(uuid, section.getInt("bow"));
            if (section.contains("arrow")) arrow.put(uuid, section.getInt("arrow"));
            if (section.contains("pearl")) pearl.put(uuid, section.getInt("pearl"));
        }
    }
    public static void loadKills(File starFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(starFile);
        if (!config.isConfigurationSection("kills")) return;

        for (String key : config.getConfigurationSection("kills").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                long value = config.getLong("kills." + key);
                kills.put(uuid, value);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID in kills.yml: " + key);
            }
        }
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

    private static final HashMap<UUID, Long> ks = new HashMap<>();
    public static void setks(UUID p, Long k) {
        ks.put(p, k);
    }
    public static void addks(UUID p) {
        ks.put(p, ks.getOrDefault(p, 0L) + 1);
    }
    public static Long getks(UUID p) { return ks.getOrDefault(p, 0L); }
    private static final HashMap<UUID, UUID> lastHit = new HashMap<>();
    public static void setLastHit(UUID p, UUID tg) {
        lastHit.put(p, tg);
    }
    @Nullable
    public static UUID getLastHit(UUID p) {
        return lastHit.get(p);
    }

    private static HashMap<UUID, List<Location>> blocks = new HashMap<>();
    public static void initBlock(UUID p) {
        List<Location> e = new ArrayList<>();
        e.add(new Location(Bukkit.getWorld("ffa"), 0, 0, 0));
        blocks.put(p, e);
    }
    public static void addPlacedBlock(UUID p, Location l) {
        final List<Location> e = blocks.get(p);
        e.add(l);
        blocks.put(p, e);
    }
    public static List<Location> getPlacedBlock(UUID p) {
        return blocks.get(p);
    }
    public static void removePlacedBlocks(UUID p) {
        blocks.remove(p);
    }
    public static void removePlacedBlock(UUID p, Location l) {
        List<Location> e = blocks.get(p);
        if (e != null) {
            e.remove(l);
        }
    }
    private static final List<Location> maps = new ArrayList<>();

    public static void addMap(Location l) {
        maps.add(l);
    }
    public static Location getRandomMap() {
        return maps.get(new Random().nextInt(maps.size()));
    }
    private static Location curmap;
    public static void setCurmap(Location l) {
        curmap = l;
    }

    public static Location getCurmap() {
        return curmap;
    }

    private static final HashMap<UUID, Long> kills = new HashMap<>();
    public static void addKill(UUID p, Long l) {
        kills.put(p, kills.getOrDefault(p, 0L) + l);
    }
    public static Long getKill(UUID p) {
        return kills.getOrDefault(p, 0L);
    }
    public static void setKill(UUID p, Long l) {
        kills.put(p, l);
    }
    private static final HashMap<UUID, Long> dies = new HashMap<>();
    public static void addDies(UUID p) {
        dies.put(p, dies.getOrDefault(p, 0L) + 1);
    }
    public static Long getDies(UUID p) {
        return dies.getOrDefault(p, 0L);
    }
    public static void setDies(UUID p, Long l) {
        dies.put(p, l);
    }

    private static final HashMap<UUID, String> pgm = new HashMap<>();
    /*
    LOBBY
    KBFFA
     */
    public static void setPlayerGamemode(UUID u, String g) {
        pgm.put(u, g);
    }

    public static String getPlayerMode(UUID u) {
        return pgm.getOrDefault(u, "LOBBY");
    }


}
