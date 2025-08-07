package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.MongoStatUtil;
import me.itsglobally.circleffa.data;
import me.itsglobally.circleffa.utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ffa implements CommandExecutor, TabCompleter {

    public static void kms(Player p) {
        utils.spawn(p.getUniqueId());
        UUID klr = data.getLastHit(p.getUniqueId());
        if (klr == null) {
            return;
        }
        utils.handleKill(p.getUniqueId(), klr);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return true;
        }
        if (strings.length < 1) {
            p.sendMessage("§c/ffa [join/leave/kms/togglebm/tbm/statsreset/setxp/setstar]");
            return true;
        }
        if (!s.equals("ffa")) {
            switch (s) {
                case "kms" -> {
                    kms(p);
                    return true;
                }
            }
            return true;
        }
        switch (strings[0]) {
            case "leave" -> {
                utils.joinLobby(p.getUniqueId());
            }
            case "join" -> {
                utils.joinFFA(p.getUniqueId());
            }
            case "changemap" -> {
                utils.changeMap();
                utils.startMapRotation();
                commandSender.sendMessage("§aChanged map.");
                return true;
            }
            case "setxp" -> {
                if (!p.hasPermission("circleffa.setxp")) {
                    p.sendMessage("§cYou do not have permission to do that!");
                    return true;
                }
                if (strings.length < 3) {
                    commandSender.sendMessage("§c/ffa setStar [player] [XP]");
                    return true;
                }
                Player p2 = Bukkit.getPlayerExact(strings[1]);
                if (p2 == null) {
                    commandSender.sendMessage("§cPlayer not found.");
                    return true;
                }
                long star;
                try {
                    star = Long.parseLong(strings[2]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage("§cEnter a vaild number.");
                    return true;
                }
                MongoStatUtil.setXp(p.getUniqueId(), star);
                return true;
            }
            case "setstar" -> {
                if (!p.hasPermission("circleffa.setstar")) {
                    p.sendMessage("§cYou do not have permission to do that!");
                    return true;
                }
                if (strings.length < 3) {
                    commandSender.sendMessage("§c/ffa setStar [player] [star]");
                    return true;
                }
                Player p2 = Bukkit.getPlayerExact(strings[1]);
                if (p2 == null) {
                    commandSender.sendMessage("§cPlayer not found.");
                    return true;
                }
                long star;
                try {
                    star = Long.parseLong(strings[2]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage("§cEnter a vaild number.");
                    return true;
                }
                MongoStatUtil.setStars(p.getUniqueId(), star);
                return true;
            }
            case "kms" -> {
                kms(p);
                return true;
            }
            case "statsreset" -> {
                if (!p.hasPermission("circleffa.statsreset")) {
                    p.sendMessage("§cYou do not have permission to do that!");
                    return true;
                }
                if (strings.length < 2) {
                    commandSender.sendMessage("§c/ffa statsreset [player]");
                    return true;
                }
                Player p2 = Bukkit.getPlayerExact(strings[1]);
                if (p2 == null) {
                    commandSender.sendMessage("§cPlayer not found");
                    return true;
                }
                UUID u = p2.getUniqueId();
                data.setks(u, 0L);
                MongoStatUtil.setDies(u, 0L);
                MongoStatUtil.setKills(u, 0L);
                MongoStatUtil.setXp(u, 0L);
                MongoStatUtil.setStars(u, 0L);
                commandSender.sendMessage("§aYou've reset " + p2.getDisplayName() + "§a's stats");
                return true;
            }
            case "togglebm", "tbm" -> {
                if (!p.hasPermission("circleffa.togglebm")) {
                    p.sendMessage("§cYou do not have permission to do that!");
                    return true;
                }
                data.toggleBm(p.getUniqueId());
                utils.getAudience(p).sendActionBar(Component.text("§7Set your build mode to " + data.getBm(p.getUniqueId())));
                return true;
            }
            default -> {
                p.sendMessage("§c/ffa [join/leave/kms/togglebm/tbm/statsreset/setxp/setstar]");
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return List.of();
        }
        int at = strings.length;
        if (at == 1) {
            if (p.hasPermission("circleffa.admin")) {
                return List.of("join", "leave", "kms", "togglebm", "tbm", "statsreset", "setxp", "setstar");
            }
            return List.of("join", "leave", "kms");
        }
        switch (strings[0]) {
            case "statsreset" -> {
                final List<String> list = new ArrayList<>();
                for (Player op : Bukkit.getOnlinePlayers()) {
                    list.add(op.getName());
                }
                return list;
            }
            case "setxp", "setstar" -> {
                if (at == 3) {
                    return List.of("Input_numbers_you_want_to_set");
                }
                final List<String> list = new ArrayList<>();
                for (Player op : Bukkit.getOnlinePlayers()) {
                    list.add(op.getName());
                }
                return list;
            }
        }
        return List.of();
    }
}
