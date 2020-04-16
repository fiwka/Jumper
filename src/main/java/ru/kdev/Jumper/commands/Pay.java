package ru.kdev.Jumper.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kdev.Jumper.database.Players;

import java.sql.SQLException;

public class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            try {
                if(args.length == 2) {
                    Player player1 = Bukkit.getPlayer(args[0]);
                    if(!Bukkit.getOnlinePlayers().contains(player1)) {
                        player.sendMessage(ChatColor.RED + "Игрок не в сети!");
                        return true;
                    }
                    if(Players.getBalance(player) < Integer.parseInt(args[1])) {
                        player.sendMessage(ChatColor.RED + "У Вас недостаточно денег!");
                        return true;
                    }
                    Players.takeBalance(player, Integer.parseInt(args[1]));
                    Players.addBalance(player1, Integer.parseInt(args[1]));
                    player.sendMessage(ChatColor.GREEN + "Вы успешно перевели " + args[1] + "$ игроку " + player1.getName());
                } else {
                    player.sendMessage(ChatColor.RED + "Использование: /pay <игрок> <сумма>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
