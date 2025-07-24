package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.data;
import me.itsglobally.circleffa.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class kms implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return true;
        }
        utils.spawn(p.getUniqueId());
        UUID klr = data.getLastHit(p.getUniqueId());
        if (klr == null) {
            return true;
        }
        utils.handleKill(p.getUniqueId(), klr);
        return true;
     }
}
