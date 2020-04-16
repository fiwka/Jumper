package ru.kdev.Jumper;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import ru.kdev.Jumper.commands.*;
import ru.kdev.Jumper.database.Players;
import ru.kdev.Jumper.listener.JumperListener;
import ru.kdev.Jumper.utils.IntFormatter;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Jumper extends JavaPlugin {
    private File zonesFile = null;
    private FileConfiguration zones = null;
    public static Map<Player, ArrayList<Team>> scoreboards = new HashMap<>();
    public static Map<Player, String> playing = new HashMap<>();
    public static Connection connection;
    private BukkitTask task;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        zonesFile = new File(this.getDataFolder(), "zones.yml");
        this.saveZones();
        Bukkit.getPluginManager().registerEvents(new JumperListener(), this);
        try {
            this.connect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.getCommand("jadmin").setExecutor(new JAdmin());
        this.getCommand("zones").setExecutor(new Zones());
        this.getCommand("rebirth").setExecutor(new Rebirth());
        this.getCommand("upboots").setExecutor(new UpBoots());
        this.getCommand("balance").setExecutor(new Balance());
        this.getCommand("pay").setExecutor(new Pay());
        this.getCommand("economy").setExecutor(new Economy());
        task = new BukkitRunnable() {
            @Override
            public void run() {
                scoreboards.forEach((player, v) -> {
                    try {
                        v.get(0).setPrefix(cColor(" &7◾ Уровень: &f" + IntFormatter.withSuffix(Players.getLevel(player))));
                        v.get(1).setPrefix(cColor(" &7◾ XP: &f" + IntFormatter.withSuffix(Players.getXP(player))));
                        v.get(2).setPrefix(cColor(" &7◾ Осталось: &f" + IntFormatter.withSuffix(Players.getNeeds(player))));
                        v.get(3).setPrefix(cColor(" &7◾ Баланс: &a" + IntFormatter.withSuffix(Players.getBalance(player)) + "&c$"));
                        v.get(4).setPrefix(cColor(" &7◾ Ребитхи: &c" + IntFormatter.withSuffix(Players.getRebirths(player))));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                for(Player player : Bukkit.getOnlinePlayers()) {
                    try {
                        if(Players.exists(player)) {
                            if(Players.getXP(player) >= Players.getLevel(player) * 25) {
                                if(Players.getLevel(player) == 50) {
                                    player.sendMessage(ChatColor.RED + "У Вас максимальный уровень! Вы можете сделать ребитх /rebirth");
                                    Players.setXP(player, 0);
                                    return;
                                }
                                Players.setLevel(player, Players.getLevel(player) + 1);
                                Players.setXP(player, 0);
                                player.sendTitle(ChatColor.BLUE + "Уровень повышен!", ChatColor.GREEN + "Поздравляем!", 20, 100, 20);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L);
    }

    public static List<Entity> getNearbyEntities(Location where, int range) {
        List<Entity> found = new ArrayList<Entity>();

        for (Entity entity : where.getWorld().getEntities()) {
            if (isInBorder(where, entity.getLocation(), range)) {
                found.add(entity);
            }
        }
        return found;
    }

    public static boolean isInBorder(Location center, Location notCenter, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();

        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }
        return true;
    }

    @Override
    public void onDisable() {
        task.cancel();
    }

    private String cColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        String host = getConfig().getString("connection.host");
        String database = getConfig().getString("connection.database");
        String username = getConfig().getString("connection.user");
        String password = getConfig().getString("connection.password");
        int port = getConfig().getInt("connection.port");
        Class.forName("com.mysql.jdbc.Driver");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setPort(port);
        dataSource.setDatabaseName(database);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setServerTimezone("UTC");
        connection = dataSource.getConnection();
    }

    public void reloadZones() throws UnsupportedEncodingException {
        if (zonesFile == null) {
            zonesFile = new File(getDataFolder(), "zones.yml");
        }
        zones = YamlConfiguration.loadConfiguration(zonesFile);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(this.getResource("zones.yml"), "UTF8");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            zones.setDefaults(defConfig);
        }
    }

    public FileConfiguration getZones() throws UnsupportedEncodingException {
        if (zones == null) {
            reloadZones();
        }
        return zones;
    }

    public void saveZones() {
        if (zones == null || zonesFile == null) {
            return;
        }
        try {
            getZones().save(zonesFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + zonesFile, ex);
        }
    }
}
