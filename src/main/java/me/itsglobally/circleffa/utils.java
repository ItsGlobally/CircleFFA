package me.itsglobally.circleffa;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
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
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Objects;
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
        inv.setHelmet(armor1(new ItemStack(Material.LEATHER_HELMET)));
        inv.setChestplate(armor1(new ItemStack(Material.LEATHER_CHESTPLATE)));
        inv.setLeggings(armor2(new ItemStack(Material.IRON_LEGGINGS)));
        inv.setBoots(armor2(new ItemStack(Material.IRON_LEGGINGS)));
        inv.setItem(data.getLayout(u, "sword"), sword(new ItemStack(Material.WOOD_SWORD)));
        inv.setItem(data.getLayout(u, "block"), new ItemStack(Material.SANDSTONE, 64));
        inv.setItem(data.getLayout(u, "tool"), tool(new ItemStack(Material.STONE_PICKAXE)));
        inv.setItem(data.getLayout(u, "bow"), bow(new ItemStack(Material.BOW)));
        inv.setItem(data.getLayout(u, "arrow"), new ItemStack(Material.ARROW, 16));
        inv.setItem(data.getLayout(u, "pearl"), new ItemStack(Material.ENDER_PEARL));
    }

    private static ItemStack armor1(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        LeatherArmorMeta lam = (LeatherArmorMeta) im;
        lam.spigot().setUnbreakable(true);
        lam.setColor(Color.fromRGB(242, 218, 255));
        is.setItemMeta(lam);
        is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        is.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        return is;
    }
    private static ItemStack armor2(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        is.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
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
        if (pu == klru) return;
        if (klr == null) return;

        klr.getInventory().setItem(
                data.getLayout(klru, "block"),
                new ItemStack(Material.SANDSTONE, 64)
        );
        klr.getInventory().setItem(
                data.getLayout(klru, "arrow"),
                new ItemStack(Material.ARROW, 16)
        );

        int pearlSlot = data.getLayout(klr.getUniqueId(), "pearl");
        ItemStack pearlItem = klr.getInventory().getItem(pearlSlot);
        int newAmount = (pearlItem != null && pearlItem.getType() == Material.ENDER_PEARL)
                ? Math.min(pearlItem.getAmount() + 1, 16)
                : 1;
        klr.getInventory().setItem(pearlSlot, new ItemStack(Material.ENDER_PEARL, newAmount));

        data.setLastHit(klru, null);
        data.setLastHit(pu, null);

        String klrdn = klr.getDisplayName();
        String pdn = (p != null)
                ? p.getDisplayName()
                : Bukkit.getOfflinePlayer(pu).getName();
        if (pdn == null) pdn = "Unknown";

        Audience klra = getAudience(klr);
        klra.sendActionBar(Component.text(klrdn + " §7killed " + pdn + "§7!"));
        klr.setHealth(20.0);
        klr.playSound(klr.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);

        if (p != null) {
            Audience pa = getAudience(p);
            pa.sendActionBar(Component.text(klrdn + " §7killed " + pdn + "§7!"));
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            if (data.getks(pu) >= 10) {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.playSound(op.getLocation(), Sound.SKELETON_DEATH, 0.75f, 2.0f);
                }
                Bukkit.broadcastMessage(klrdn + " §ahas ended " + p.getDisplayName() + "§a's " + data.getks(pu) + " killstreaks!");
                DiscordSRV.getPlugin().getJda().getTextChannelById(1392853553369972756L)
                        .sendMessage(klr.getName() + " has ended " + p.getName() + "'s " + data.getks(pu) + " killstreaks!")
                        .queue();
            }
            DiscordSRV.getPlugin().getJda().getTextChannelById(1392853553369972756L)
                    .sendMessage(klr.getName() + " killed " + p.getName())
                    .queue();

        }
        data.setks(pu, 0L);
        data.addks(klru);
        data.addKill(klru, 1L);
        data.addDies(p.getUniqueId());
        starUtils.addXp(klru, data.getks(klru));
        long streak = data.getks(klru);
        if (streak >= 10 && streak % 5 == 0) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.playSound(op.getLocation(), Sound.ENDERDRAGON_GROWL, 0.75f, 2.0f);
            }
            Bukkit.broadcastMessage(klrdn + " §ahas reached " + streak + " §akillstreaks!");
            DiscordSRV.getPlugin().getJda().getTextChannelById(1392853553369972756L)
                    .sendMessage(klr.getName() + " has reached " + streak + " killstreaks!")
                    .queue();
        }
    }

    private static BukkitRunnable mapTask;

    public static void changeMap() {
        data.setCurmap(data.getRandomMap());
        Bukkit.broadcastMessage("§eMap has changed!");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (data.getBm(p.getUniqueId())) continue;
            handleKill(p.getUniqueId(), data.getLastHit(p.getUniqueId()));
            spawn(p.getUniqueId());
        }
        Bukkit.broadcastMessage("§7Next map change in 10 minutes.");
        if (DiscordSRV.getPlugin().getJda() != null) Objects.requireNonNull(DiscordSRV.getPlugin().getJda().getTextChannelById(1392853553369972756L))
                .sendMessage("Map changed")
                .queue();
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

        mapTask.runTaskLater(data.getPlugin(), 10 * 60 * 20L);
    }

    public static void updateScoreBorad(UUID u) {
        Player p = Bukkit.getPlayer(u);
        if (p == null) return;

        Scoreboard sb = p.getScoreboard();
        Objective obj = sb.getObjective("stats");
        if (obj == null) {
            obj = sb.registerNewObjective("stats", "dummy");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.setDisplayName("§dCircle FFA");
        }

        for (String entry : sb.getEntries()) {
            if (obj.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(entry) != null) {
                sb.resetScores(entry);
            }
        }

        obj.getScore("§r§7--------------").setScore(10);
        obj.getScore("§aStars: " + "§7[" + starUtils.getStar(u) + "✫]").setScore(9);
        obj.getScore("§aXP: " + starUtils.getXp(u)).setScore(9);
        obj.getScore("§aKills: " + data.getKill(u)).setScore(7);
        obj.getScore("§aDeaths: " + data.getDies(u)).setScore(6);
        obj.getScore("§aKillstreaks: " + data.getks(u)).setScore(5);
        double kdr = data.getDies(u) == 0 ? data.getKill(u) : (double) data.getKill(u) / data.getDies(u);
        String kdrFormatted = String.format("%.2f", kdr);
        obj.getScore("§aKDR: " + kdrFormatted).setScore(4);
        obj.getScore("§7--------------").setScore(3);
        obj.getScore("§ditsglobally.top").setScore(1);
    }


}
