package ru.kdev.Jumper.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kdev.Jumper.database.Players;

import java.sql.SQLException;
import java.util.Arrays;

public class UpgradeBoots {
    private Player player;
    private Inventory inventory;

    public UpgradeBoots(Player player) throws SQLException {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 9, ChatColor.GREEN + "Прокачка ботинок");
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Прокачать уровень");
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Ваш уровень: " + ChatColor.DARK_GRAY + Players.getBootsLevel(player), ChatColor.GRAY + "Цена следующего уровная " + ChatColor.GREEN + Players.getBootsLevel(player) * 100 + "$"));
        item.setItemMeta(itemMeta);
        this.inventory.setItem(4, item);
    }

    public void showInventory() {
        this.player.openInventory(this.inventory);
    }
}
