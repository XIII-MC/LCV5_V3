package com.xiii.libertycity.manager.profile;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.manager.threads.ProfileThread;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile implements java.io.Serializable{
    private static final long serialVersionUID = 5306742032478905396L;

    //------------------------------------
    public final UUID playerUUID;
    public final String playerName;
    public final transient Player playerEntity;
    private final transient ProfileThread profileThread;
    //------------------------------------

    public Profile(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerEntity = player.getPlayer();
        this.profileThread = LibertyCity.getInstance().getThreadManager().getAvailableProfileThread();
    }

    public UUID getUUID() {
        return playerUUID;
    }

    public String getName() {
        return playerName;
    }

    public Player getPlayer() {
        return playerEntity;
    }

    public ProfileThread getProfileThread() {
        return profileThread;
    }
}
