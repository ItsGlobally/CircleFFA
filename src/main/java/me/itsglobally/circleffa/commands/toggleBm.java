package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.data;
import me.itsglobally.circleffa.utils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class toggleBm implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return true;
        }
        data.toggleBm(p.getUniqueId());
        utils.getAudience(p).sendActionBar(Component.text("set ur bm to " + data.getBm(p.getUniqueId())));
        return true;
    }
}
