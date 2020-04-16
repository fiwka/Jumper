package ru.kdev.Jumper.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.kdev.Jumper.Jumper;

import java.io.UnsupportedEncodingException;

public class ZoneManager {
    Jumper jumper = Jumper.getPlugin(Jumper.class);

    public void createZone(String name, Double x, Double y, Double z, Float yaw, Float pitch, Integer level, World world) throws UnsupportedEncodingException {
        jumper.getZones().set("zones." + name + ".world", world.getName());
        jumper.getZones().set("zones." + name + ".x", x);
        jumper.getZones().set("zones." + name + ".y", y);
        jumper.getZones().set("zones." + name + ".z", z);
        jumper.getZones().set("zones." + name + ".yaw", yaw);
        jumper.getZones().set("zones." + name + ".pitch", pitch);
        jumper.getZones().set("zones." + name + ".level", level);
        jumper.saveZones();
        jumper.getLogger().info("Created zone " + name);
    }

    public void setFinalPoint(String name, Double x, Double y, Double z, World world, Double distance, Integer exp) throws UnsupportedEncodingException {
        jumper.getZones().set("zones." + name + ".finalPoint.world", world.getName());
        jumper.getZones().set("zones." + name + ".finalPoint.x", x);
        jumper.getZones().set("zones." + name + ".finalPoint.y", y);
        jumper.getZones().set("zones." + name + ".finalPoint.z", z);
        jumper.getZones().set("zones." + name + ".finalPoint.distance", distance);
        jumper.getZones().set("zones." + name + ".finalPoint.exp", exp);
        jumper.saveZones();
        jumper.getLogger().info("Changed zone " + name + " final point");
    }

    public void setSpawnZone(String name, Double x, Double y, Double z, Float yaw, Float pitch, World world) throws UnsupportedEncodingException {
        jumper.getZones().set("zones." + name + ".world", world.getName());
        jumper.getZones().set("zones." + name + ".x", x);
        jumper.getZones().set("zones." + name + ".y", y);
        jumper.getZones().set("zones." + name + ".z", z);
        jumper.getZones().set("zones." + name + ".yaw", yaw);
        jumper.getZones().set("zones." + name + ".pitch", pitch);
        jumper.saveZones();
        jumper.getLogger().info("Changed zone spawn " + name);
    }

    public boolean existsZone(String name) throws UnsupportedEncodingException {
        return jumper.getZones().contains("zones." + name);
    }

    public Location getFinalPointZone(String name) throws UnsupportedEncodingException {
        return new Location(
                Bukkit.getWorld(jumper.getZones().getString("zones." + name + ".finalPoint.world")),
                Double.parseDouble(jumper.getZones().getString("zones." + name + ".finalPoint.x")),
                Double.parseDouble(jumper.getZones().getString("zones." + name + ".finalPoint.y")),
                Double.parseDouble(jumper.getZones().getString("zones." + name + ".finalPoint.z"))
        );
    }

    public Double getFinalPointDistanceZone(String name) throws UnsupportedEncodingException {
        return Double.parseDouble(jumper.getZones().getString("zones." + name + ".finalPoint.distance"));
    }

    public int getFinalPointExpZone(String name) throws UnsupportedEncodingException {
        return jumper.getZones().getInt("zones." + name + ".finalPoint.exp");
    }

    public int getMinimumLevelZone(String name) throws UnsupportedEncodingException {
        return jumper.getZones().getInt("zones." + name + ".level");
    }

    public void removeZone(String name) throws UnsupportedEncodingException {
        jumper.getZones().set("zones." + name, null);
        jumper.saveZones();
        jumper.getLogger().info("Removed zone " + name);
    }

    public void teleportToZone(Player player, String name) throws UnsupportedEncodingException {
        Location loc = new Location(
                Bukkit.getWorld(jumper.getZones().getString("zones." + name + ".world")),
                jumper.getZones().getDouble("zones." + name + ".x"),
                jumper.getZones().getDouble("zones." + name + ".y"),
                jumper.getZones().getDouble("zones." + name + ".z"),
                (float) jumper.getZones().getDouble("zones." + name + ".yaw"),
                (float) jumper.getZones().getDouble("zones." + name + ".pitch")
        );
        player.teleport(loc);
    }
}
