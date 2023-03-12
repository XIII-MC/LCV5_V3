package com.xiii.libertycity.core.listeners;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.roleplay.events.network.RegisterEvent;
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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        LibertyCity.getInstance().getThread().submit(() -> {

            final Player player = e.getPlayer();

            if (FileManager.profileExists(player.getUniqueId())) {

                FileManager.readProfile(player.getUniqueId());

                final Profile profile = this.plugin.getProfileManager().getProfile(player.getUniqueId());

                profile.initialize(player);
            } else RegisterEvent.initialize(player);

            this.plugin.getProfileManager().createProfile(player);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLeave(PlayerQuitEvent e) {
        LibertyCity.getInstance().getThread().submit(() -> {

            final Player player = e.getPlayer();

            final Profile profile = this.plugin.getProfileManager().getProfile(player.getUniqueId());

            if (!profile.isVerified) RegisterEvent.shutdown(player);
            else FileManager.saveProfile(profile);

            this.plugin.getProfileManager().removeProfile(player.getUniqueId());

            this.plugin.getThreadManager().removeProfile(player.getUniqueId());
        });
    }
}
