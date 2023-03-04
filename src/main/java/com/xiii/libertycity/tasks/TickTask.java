package com.xiii.libertycity.tasks;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.utils.MathUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class TickTask extends BukkitRunnable {

    private final LibertyCity plugin;

    public TickTask(LibertyCity plugin) {
        this.plugin = plugin;
    }

    private static int ticks;
    private static double tps = 20.0D;
    private static long tickTime, lastLagSpike;
    private int tpsTicks = 20;
    private long lastTime = System.currentTimeMillis();
    private long currentSec;

    @Override
    public void run() {

        //Increment tick
        ticks++;

        //Get the current system time
        final long currentTime = System.currentTimeMillis();

        //Handle server TPS and tick time
        server:
        {

            //The server's probably laggy at this early stage
            if (ticks < 100) break server;

            tickTime = currentTime - this.lastTime;

            this.lastTime = currentTime;

            final long sec = (currentTime / 1000L);

            if (this.currentSec == sec) {

                this.tpsTicks++;

            } else {

                this.currentSec = sec;

                tps = Math.min(MathUtils.decimalRound((tps + this.tpsTicks) / 2.0D, 2), 20.0D);

                this.tpsTicks = 1;
            }

            //Handle lag spikes
            if (tickTime >= 1050L
                    || tps <= 14.5) {

                lastLagSpike = currentTime;
            }
        }
    }

    public static double getTPS() {
        return tps;
    }

    public static int getCurrentTick() {
        return Math.abs(ticks);
    }

    public static long getTickTime() {
        return tickTime;
    }

    public static long getLastLagSpike() {
        return MathUtils.elapsed(lastLagSpike);
    }
}
