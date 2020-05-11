package ru.kdev.Jumper.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.kdev.Jumper.Jumper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Players {
    public static Map<UUID, Map<String, Integer>> cache = new HashMap<>();

    public static void cache(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            Connection connection = Jumper.connection;
            PreparedStatement statement;
            try {
                statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
                statement.setString(1, player.getName());
                ResultSet rs = statement.executeQuery();
                Map<String, Integer> map = new HashMap<>();
                while (rs.next()) {
                    if(cache.containsKey(player)) {
                        map.replace("level", rs.getInt("level"));
                        map.replace("xp", rs.getInt("xp"));
                        map.replace("balance", rs.getInt("balance"));
                        map.replace("rebirths", rs.getInt("rebirths"));
                        map.replace("bLevel", rs.getInt("bLevel"));
                    } else {
                        map.put("level", rs.getInt("level"));
                        map.put("xp", rs.getInt("xp"));
                        map.put("balance", rs.getInt("balance"));
                        map.put("rebirths", rs.getInt("rebirths"));
                        map.put("bLevel", rs.getInt("bLevel"));
                    }
                }
                cache.put(player.getUniqueId(), map);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static Integer getLevel(Player player) throws SQLException {
        return cache.get(player.getUniqueId()).get("level");
    }

    public static Integer getBootsLevel(Player player) throws SQLException {
        return cache.get(player.getUniqueId()).get("bLevel");
    }

    public static Integer getXP(Player player) throws SQLException {
        return cache.get(player.getUniqueId()).get("xp");
    }

    public static Integer getNeeds(Player player) throws SQLException {
        return (getLevel(player) * 25) - getXP(player);
    }

    public static Integer getBalance(Player player) throws SQLException {
        return cache.get(player.getUniqueId()).get("balance");
    }

    public static Integer getRebirths(Player player) throws SQLException {
        return cache.get(player.getUniqueId()).get("rebirths");
    }

    public static boolean exists(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        return statement.executeQuery().next();
    }

    public static void createPlayer(Player player) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("INSERT INTO players (nickname) VALUES (?)");
                statement.setString(1, player.getName());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setLevel(Player player, int level) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET level = ? WHERE nickname = ?");
                statement.setInt(1, level);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                cache(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setRebirths(Player player, int level) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET rebirths = ? WHERE nickname = ?");
                statement.setInt(1, level);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                cache(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setBLevel(Player player, int level) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET bLevel = ? WHERE nickname = ?");
                statement.setInt(1, level);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                cache(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setXP(Player player, int xp) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET xp = ? WHERE nickname = ?");
                statement.setInt(1, xp);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                cache(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setBalance(Player player, int balance) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET balance = ? WHERE nickname = ?");
                statement.setInt(1, balance);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                cache(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void addBalance(Player player, int balance) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET balance = balance + ? WHERE nickname = ?");
                statement.setInt(1, balance);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                cache(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void takeBalance(Player player, int balance) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(Jumper.getPlugin(Jumper.class), () -> {
            try {
                Connection connection = Jumper.connection;
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET balance = balance - ? WHERE nickname = ?");
                statement.setInt(1, balance);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                cache(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
