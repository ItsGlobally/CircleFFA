package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ffa implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return true;
        }
        if (strings.length < 1) {
            p.sendMessage("§c/ffa (join/leave)");
            return true;
        }
        switch (strings[0]) {
            case "leave" -> {
                utils.joinLobby(p.getUniqueId());
            }
            case "join" -> {
                utils.joinFFA(p.getUniqueId());
            }
            default -> {
                p.sendMessage("§c/ffa (join/leave)");
                return true;
            }
        }
        return true;
    }
}
