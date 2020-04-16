package ru.kdev.Jumper.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import ru.kdev.Jumper.Jumper;
import ru.kdev.Jumper.database.Players;

import java.sql.SQLException;
import java.util.List;

public class JAdmin implements CommandExecutor {
    Jumper jumper = Jumper.getPlugin(Jumper.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("jumper.admin")) {
            sender.sendMessage(ChatColor.RED + "У Вас нет прав!");
            return true;
        }
        if(args.length > 0) {
            if(args[0].equals("setspawn")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    Location loc = player.getLocation();
                    jumper.getConfig().set("spawn.world", loc.getWorld().getName());
                    jumper.getConfig().set("spawn.x", loc.getX());
                    jumper.getConfig().set("spawn.y", loc.getY());
                    jumper.getConfig().set("spawn.z", loc.getZ());
                    jumper.getConfig().set("spawn.yaw", loc.getYaw());
                    jumper.getConfig().set("spawn.pitch", loc.getPitch());
                    jumper.saveConfig();
                    player.sendMessage(ChatColor.WHITE + "Точка спавна установлена!");
                }
            }
            if(args[0].equals("addnpc")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    if(args.length > 1) {
                        String saveString = args[1] + ":" + player.getLocation().getWorld().getName() + ":" + player.getLocation().getX()
                                + ":" + player.getLocation().getY() + ":" + player.getLocation().getZ();
                        List<String> list = jumper.getConfig().getStringList("npcs");
                        list.add(saveString);
                        jumper.getConfig().set("npcs", list);
                        jumper.saveConfig();
                        Location loc = new Location(
                                player.getLocation().getWorld(),
                                player.getLocation().getX(),
                                player.getLocation().getY(),
                                player.getLocation().getZ(),
                                player.getLocation().getYaw(),
                                player.getLocation().getPitch()
                        );
                        Villager villager = (Villager) player.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                        villager.setInvulnerable(true);
                        villager.setAI(false);
                        villager.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + "Вход");
                        villager.setCustomNameVisible(true);
                        villager.setCollidable(false);
                        villager.setMetadata("zone", new FixedMetadataValue(jumper, args[1]));
                        player.sendMessage(ChatColor.WHITE + "NPC добавлен");
                    }
                }
            }
            if(args[0].equals("addexitnpc")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    Location loc = new Location(
                            player.getLocation().getWorld(),
                            player.getLocation().getX(),
                            player.getLocation().getY(),
                            player.getLocation().getZ(),
                            player.getLocation().getYaw(),
                            player.getLocation().getPitch()
                    );
                    Villager villager = (Villager) player.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                    villager.setInvulnerable(true);
                    villager.setAI(false);
                    villager.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "Выход");
                    villager.setCustomNameVisible(true);
                    villager.setCollidable(false);
                    villager.setMetadata("exit", new FixedMetadataValue(jumper, 1));
                    player.sendMessage(ChatColor.WHITE + "NPC добавлен");
                }
            }
            if(args[0].equals("setlevel")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    if(args.length > 2) {
                        Player player1 = Bukkit.getPlayer(args[1]);
                        try {
                            if(Players.exists(player1)) {
                                Players.setLevel(player1, Integer.parseInt(args[2]));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.WHITE + "Игроку выдан уровень!");
                    }
                }
            }
            if(args[0].equals("setxp")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    if(args.length > 2) {
                        Player player1 = Bukkit.getPlayer(args[1]);
                        try {
                            if(Players.exists(player1)) {
                                Players.setXP(player1, Integer.parseInt(args[2]));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.WHITE + "Игроку выдан опыт!");
                    }
                }
            }
            if(args[0].equals("setboots")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    if(args.length > 2) {
                        Player player1 = Bukkit.getPlayer(args[1]);
                        try {
                            if(Players.exists(player1)) {
                                Players.setBLevel(player1, Integer.parseInt(args[2]));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.WHITE + "Игроку выдан уровень ботинок!");
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fПомощь по командам:"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/jadmin setlevel [&cНИК&f] &f[&cУРОВЕНЬ&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/jadmin setxp [&cНИК&f] &f[&cEXP&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/jadmin setboots [&cНИК&f] &f[&cУРОВЕНЬ&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/jadmin setspawn"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/jadmin addnpc &f[&cЗОНА&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/jadmin addexitnpc"));
        }
        return false;
    }
}
