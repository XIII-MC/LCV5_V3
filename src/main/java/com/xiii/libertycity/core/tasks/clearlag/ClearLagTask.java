package com.xiii.libertycity.core.tasks.clearlag;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.utils.BossBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearLagTask extends BukkitRunnable {

    private final LibertyCity plugin;

    public ClearLagTask(final LibertyCity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskLater(plugin, () -> BossBarUtils.setMessage("§fLes éboueurs ramasseront toutes les ordures dans §61 minute§f!", 60, true, 100, BarColor.YELLOW, BarStyle.SEGMENTED_6), 20*60);
        Bukkit.getScheduler().runTaskLater(plugin, () -> BossBarUtils.setMessage("§fLes éboueurs ramasseront toutes les ordures dans §630 secondes§f!", 30, false, null, BarColor.YELLOW, BarStyle.SEGMENTED_6), 20*90);
        Bukkit.getScheduler().runTaskLater(plugin, () -> BossBarUtils.setMessage("§fLes éboueurs ramasseront toutes les ordures dans §610 secondes§f!", 10, false, null, BarColor.YELLOW, BarStyle.SEGMENTED_6), 20*110);
        Bukkit.getScheduler().runTaskLater(plugin, () -> BossBarUtils.setMessage("§fLes éboueurs onts ramassé §6" + ClearLag.clearLag("world", true) + "§f ordures!", 15, true, 15, BarColor.YELLOW, BarStyle.SEGMENTED_6), 20*120);
    }
}
