package com.xiii.libertycity.core.listeners;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    private final LibertyCity plugin;

    public ProfileListener(LibertyCity plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();

        if (FileManager.profileExists(player)) {

            FileManager.readProfile(player);

            final Profile profile = this.plugin.getProfileManager().getProfile(player);

            profile.initialize(player);
        }

        this.plugin.getProfileManager().createProfile(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {

        final Player player = e.getPlayer();

        final Profile profile = this.plugin.getProfileManager().getProfile(player);

        profile.getProfileThread().execute(() -> FileManager.saveProfile(profile));

        this.plugin.getThreadManager().removeProfile(player);
    }
}
