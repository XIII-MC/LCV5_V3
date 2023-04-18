package com.xiii.libertycity.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflection {

    public static Object getHandle(final Player player) {
        try {
            Class<?> craftPlayer = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftPlayer");
            return craftPlayer.cast(player).getClass().getMethod("getHandle").invoke(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
