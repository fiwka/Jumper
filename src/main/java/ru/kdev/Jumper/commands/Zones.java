package ru.kdev.Jumper.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kdev.Jumper.Jumper;
import ru.kdev.Jumper.utils.ZoneManager;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class Zones implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("jumper.zones")) {
            sender.sendMessage(ChatColor.RED + "У Вас нет прав!");
            return true;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("This player only command");
            return true;
        }
        Player player = (Player) sender;
        if(args.length > 0) {
            if(args[0].equals("create")) {
                if(args.length > 2) {
                    ZoneManager zoneManager = new ZoneManager();
                    try {
                        if(zoneManager.existsZone(args[1])) {
                            player.sendMessage("Зона существует");
                            return true;
                        }
                        Location loc = player.getLocation();
                        zoneManager.createZone(args[1], loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), Integer.parseInt(args[2]), Objects.requireNonNull(loc.getWorld()));
                        player.sendMessage("Зона создана");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(args[0].equals("setspawn")) {
                if(args.length > 1) {
                    ZoneManager zoneManager = new ZoneManager();
                    try {
                        if(!zoneManager.existsZone(args[1])) {
                            player.sendMessage("Зона не существует");
                            return true;
                        }
                        Location loc = player.getLocation();
                        zoneManager.setSpawnZone(args[1], loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), Objects.requireNonNull(loc.getWorld()));
                        player.sendMessage("Спавн зоны изменен!");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(args[0].equals("remove")) {
                if(args.length > 1) {
                    ZoneManager zoneManager = new ZoneManager();
                    try {
                        if(!zoneManager.existsZone(args[1])) {
                            player.sendMessage("Зона не существует");
                            return true;
                        }
                        zoneManager.removeZone(args[1]);
                        player.sendMessage("Зона удалена!");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(args[0].equals("setfinalpoint")) {
                if(args.length > 3) {
                    ZoneManager zoneManager = new ZoneManager();
                    try {
                        if(!zoneManager.existsZone(args[1])) {
                            player.sendMessage("Зона не существует");
                            return true;
                        }
                        Location loc = player.getLocation();
                        zoneManager.setFinalPoint(args[1], loc.getX(), loc.getY(), loc.getZ(), Objects.requireNonNull(loc.getWorld()), Double.parseDouble(args[2]), Integer.parseInt(args[3]));
                        player.sendMessage("Зона финальная точка установлена!");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fПомощь по командам:"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/zones create [&cНАЗВАНИЕ&f] [&cМИНУРОВЕНЬ&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/zones setspawn [&cНАЗВАНИЕ&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/zones setfinalpoint [&cНАЗВАНИЕ&f] &f[&cEXP&f] &f[&cДИСТАНЦИЯ&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/zones remove [&cНАЗВАНИЕ&f]"));
        }
        return false;
    }
}
