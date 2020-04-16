package ru.kdev.Jumper.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import ru.kdev.Jumper.Jumper;
import ru.kdev.Jumper.database.Players;

import java.sql.SQLException;
import java.util.Arrays;

public class Rebirth implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            try {
                if(Players.getLevel(player) < 50) {
                    player.sendMessage(ChatColor.RED + "Ребитх доступен с 50 уровня!");
                    return true;
                }
                if(Players.getRebirths(player) < 6) Players.setLevel(player, Players.getRebirths(player) + 1);
                else Players.setLevel(player, 6);
                Players.setBLevel(player, 2);
                Players.setBalance(player, 100);
                Players.setRebirths(player, Players.getRebirths(player) + 1);
                if(Jumper.playing.containsKey(player)) {
                    Jumper.playing.remove(player);
                }
                for(PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
                ItemMeta bootsMeta = boots.getItemMeta();
                bootsMeta.setDisplayName(ChatColor.GREEN + "Ботинки");
                bootsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Уровень " + Players.getBootsLevel(player)));
                bootsMeta.setUnbreakable(true);
                boots.setItemMeta(bootsMeta);
                player.getInventory().setBoots(boots);
                player.sendTitle(ChatColor.BLUE + "Вы сделали ребитх!", ChatColor.GREEN + "Поздравляем!", 20, 100, 20);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
