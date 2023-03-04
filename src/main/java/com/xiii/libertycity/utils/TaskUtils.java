package com.xiii.libertycity.utils;

import com.xiii.libertycity.LibertyCity;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class TaskUtils {

    private TaskUtils() {
    }

    public static BukkitTask taskTimer(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimer(LibertyCity.getInstance(), runnable, delay, interval);
    }

    public static BukkitTask taskTimerAsync(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(LibertyCity.getInstance(), runnable, delay, interval);
    }

    public static BukkitTask task(Runnable runnable) {
        return Bukkit.getScheduler().runTask(LibertyCity.getInstance(), runnable);
    }

    public static BukkitTask taskAsync(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(LibertyCity.getInstance(), runnable);
    }

    public static BukkitTask taskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(LibertyCity.getInstance(), runnable, delay);
    }

    public static BukkitTask taskLaterAsync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(LibertyCity.getInstance(), runnable, delay);
    }
}
