package me.itsglobally.circleffa;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class events implements Listener {
    @EventHandler
    public void blockplace(BlockPlaceEvent e) {
        if (data.getBm(e.getPlayer().getUniqueId())) return;
        if (e.getBlockPlaced().getLocation().getY() < 195 && e.getBlockPlaced().getLocation().getY() >= 175) {
            data.addPlacedBlock(e.getPlayer().getUniqueId(), e.getBlockPlaced().getLocation());
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getBlockPlaced().setType(Material.AIR);
                    data.removePlacedBlock(e.getPlayer().getUniqueId(), e.getBlockPlaced().getLocation());
                }
            }.runTaskLater(data.getPlugin(), 5 * 20L);
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void blockbreak(BlockBreakEvent e) {
        if (data.getBm(e.getPlayer().getUniqueId())) return;

        if (e.getBlock().getLocation().getY() < 195 && e.getBlock().getLocation().getY() > 175) {
            if (e.getBlock().getType() != Material.SANDSTONE) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().getY() < 150) {
            if (data.getBm(e.getPlayer().getUniqueId())) return;
            utils.spawn(e.getPlayer().getUniqueId());
            UUID lastHit = data.getLastHit(e.getPlayer().getUniqueId());
            if (lastHit != null) {
                utils.handleKill(e.getPlayer().getUniqueId(), lastHit);
            }
        }
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if ((e.getEntity().getLocation().getY() > 200) || (e.getDamager().getLocation().getY() > 200)) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player p)) {
            e.setCancelled(true);
            return;
        }
        Player damager = null;

        if (e.getDamager() instanceof Player p1) {
            damager = p1;
        } else if (e.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter) {
            damager = shooter;
        }

        if (damager != null) {
            if (p != damager) data.setLastHit(p.getUniqueId(), damager.getUniqueId());
            if (p.getHealth() <= e.getFinalDamage()) {
                e.setCancelled(true);
                utils.spawn(p.getUniqueId());
                utils.handleKill(p.getUniqueId(), damager.getUniqueId());
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
        // utils.getAudience(e.getPlayer()).sendPlayerListHeaderAndFooter(Component.text("§dCircle Network!\n§bYou are playing on §dCircle FFA!\n"), Component.text("\n§bitsglobally.top"));
        data.setPlayerGamemode(e.getPlayer().getUniqueId(), "LOBBY");
        utils.spawn(e.getPlayer().getUniqueId());
        data.setks(e.getPlayer().getUniqueId(), 0L);
        data.setLastHit(e.getPlayer().getUniqueId(), null);
        data.initBlock(e.getPlayer().getUniqueId());

    }

    @EventHandler
    public void playerleave(PlayerQuitEvent e) {
        UUID lastHit = data.getLastHit(e.getPlayer().getUniqueId());
        if (lastHit != null) {
            utils.handleKill(e.getPlayer().getUniqueId(), lastHit);
        }
        for (Location l : data.getPlacedBlock(e.getPlayer().getUniqueId())) {
            l.getBlock().setType(Material.AIR);
        }
        data.removePlacedBlocks(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemUse(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (p.getLocation().getY() > 200) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (e.getPlayer().getLocation().getY() > 200) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Bukkit.broadcastMessage("§7[" + MongoStatUtil.getStars(e.getPlayer().getUniqueId()) + "✫] §r" + e.getPlayer().getDisplayName() + " §r» " + e.getMessage());
    }
}
