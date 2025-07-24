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

import java.util.UUID;

public class utils {
    public static void spawn(UUID u) {
        Player p = Bukkit.getPlayer(u);
        if (p == null) return;

        p.teleport(new Location(p.getWorld(), 200.5, 201, 200.5));
        p.setHealth(20.0);
        p.setFoodLevel(20);

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        PlayerInventory inv = p.getInventory();
        inv.setHelmet(armor(new ItemStack(Material.LEATHER_HELMET)));
        inv.setChestplate(armor(new ItemStack(Material.LEATHER_CHESTPLATE)));
        inv.setLeggings(armor(new ItemStack(Material.LEATHER_LEGGINGS)));
        inv.setBoots(armor(new ItemStack(Material.LEATHER_BOOTS)));
        inv.setItem(data.getLayout(u, "sword"), sword(new ItemStack(Material.IRON_SWORD)));
        inv.setItem(data.getLayout(u, "block"), item(new ItemStack(Material.WOOL, 64)));
        inv.setItem(data.getLayout(u, "tool"), item(new ItemStack(Material.SHEARS)));
    }

    private static ItemStack armor(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        LeatherArmorMeta lam = (LeatherArmorMeta) im;
        lam.spigot().setUnbreakable(true);
        lam.setColor(Color.fromBGR(255, 229, 255));
        is.setItemMeta(lam);
        is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        return is;
    }

    private static ItemStack sword(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        return is;
    }

    private static ItemStack item(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        is.setItemMeta(im);
        return is;
    }

    public static Audience getAudience(Player p) {
        return data.getInstance().adventure().player(p);
    }

    public static void handleKill(UUID pu, UUID klru) {
        Player p = Bukkit.getPlayer(pu);
        Player klr = Bukkit.getPlayer(klru);
        Audience pa = getAudience(p);
        Audience klra = getAudience(klr);
        String pdn = p.getDisplayName();
        String klrdn = klr.getDisplayName();
        pa.sendActionBar(Component.text(pdn + " killed " + klrdn + "!"));
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
        klra.sendActionBar(Component.text(pdn + " killed " + klrdn + "!"));
        klr.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
        data.setks(pu, 0);
        data.addks(klru);
        if (data.getks(klru) >= 10) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                getAudience(op).sendActionBar(Component.text(klrdn + " has reached 10 killstreaks!"));
                op.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 0.75f, 2.0f);
            }
        }
    }

}
