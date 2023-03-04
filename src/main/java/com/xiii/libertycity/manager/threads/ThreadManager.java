package com.xiii.libertycity.manager.threads;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.manager.Initializer;
import com.xiii.libertycity.manager.profile.Profile;
import com.xiii.libertycity.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

public class ThreadManager implements Listener, Initializer {

    //Get a proper thread limit
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    private final List<ProfileThread> profileThreads = new ArrayList<>();

    private final LibertyCity plugin;

    public ThreadManager(LibertyCity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, LibertyCity.getInstance());
    }

    public ProfileThread getAvailableProfileThread() {

        ProfileThread profileThread;

        if (this.profileThreads.size() < MAX_THREADS) {

            profileThread = new ProfileThread();

            this.profileThreads.add(profileThread);

        } else {

            profileThread = this.profileThreads
                    .stream()
                    .min(Comparator.comparing(ProfileThread::getProfileCount))
                    .orElse(MathUtils.randomElement(this.profileThreads));
        }

        if (profileThread == null) {

            LibertyCity.log(Level.SEVERE, "Thread is null. Please restart the server and report this error.");
        }

        return profileThread.incrementAndGet();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {

        Profile profile = this.plugin.getProfileManager().getProfile(e.getPlayer());

        if (profile == null) return;

        ProfileThread profileThread = profile.getProfileThread();

        if (profileThread.getProfileCount() > 1) {

            profileThread.decrement();

            return;
        }

        this.profileThreads.remove(profileThread.shutdownThread());
    }

    @Override
    public void shutdown() {
        this.profileThreads.forEach(ProfileThread::shutdownThread);

        this.profileThreads.clear();
    }
}
