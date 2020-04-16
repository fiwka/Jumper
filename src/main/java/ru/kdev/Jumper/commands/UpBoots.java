package ru.kdev.Jumper.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kdev.Jumper.gui.UpgradeBoots;

import java.sql.SQLException;

public class UpBoots implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            UpgradeBoots upBootsGui = null;
            try {
                upBootsGui = new UpgradeBoots(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            upBootsGui.showInventory();
        }
        return false;
    }
}
