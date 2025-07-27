package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class changeMap implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        utils.changeMap();
        utils.startMapRotation();
        commandSender.sendMessage("§aChanged map.");
        return true;
    }
}
