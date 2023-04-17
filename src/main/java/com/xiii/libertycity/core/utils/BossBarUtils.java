package com.xiii.libertycity.core.utils;

import com.xiii.libertycity.LibertyCity;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class BossBarUtils {

    private static final BossBar bossBar = LibertyCity.getInstance().getBossBar();
    private static double cachedProgress;
    private static BukkitTask progressBar;

    public static void setMessage(final String message, final int lastSeconds, final boolean resetProgress, final Integer finalReset, final BarColor bossBarColor, final BarStyle bossBarStyle) {

        bossBar.setColor(bossBarColor);
        bossBar.setStyle(bossBarStyle);
        bossBar.setTitle(message);

        bossBar.setVisible(true);

        if (resetProgress) {

            bossBar.setProgress(1);
            final double temp = 20 * lastSeconds;
            cachedProgress = 1 / temp;

            progressBar = new BukkitRunnable() {

                @Override
                public void run() {
                    if (bossBar.getProgress() >= cachedProgress)
                        bossBar.setProgress(bossBar.getProgress() - cachedProgress);
                }

            }.runTaskTimerAsynchronously(LibertyCity.getInstance(), 1, 0L);
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(LibertyCity.getInstance(), () -> {
            bossBar.setVisible(false);
            bossBar.setProgress(1);
            progressBar = null;
        }, 20L *(resetProgress ? finalReset : lastSeconds));
    }
}
