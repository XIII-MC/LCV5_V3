package com.xiii.libertycity.core.tasks;

import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.utils.MathUtils;

import java.util.TimerTask;

public class TickTask extends TimerTask {

    private final Profile profile;

    public TickTask(Profile profile) {
        this.profile = profile;
    }

    public void run() {

        this.profile.getProfileThread().execute(() -> {

            //Increment tick
            this.profile.ticks++;

            //Get the current system time
            final long currentTime = System.currentTimeMillis();

            //Handle server TPS and tick time
            server:
            {

                //The server's probably laggy at this early stage
                if (this.profile.ticks < 100) break server;

                this.profile.tickTime = currentTime - this.profile.lastTime;

                this.profile.lastTime = currentTime;

                final long sec = (currentTime / 1000L);

                if (this.profile.currentSec == sec) {

                    this.profile.tpsTicks++;

                } else {

                    this.profile.currentSec = sec;

                    this.profile.tps = Math.min(MathUtils.decimalRound((this.profile.tps + this.profile.tpsTicks) / 2.0D, 2), 20.0D);

                    this.profile.tpsTicks = 1;
                }

                //Handle lag spikes
                if (this.profile.tickTime >= 1050L
                        || this.profile.tps <= 14.5) {

                    this.profile.lastLagSpike = currentTime;
                }
            }
        });
    }
}
