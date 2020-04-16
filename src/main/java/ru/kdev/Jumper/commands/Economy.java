package ru.kdev.Jumper.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kdev.Jumper.database.Players;

import java.sql.SQLException;

public class Economy implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("jumper.admin")) {
            sender.sendMessage(ChatColor.RED + "У Вас нет прав!");
            return true;
        }
        if(args.length > 0) {
            if(args[0].equals("add")) {
                if(args.length == 3) {
                    Player player1 = Bukkit.getPlayer(args[1]);
                    try {
                        assert player1 != null;
                        Players.addBalance(player1, Integer.parseInt(args[2]));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage("Баланс пополнен!");
                }
            }
            if(args[0].equals("take")) {
                if(args.length == 3) {
                    Player player1 = Bukkit.getPlayer(args[1]);
                    try {
                        assert player1 != null;
                        Players.takeBalance(player1, Integer.parseInt(args[2]));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage("Баланс уменьшен!");
                }
            }
            if(args[0].equals("set")) {
                if(args.length == 3) {
                    Player player1 = Bukkit.getPlayer(args[1]);
                    try {
                        assert player1 != null;
                        Players.setBalance(player1, Integer.parseInt(args[2]));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage("Баланс установлен!");
                }
            }
            if(args[0].equals("get")) {
                if(args.length == 2) {
                    Player player1 = Bukkit.getPlayer(args[1]);
                    try {
                        assert player1 != null;
                        sender.sendMessage("Баланс игрока: " + Players.getBalance(player1));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fПомощь по командам:"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/economy add &f[&cНИК&f] &f[&cСУММА&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/economy take &f[&cНИК&f] &f[&cСУММА&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/economy set &f[&cНИК&f] &f[&cСУММА&f]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/economy get &f[&cНИК&f]"));
        }
        return false;
    }
}
