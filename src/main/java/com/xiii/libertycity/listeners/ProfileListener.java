package com.xiii.libertycity.listeners;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.manager.files.FileManager;
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

        FileManager.readProfile(player);

        this.plugin.getProfileManager().createProfile(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e) {

        final Player player = e.getPlayer();

        FileManager.saveProfile(this.plugin.getProfileManager().getProfile(player));

        this.plugin.getProfileManager().removeProfile(player);
    }
}
