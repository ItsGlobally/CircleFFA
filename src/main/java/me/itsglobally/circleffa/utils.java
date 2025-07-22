package me.itsglobally.circleffa;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
        inv.addItem(sword(new ItemStack(Material.IRON_SWORD)));
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
}
