package me.itsglobally.circleffa;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class utils {
    public static void spawn(UUID u) {
        Player p = Bukkit.getPlayer(u);
        if (p == null) return;

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        p.teleport(data.getCurmap());
        p.setHealth(20.0);
        p.setFoodLevel(20);


        PlayerInventory inv = p.getInventory();
        inv.setHelmet(armor(new ItemStack(Material.LEATHER_HELMET)));
        inv.setChestplate(armor(new ItemStack(Material.LEATHER_CHESTPLATE)));
        inv.setLeggings(armor(new ItemStack(Material.LEATHER_LEGGINGS)));
        inv.setBoots(armor(new ItemStack(Material.LEATHER_BOOTS)));
        inv.setItem(data.getLayout(u, "sword"), sword(new ItemStack(Material.WOOD_SWORD)));
        inv.setItem(data.getLayout(u, "block"), new ItemStack(Material.SANDSTONE, 64));
        inv.setItem(data.getLayout(u, "tool"), tool(new ItemStack(Material.STONE_PICKAXE)));
        inv.setItem(data.getLayout(u, "bow"), bow(new ItemStack(Material.BOW)));
        inv.setItem(data.getLayout(u, "arrow"), new ItemStack(Material.ARROW, 16));
        inv.setItem(data.getLayout(u, "pearl"), new ItemStack(Material.ENDER_PEARL));
    }

    private static ItemStack armor(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        LeatherArmorMeta lam = (LeatherArmorMeta) im;
        lam.spigot().setUnbreakable(true);
        lam.setColor(Color.fromBGR(255, 229, 255));
        is.setItemMeta(lam);
        is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        is.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
        return is;
    }

    private static ItemStack sword(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        return is;
    }

    private static ItemStack bow(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        return is;
    }

    private static ItemStack tool(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
        return is;
    }

    public static Audience getAudience(Player p) {
        return data.getInstance().adventure().player(p);
    }

    public static void handleKill(UUID pu, UUID klru) {
        Player p = Bukkit.getPlayer(pu);
        Player klr = Bukkit.getPlayer(klru);

        if (klr == null) return;

        // Give 64 sandstone to killer
        klr.getInventory().setItem(
                data.getLayout(klru, "block"),
                new ItemStack(Material.SANDSTONE, 64)
        );
        klr.getInventory().setItem(
                data.getLayout(klru, "arrow"),
                new ItemStack(Material.ARROW, 16)
        );

        // Handle ender pearl increment safely
        int pearlSlot = data.getLayout(klr.getUniqueId(), "pearl");
        ItemStack pearlItem = klr.getInventory().getItem(pearlSlot);
        int newAmount = (pearlItem != null && pearlItem.getType() == Material.ENDER_PEARL)
                ? Math.min(pearlItem.getAmount() + 1, 16)
                : 1;
        klr.getInventory().setItem(pearlSlot, new ItemStack(Material.ENDER_PEARL, newAmount));

        // Reset last hit tracking
        data.setLastHit(klru, null);
        data.setLastHit(pu, null);

        String klrdn = klr.getDisplayName();
        String pdn = (p != null)
                ? p.getDisplayName()
                : Bukkit.getOfflinePlayer(pu).getName();
        if (pdn == null) pdn = "Unknown";

        // Killer's message and sound
        Audience klra = getAudience(klr);
        klra.sendActionBar(Component.text(klrdn + " §7killed " + pdn + "§7!"));
        klr.setHealth(20.0);
        klr.playSound(klr.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);

        // Victim's message and sound if online
        if (p != null) {
            Audience pa = getAudience(p);
            pa.sendActionBar(Component.text(klrdn + " §7killed " + pdn + "§7!"));
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            if (data.getks(pu) >= 10) {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.playSound(op.getLocation(), Sound.SKELETON_DEATH, 0.75f, 2.0f);
                }
                Bukkit.broadcastMessage(klrdn + " §ahas ended " + p.getDisplayName() + "'s " + data.getks(pu) + " killstreaks!");
            }
        }

        // Reset victim streak, increment killer streak
        data.setks(pu, 0);
        data.addks(klru);

        int streak = data.getks(klru);
        if (streak >= 10 && streak % 5 == 0) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.playSound(op.getLocation(), Sound.ENDERDRAGON_GROWL, 0.75f, 2.0f);
            }
            Bukkit.broadcastMessage(klrdn + " §ahas reached " + streak + " killstreaks!");
        }
    }

    private static BukkitRunnable mapTask;

    public static void changeMap() {
        data.setCurmap(data.getRandomMap());
        Bukkit.broadcastMessage("§eMap has changed!");
        for (Player p : Bukkit.getOnlinePlayers()) {
            handleKill(p.getUniqueId(), data.getLastHit(p.getUniqueId()));
            spawn(p.getUniqueId());
        }
        Bukkit.broadcastMessage("§7Next map change in 5 minutes.");
    }

    public static void startMapRotation() {
        if (mapTask != null) {
            mapTask.cancel();
        }

        mapTask = new BukkitRunnable() {
            @Override
            public void run() {
                changeMap();
                startMapRotation();
            }
        };

        mapTask.runTaskLater(data.getPlugin(), 5 * 60 * 20);
    }



}
