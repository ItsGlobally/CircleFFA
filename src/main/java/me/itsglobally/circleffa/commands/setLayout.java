package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.data;
import me.itsglobally.circleffa.utils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class setLayout implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return true;
        }

        if (strings.length < 2) {
            p.sendMessage("§c/setLayout (block|sword|tool) (0-8)");
            return true;
        }

        String block = strings[0];
        int e;
        try {
            e = Integer.parseInt(strings[1]);
        } catch (NumberFormatException ex) {
            p.sendMessage("§c/setLayout (block|sword|tool) (1-9)");
            return true;
        }

        if (e < 0 || e > 8) {
            p.sendMessage("§c/setLayout (block|sword|tool) (1-9)");
            return true;
        }

        Audience ap = utils.getAudience(p);
        if (block.equalsIgnoreCase("block") || block.equalsIgnoreCase("sword") || block.equalsIgnoreCase("tool") || block.equalsIgnoreCase("bow") || block.equalsIgnoreCase("arrow")) {
            data.setLayout(p.getUniqueId(), block.toLowerCase(), e - 1);
            ap.sendActionBar(Component.text("Set your " + block + " to slot " + e)); // or use MiniMessage
        } else {
            p.sendMessage("§c/setLayout (block|sword|tool) (1-9)");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return List.of("block", "sword", "tool");
        }
        if (strings.length == 2) {
            return List.of("0", "1", "2", "3", "4", "5", "6", "7", "8");
        }
        return List.of();
    }
}
