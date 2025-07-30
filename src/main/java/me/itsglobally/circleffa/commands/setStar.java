package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.starUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setStar implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage("§c/setStar (player)");
            return true;
        }
        Player p = Bukkit.getPlayerExact(strings[0]);
        if (p == null) {
            commandSender.sendMessage("§cPlayer not found.");
            return true;
        }
        long star;
        try {
            star = Long.parseLong(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage("§cEnter a vaild number.");
            return true;
        }
        starUtils.setStar(p.getUniqueId(), star);
        return true;
    }
}
