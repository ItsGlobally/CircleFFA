package me.itsglobally.circleffa;

import github.scarsz.discordsrv.DiscordSRV;
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

import java.util.*;

public class utils {

    // A map to store player UUID -> Set of scoreboard entries you added
    private static final Map<UUID, Set<String>> playerScoreEntries = new HashMap<>();
    private static BukkitRunnable mapTask;

    public static void joinFFA(UUID u) {
        Player p = Bukkit.getPlayer(u);
        data.setPlayerGamemode(u, "KBFFA");
        spawn(u);
    }

    public static void joinLobby(UUID u) {
        Player p = Bukkit.getPlayer(u);
        data.setPlayerGamemode(u, "LOBBY");
        handleKill(u, data.getLastHit(u));
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.teleport(new Location(Bukkit.getWorld("ffa"), 2001.5, 201, 2001.5));
    }

    public static void spawn(UUID u) {
        Player p = Bukkit.getPlayer(u);
        if (data.getPlayerMode(p.getUniqueId()).equals("LOBBY")) {
            joinLobby(p.getUniqueId());
            return;
        }
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
        if (pu == klru) {
            data.setks(pu, 0L);
            return;
        }
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
                DiscordSRV.getPlugin().getJda().getTextChannelById(1401142669287100498L)
                        .sendMessage(klr.getName() + " has ended " + p.getName() + "'s " + data.getks(pu) + " killstreaks!")
                        .queue();
            }
            DiscordSRV.getPlugin().getJda().getTextChannelById(1401142669287100498L)
                    .sendMessage(klr.getName() + " killed " + p.getName())
                    .queue();
            MongoStatUtil.addDies(p.getUniqueId());
        }
        data.setks(pu, 0L);
        data.addks(klru);
        MongoStatUtil.addKill(klru);
        MongoStatUtil.addXp(klru, data.getks(klru));
        long streak = data.getks(klru);
        if (streak >= 10 && streak % 5 == 0) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.playSound(op.getLocation(), Sound.ENDERDRAGON_GROWL, 0.75f, 2.0f);
            }
            Bukkit.broadcastMessage(klrdn + " §ahas reached " + streak + " §akillstreaks!");
            DiscordSRV.getPlugin().getJda().getTextChannelById(1401142669287100498L)
                    .sendMessage(klr.getName() + " has reached " + streak + " killstreaks!")
                    .queue();
        }
    }

    public static void changeMap() {
        data.setCurmap(data.getRandomMap());
        Bukkit.broadcastMessage("§eMap has changed!");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (data.getBm(p.getUniqueId()) || !Objects.equals(data.getPlayerMode(p.getUniqueId()), "KBFFA")) continue;
            handleKill(p.getUniqueId(), data.getLastHit(p.getUniqueId()));
            spawn(p.getUniqueId());
        }
        Bukkit.broadcastMessage("§7Next map change in 10 minutes.");
        if (DiscordSRV.getPlugin().getJda() != null)
            Objects.requireNonNull(DiscordSRV.getPlugin().getJda().getTextChannelById(1401142669287100498L))
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

    public static void updateScoreBoard(UUID u) {
        Player p = Bukkit.getPlayer(u);
        if (p == null) return;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard sb = p.getScoreboard();

        if (sb == null || sb == manager.getMainScoreboard()) {
            sb = manager.getNewScoreboard();
            p.setScoreboard(sb);
        }

        Objective obj = sb.getObjective("stats");
        if (obj == null) {
            obj = sb.registerNewObjective("stats", "dummy");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.setDisplayName("§dCircle FFA");
        }

        // Clear old scores you added previously
        Set<String> oldEntries = playerScoreEntries.getOrDefault(u, new HashSet<>());
        for (String entry : oldEntries) {
            sb.resetScores(entry);
        }

        // Prepare new entries
        Set<String> newEntries = new HashSet<>();

        String line1 = "§r§7╭─────────";
        obj.getScore(line1).setScore(10);
        newEntries.add(line1);

        String line2 = "│ §aStars: §7[" + MongoStatUtil.getStars(u) + "✫]";
        obj.getScore(line2).setScore(9);
        newEntries.add(line2);

        String line3 = "│ §aXP: " + MongoStatUtil.getXp(u);
        obj.getScore(line3).setScore(8);
        newEntries.add(line3);

        String line4 = "│ §aKills: " + MongoStatUtil.getKills(u);
        obj.getScore(line4).setScore(7);
        newEntries.add(line4);

        String line5 = "│ §aDeaths: " + MongoStatUtil.getDies(u);
        obj.getScore(line5).setScore(6);
        newEntries.add(line5);

        String line6 = "│ §aKillstreaks: " + data.getks(u);
        obj.getScore(line6).setScore(5);
        newEntries.add(line6);

        double kdr = MongoStatUtil.getDies(u) == 0 ? MongoStatUtil.getKills(u) : (double) MongoStatUtil.getKills(u) / MongoStatUtil.getDies(u);
        String kdrFormatted = String.format("%.2f", kdr);
        String line7 = "│ §aKDR: " + kdrFormatted;
        obj.getScore(line7).setScore(4);
        newEntries.add(line7);

        String line8 = "│ §ditsglobally.top";
        obj.getScore(line8).setScore(3);
        newEntries.add(line8);
        String line9 = "§7╰─────────";
        obj.getScore(line9).setScore(2);
        newEntries.add(line9);

        playerScoreEntries.put(u, newEntries);
    }

}
