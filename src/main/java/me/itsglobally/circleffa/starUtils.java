package me.itsglobally.circleffa;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class starUtils {
    private static final HashMap<UUID, Long> stars = new HashMap<>();
    private static final HashMap<UUID, Long> xp = new HashMap<>();

    public static void setStar(UUID u, Long l) {
        stars.put(u, l);
    }

    public static void addStar(UUID u, Long l) {
        stars.put(u, stars.getOrDefault(u, 0L) + l);
    }

    public static Long getStar(UUID u) {
        return stars.getOrDefault(u, 0L);
    }

    public static void loadStar(File starFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(starFile);
        if (!config.isConfigurationSection("stars")) return;

        for (String key : config.getConfigurationSection("stars").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                long value = config.getLong("stars." + key);
                stars.put(uuid, value);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID in stars.yml: " + key);
            }
        }
    }

    public static void saveStar(File starFile) {
        FileConfiguration config = new YamlConfiguration();

        for (UUID uuid : stars.keySet()) {
            config.set("stars." + uuid.toString(), stars.get(uuid));
        }

        try {
            config.save(starFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setXp(UUID u, Long l) {
        xp.put(u, l);
        handleXpOverflow(u);
    }

    public static void addXp(UUID u, Long l) {
        xp.put(u, xp.getOrDefault(u, 0L) + l);
        handleXpOverflow(u);
    }


    public static Long getXp(UUID u) {
        return xp.getOrDefault(u, 0L);
    }

    public static void loadXp(File starFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(starFile);
        if (!config.isConfigurationSection("xp")) return;

        for (String key : config.getConfigurationSection("xp").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                long value = config.getLong("xp." + key);
                xp.put(uuid, value);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID in stars.yml: " + key);
            }
        }
    }

    public static void saveXp(File starFile) {
        FileConfiguration config = new YamlConfiguration();

        for (UUID uuid : xp.keySet()) {
            config.set("xp." + uuid.toString(), xp.get(uuid));
        }

        try {
            config.save(starFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleXpOverflow(UUID u) {
        long currentXp = xp.getOrDefault(u, 0L);
        if (currentXp >= 100) {
            long starsToAdd = currentXp / 100;
            long remainingXp = currentXp % 100;

            addStar(u, starsToAdd);
            xp.put(u, remainingXp);
        }
    }

}
