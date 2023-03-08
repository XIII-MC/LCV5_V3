package com.xiii.libertycity.core.manager.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileThread {

    private final ExecutorService thread = Executors.newSingleThreadExecutor();

    private int profileCount;

    public void execute(Runnable runnable) {

        if (this.thread.isShutdown()) return;

        this.thread.execute(runnable);
    }

    public int getProfileCount() {
        return this.profileCount;
    }

    public ProfileThread incrementAndGet() {

        this.profileCount++;

        return this;
    }

    public void decrement() {
        this.profileCount--;
    }

    public ProfileThread shutdownThread() {

        this.thread.shutdownNow();

        return this;
    }
}
