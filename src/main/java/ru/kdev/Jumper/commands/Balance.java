package ru.kdev.Jumper.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kdev.Jumper.database.Players;

import java.sql.SQLException;

public class Balance implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            try {
                player.sendMessage(ChatColor.WHITE + "Ваш баланс: " + ChatColor.RED + Players.getBalance(player) + "$");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
