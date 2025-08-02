package me.itsglobally.circleffa.commands;

import me.itsglobally.circleffa.MongoStatUtil;
import me.itsglobally.circleffa.data;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class statsreset implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage("§c/statsreset (player)");
            return true;
        }
        Player p2 = Bukkit.getPlayerExact(strings[0]);
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
        commandSender.sendMessage("§aYou've reseted " + p2.getDisplayName() + "§a's stats");
        return true;
    }
}
