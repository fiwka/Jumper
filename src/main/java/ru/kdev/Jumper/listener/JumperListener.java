package ru.kdev.Jumper.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import ru.kdev.Jumper.Jumper;
import ru.kdev.Jumper.database.Players;
import ru.kdev.Jumper.utils.IntFormatter;
import ru.kdev.Jumper.utils.ZoneManager;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class JumperListener implements Listener {
    Jumper jumper = Jumper.getPlugin(Jumper.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();
        if(!Players.exists(player)) {
            Players.createPlayer(player);
        }
        if(!Jumper.scoreboards.containsKey(player)) {
            ArrayList<Team> rows = new ArrayList<>();
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("jumper", "dummy", cColor("     &aJumper     "));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            rows.add(createRow(board, " &7◾ Уровень: &f" + IntFormatter.withSuffix(Players.getLevel(player)), "level", 1));
            rows.add(createRow(board, " &7◾ XP: &f" + IntFormatter.withSuffix(Players.getXP(player)), "xp", 2));
            rows.add(createRow(board, " &7◾ Осталось: &f" + IntFormatter.withSuffix(Players.getNeeds(player)), "needs", 3));
            rows.add(createRow(board, " &7◾ Баланс: &a" + IntFormatter.withSuffix(Players.getBalance(player)) + "&c$", "balance", 4));
            rows.add(createRow(board, " &7◾ Ребитхи: &c" + IntFormatter.withSuffix(Players.getRebirths(player)), "rebirths", 5));
            obj.getScore("").setScore(8);
            obj.getScore(cColor(" &bПрофиль")).setScore(7);
            obj.getScore("§1").setScore(6);
            obj.getScore("§2").setScore(5);
            obj.getScore("§3").setScore(4);
            obj.getScore("§4").setScore(3);
            obj.getScore("§5").setScore(2);
            obj.getScore(" ").setScore(1);
            obj.getScore("     www.cristalix.ru     ").setScore(0);
            player.setScoreboard(board);
            Jumper.scoreboards.put(player, rows);
        }
        player.teleport(new Location(
                Bukkit.getWorld(jumper.getConfig().getString("spawn.world")),
                jumper.getConfig().getDouble("spawn.x"),
                jumper.getConfig().getDouble("spawn.y"),
                jumper.getConfig().getDouble("spawn.z"),
                convertToFloat(jumper.getConfig().getDouble("spawn.yaw")),
                convertToFloat(jumper.getConfig().getDouble("spawn.pitch"))
        ));
        ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.setDisplayName(ChatColor.GREEN + "Ботинки");
        bootsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Уровень " + Players.getBootsLevel(player)));
        bootsMeta.setUnbreakable(true);
        boots.setItemMeta(bootsMeta);
        player.getInventory().setBoots(boots);
    }

    public static Float convertToFloat(double doubleValue) {
        return (float) doubleValue;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().getType() == Material.GOLDEN_BOOTS) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFallDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player)e.getEntity();
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL){
                e.setCancelled(true);
            }
            if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) throws SQLException {
        if(e.getSlotType() == InventoryType.SlotType.ARMOR) {
            e.setCancelled(true);
        }

        if(e.getClickedInventory().getTitle().equals(ChatColor.GREEN + "Прокачка ботинок")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if(e.getCurrentItem().getType() == Material.EXPERIENCE_BOTTLE) {
                if(Players.getBootsLevel(player) == 10) {
                    player.sendMessage(ChatColor.RED + "Вы достигли максимального уровня ботинок!");
                    return;
                }
                if(Players.getBalance(player) >= Players.getBootsLevel(player) * 100) {
                    Players.takeBalance(player, Players.getBootsLevel(player) * 100);
                    player.closeInventory();
                    Players.setBLevel(player, Players.getBootsLevel(player) + 1);
                    ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
                    ItemMeta bootsMeta = boots.getItemMeta();
                    bootsMeta.setDisplayName(ChatColor.GREEN + "Ботинки");
                    bootsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Уровень " + Players.getBootsLevel(player)));
                    bootsMeta.setUnbreakable(true);
                    boots.setItemMeta(bootsMeta);
                    player.getInventory().setBoots(boots);
                    player.sendMessage(ChatColor.GREEN + "Вы успешно прокачали ботинки!");
                } else {
                    player.sendMessage(ChatColor.RED + "У Вас недостаточно денег!");
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) throws SQLException {
        e.getEntity().spigot().respawn();
        e.getEntity().teleport(new Location(
                Bukkit.getWorld(jumper.getConfig().getString("spawn.world")),
                jumper.getConfig().getDouble("spawn.x"),
                jumper.getConfig().getDouble("spawn.y"),
                jumper.getConfig().getDouble("spawn.z"),
                convertToFloat(jumper.getConfig().getDouble("spawn.yaw")),
                convertToFloat(jumper.getConfig().getDouble("spawn.pitch"))
        ));
        Jumper.playing.remove(e.getEntity());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) throws UnsupportedEncodingException, SQLException {
        if(Jumper.playing.containsKey(e.getPlayer())) {
            ZoneManager zoneManager = new ZoneManager();
            if(zoneManager.getFinalPointZone(Jumper.playing.get(e.getPlayer())).distance(e.getPlayer().getLocation()) <= zoneManager.getFinalPointDistanceZone(Jumper.playing.get(e.getPlayer()))) {
                zoneManager.teleportToZone(e.getPlayer(), Jumper.playing.get(e.getPlayer()));
                e.getPlayer().sendTitle(ChatColor.BLUE + "Вы получили +" + zoneManager.getFinalPointExpZone(Jumper.playing.get(e.getPlayer())) +" XP!", ChatColor.GREEN + "Поздравляем!", 20, 100, 20);
                Players.setXP(e.getPlayer(), Players.getXP(e.getPlayer()) + zoneManager.getFinalPointExpZone(Jumper.playing.get(e.getPlayer())));
                Random rand = new Random();
                int chance = rand.nextInt(10);
                if(chance < 5) {
                    ItemStack artifact = new ItemStack(Material.LAPIS_LAZULI, 1);
                    ItemMeta artifactMeta = artifact.getItemMeta();
                    artifactMeta.setDisplayName(ChatColor.GREEN + "Артифакт");
                    artifact.setItemMeta(artifactMeta);
                    e.getPlayer().getInventory().addItem(artifact);
                }
            }
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) throws SQLException {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getClickedBlock().getType() == Material.SIGN) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                if(sign.getLine(1).matches(ChatColor.BOLD + "Продать всё")) {
                    int price = 0;
                    for(ItemStack item : e.getPlayer().getInventory().getContents()) {
                        if(item == null) {
                            continue;
                        }
                        if(item.getType() == Material.LAPIS_LAZULI && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Артифакт")) {
                            price += item.getAmount() * 10;
                            e.getPlayer().getInventory().removeItem(item);
                        }
                    }
                    e.getPlayer().sendMessage("Вы получили " + price + "$ за продажу артифактов");
                    Players.addBalance(e.getPlayer(), price);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity().getType() == EntityType.VILLAGER && e.getEntity().getCustomName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "Вход")) {
            e.setCancelled(true);
        }
        if(e.getEntity().getType() == EntityType.VILLAGER && e.getEntity().getCustomName().equals(ChatColor.RED + "" + ChatColor.BOLD + "Выход")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) throws UnsupportedEncodingException, SQLException {
        if(e.getRightClicked().getCustomName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "Вход") && e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            ZoneManager zoneManager = new ZoneManager();
            Location loc1 = e.getRightClicked().getLocation();
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(String npc : jumper.getConfig().getStringList("npcs")) {
                        String[] splitted = npc.split(":");
                        Location loc2 = new Location(
                                Bukkit.getWorld(splitted[1]),
                                Double.parseDouble(splitted[2]),
                                Double.parseDouble(splitted[3]),
                                Double.parseDouble(splitted[4])
                        );
                        if(loc2.distance(loc1) <= 1.2) {
                            try {
                                if(Players.getLevel(e.getPlayer()) >= zoneManager.getMinimumLevelZone(splitted[0])) {
                                    zoneManager.teleportToZone(e.getPlayer(), splitted[0]);
                                    Jumper.playing.put(e.getPlayer(), splitted[0]);
                                } else {
                                    e.getPlayer().sendMessage(ChatColor.RED + "У Вас недостаточный уровень! Требуется: " + zoneManager.getMinimumLevelZone(splitted[0]));
                                }
                            } catch (UnsupportedEncodingException | SQLException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }.runTaskAsynchronously(jumper);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, Players.getBootsLevel(e.getPlayer())));
        } else if(e.getRightClicked().getCustomName().equals(ChatColor.RED + "" + ChatColor.BOLD + "Выход") && e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            e.getPlayer().teleport(new Location(
                    Bukkit.getWorld(jumper.getConfig().getString("spawn.world")),
                    jumper.getConfig().getDouble("spawn.x"),
                    jumper.getConfig().getDouble("spawn.y"),
                    jumper.getConfig().getDouble("spawn.z"),
                    convertToFloat(jumper.getConfig().getDouble("spawn.yaw")),
                    convertToFloat(jumper.getConfig().getDouble("spawn.pitch"))
            ));
            for(PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
                e.getPlayer().removePotionEffect(effect.getType());
            }
            Jumper.playing.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        if(Jumper.playing.containsKey(e.getPlayer())) {
            Jumper.playing.remove(e.getPlayer());
        }
        player.getInventory().setBoots(new ItemStack(Material.AIR));
    }

    private Team createRow(Scoreboard board, String text, String name, int num) {
        Team row;
        row = board.registerNewTeam(name);
        row.addEntry("§" + num + "");
        row.setPrefix(cColor(text));
        return row;
    }

    private String cColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
