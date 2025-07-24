package me.itsglobally.circleffa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class events implements Listener {
    @EventHandler
    public void blockplace(BlockPlaceEvent e) {
        if (data.getBm(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void blockbreak(BlockBreakEvent e) {
        if (data.getBm(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        if ((e.getEntity().getLocation().getY() > 200) || (e.getDamager().getLocation().getY() > 200)) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player p)) {
            e.setCancelled(true);
            return;
        }
        if (e.getDamager() instanceof Player dmgere) {
            data.setLastHit(p.getUniqueId(), dmgere.getUniqueId());
            if (p.getHealth() < e.getFinalDamage()) {
                e.setCancelled(true);
                utils.spawn(p.getUniqueId());
                utils.handleKill(p.getUniqueId(), dmgere.getUniqueId());
                data.setLastHit(p.getUniqueId(), null);
            }
        }
    }

    @EventHandler
    public void hunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void regen(EntityRegainHealthEvent e) {
        if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void died(PlayerDeathEvent e) {
        e.getEntity().spigot().respawn();
        utils.spawn(e.getEntity().getUniqueId());
    }
    @EventHandler
    public void playerjoin(PlayerJoinEvent e) {
        utils.spawn(e.getPlayer().getUniqueId());
        data.setks(e.getPlayer().getUniqueId(), 0);
        Bukkit.broadcastMessage("[+] " + e.getPlayer().getDisplayName());
        data.setLastHit(e.getPlayer().getUniqueId(), null);
    }

    @EventHandler
    public void playerleave(PlayerQuitEvent e) {
        Bukkit.broadcastMessage("[-] " + e.getPlayer().getDisplayName());
        UUID lastHit = data.getLastHit(e.getPlayer().getUniqueId());
        if (lastHit != null) {
            utils.handleKill(e.getPlayer().getUniqueId(), lastHit);
        }
    }

}
