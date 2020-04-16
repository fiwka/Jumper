package ru.kdev.Jumper.database;

import org.bukkit.entity.Player;
import ru.kdev.Jumper.Jumper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Players {
    public static Integer getLevel(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        ResultSet rs = statement.executeQuery();
        int returning = 0;
        while (rs.next()) {
            returning = rs.getInt("level");
        }
        return returning;
    }

    public static Integer getBootsLevel(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        ResultSet rs = statement.executeQuery();
        int returning = 0;
        while (rs.next()) {
            returning = rs.getInt("bLevel");
        }
        return returning;
    }

    public static Integer getXP(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        ResultSet rs = statement.executeQuery();
        int returning = 0;
        while (rs.next()) {
            returning = rs.getInt("xp");
        }
        return returning;
    }

    public static Integer getNeeds(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        ResultSet rs = statement.executeQuery();
        int returning = 0;
        while (rs.next()) {
            returning = (rs.getInt("level") * 25) - getXP(player);
        }
        return returning;
    }

    public static Integer getBalance(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        ResultSet rs = statement.executeQuery();
        int returning = 0;
        while (rs.next()) {
            returning = rs.getInt("balance");
        }
        return returning;
    }

    public static Integer getRebirths(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        ResultSet rs = statement.executeQuery();
        int returning = 0;
        while (rs.next()) {
            returning = rs.getInt("rebirths");
        }
        return returning;
    }

    public static boolean exists(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE nickname = ?");
        statement.setString(1, player.getName());
        return statement.executeQuery().next();
    }

    public static void createPlayer(Player player) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO players (nickname) VALUES (?)");
        statement.setString(1, player.getName());
        statement.executeUpdate();
    }

    public static void setLevel(Player player, int level) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET level = ? WHERE nickname = ?");
        statement.setInt(1, level);
        statement.setString(2, player.getName());
        statement.executeUpdate();
    }

    public static void setRebirths(Player player, int level) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET rebirths = ? WHERE nickname = ?");
        statement.setInt(1, level);
        statement.setString(2, player.getName());
        statement.executeUpdate();
    }

    public static void setBLevel(Player player, int level) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET bLevel = ? WHERE nickname = ?");
        statement.setInt(1, level);
        statement.setString(2, player.getName());
        statement.executeUpdate();
    }

    public static void setXP(Player player, int xp) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET xp = ? WHERE nickname = ?");
        statement.setInt(1, xp);
        statement.setString(2, player.getName());
        statement.executeUpdate();
    }

    public static void setBalance(Player player, int balance) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET balance = ? WHERE nickname = ?");
        statement.setInt(1, balance);
        statement.setString(2, player.getName());
        statement.executeUpdate();
    }

    public static void addBalance(Player player, int balance) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET balance = balance + ? WHERE nickname = ?");
        statement.setInt(1, balance);
        statement.setString(2, player.getName());
        statement.executeUpdate();
    }

    public static void takeBalance(Player player, int balance) throws SQLException {
        Connection connection = Jumper.connection;
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET balance = balance - ? WHERE nickname = ?");
        statement.setInt(1, balance);
        statement.setString(2, player.getName());
        statement.executeUpdate();
    }
}
