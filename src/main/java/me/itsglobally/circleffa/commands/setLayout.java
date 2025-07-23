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
        String block = strings[0];
        int e;
        try {
            e = Integer.parseInt(strings[1]);
        } catch (NumberFormatException ex) {
            p.sendMessage("§c/setLayout (block|sword|tool) (0-8)");
            return true;
        }
        switch (e) {
            case 1,2,3,4,5,6,7,8,9,0:
                break;
            default:
                p.sendMessage("§c/setLayout (block|sword|tool) (0-8)");
                return true;
        }
        Audience ap = utils.getAudience(p);
        switch (block) {
            case "block", "sword", "tool":
                data.setLayout(p.getUniqueId(), block, e);
                ap.sendActionBar(Component.text("§7Set your " + block + " to slot " + e));
                break;
            default:
                p.sendMessage("§c/setLayout (block|sword|tool) (0-8)");
                break;
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
        if (strings.length > 2) {
            return List.of("ni_sha_b_ba");
        }
        return List.of();
    }
}
