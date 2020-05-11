package ru.kdev.Jumper.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kdev.Jumper.Jumper;
import ru.kdev.Jumper.database.Players;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class UpgradeBoots implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("upBoots")
            .provider(new UpgradeBoots())
            .size(3, 9)
            .title(ChatColor.GREEN + "Прокачка уровня")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Прокачать уровень");
        try {
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Ваш уровень: " + ChatColor.GREEN + Players.getBootsLevel(player), ChatColor.GRAY + "Цена следующего уровная " + ChatColor.GREEN + Players.getBootsLevel(player) * 100 + "$"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        item.setItemMeta(meta);
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        contents.set(1, 4, ClickableItem.of(item, e -> {
            try {
                if(Players.getBootsLevel(player) == 10) {
                    player.sendMessage(ChatColor.RED + "Вы достигли максимального уровня ботинок!");
                    return;
                }
                if(Players.getBalance(player) >= Players.getBootsLevel(player) * 100) {
                    player.closeInventory();
                    ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
                    ItemMeta bootsMeta = boots.getItemMeta();
                    bootsMeta.setDisplayName(ChatColor.GREEN + "Ботинки");
                    Players.takeBalance(player, Players.getBootsLevel(player) * 100);
                    Players.setBLevel(player, Players.getBootsLevel(player) + 1);
                    Bukkit.getScheduler().runTaskLater(Jumper.getPlugin(Jumper.class), () -> {
                        try {
                            bootsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Уровень " + Players.getBootsLevel(player)));
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        bootsMeta.setUnbreakable(true);
                        boots.setItemMeta(bootsMeta);
                        player.getInventory().setBoots(boots);
                    }, 20L);
                    player.sendMessage(ChatColor.GREEN + "Вы успешно прокачали ботинки!");
                } else {
                    player.sendMessage(ChatColor.RED + "У Вас недостаточно денег!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
