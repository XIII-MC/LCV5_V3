package com.xiii.libertycity.core.listeners;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.enums.MsgType;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.core.utils.TabListUtils;
import com.xiii.libertycity.roleplay.events.network.RegisterEvent;
import com.xiii.libertycity.roleplay.utils.NameTags;
import org.bukkit.Bukkit;
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
        LibertyCity.getInstance().getThread().execute(() -> {

            final Player player = e.getPlayer();

            if (FileManager.profileExists(player.getUniqueId())) {

                FileManager.readProfile(player.getUniqueId());

                final Profile profile = this.plugin.getProfileManager().getProfile(player.getUniqueId());

                profile.initialize(player);
            } else RegisterEvent.initialize(player);

            TabListUtils.emptyTabList(player);

            NameTags.hideNameTag(player);

            this.plugin.getProfileManager().createProfile(player);

            if (player.hasPermission("LibertyCity.bypass.join")) Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission("LibertyCity.alert.staff")).forEach(players -> ChatUtils.multicast(MsgType.STAFF.getMessage() + player.getName() + "§7 c'est connecté.", players));
        });
        e.setJoinMessage("");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLeave(PlayerQuitEvent e) {
        LibertyCity.getInstance().getThread().execute(() -> {

            final Player player = e.getPlayer();

            final Profile profile = this.plugin.getProfileManager().getProfile(player.getUniqueId());

            if (!profile.isVerified) RegisterEvent.shutdown(player);
            else FileManager.saveProfile(profile);

            this.plugin.getBossBar().removePlayer(player);

            this.plugin.getProfileManager().removeProfile(player.getUniqueId());

            this.plugin.getThreadManager().removeProfile(player.getUniqueId());

            if (player.hasPermission("LibertyCity.bypass.join")) Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission("LibertyCity.alert.staff")).forEach(players -> ChatUtils.multicast(MsgType.STAFF.getMessage() + player.getName() + "§7 c'est déconnecté.", players));
        });
        e.setQuitMessage("");
    }
}
