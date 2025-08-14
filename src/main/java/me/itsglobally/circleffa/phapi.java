package me.itsglobally.circleffa;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class phapi extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "ItsGlobally";
    }

    @Override
    public String getIdentifier() {
        return "CircleFFA";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        List<String> requireOnline = List.of(
                "kills",
                "deaths",
                "ks",
                "xp",
                "stars"
        );
        if (requireOnline.contains(params)) {
            if (!(player instanceof Player p)) {
                return "";
            }
            return rqOp(p, params);
        } else {
            return nrqOp(player, params);
        }
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        return onRequest(player, params);
    }
    public static String rqOp(Player p, String params) {
        UUID u = p.getUniqueId();
        switch (params) {
            case "kills" -> {
                return String.valueOf(MongoStatUtil.getKills(u));
            }
            case "deaths" -> {
                return String.valueOf(MongoStatUtil.getDies(u));
            }
            case "ks" -> {
                return String.valueOf(data.getks(u));
            }
            case "xp" -> {
                return String.valueOf(MongoStatUtil.getXp(u));
            }
            case "stars" -> {
                return String.valueOf(MongoStatUtil.getStars(u));
            }
            default -> {
                return "";
            }
        }
    }
    public static String nrqOp(OfflinePlayer p, String params) {
        switch (params) {
            default -> {
                return "";
            }
        }
    }

}

