package com.xiii.libertycity.core.manager.profile;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.threads.ProfileThread;
import com.xiii.libertycity.core.processors.ClientPlayPacket;
import com.xiii.libertycity.core.processors.ServerPlayPacket;
import com.xiii.libertycity.core.utils.TaskUtils;
import com.xiii.libertycity.roleplay.events.network.RegisterEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile implements java.io.Serializable{
    private static final long serialVersionUID = 5306742032478905396L;

    //------------------------------------
    public final UUID playerUUID;
    public final String playerName;
    public transient Player playerEntity;
    private transient ProfileThread profileThread;
    //------------------------------------
    private transient RegisterEvent registerEvent;
    //------------------------------------
    public String lastMessage;

    public Profile(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerEntity = player.getPlayer();
        this.profileThread = LibertyCity.getInstance().getThreadManager().getAvailableProfileThread();

        this.registerEvent = new RegisterEvent();
    }

    public void initialize(Player player) {
        this.profileThread = LibertyCity.getInstance().getThreadManager().getAvailableProfileThread();
        this.registerEvent = new RegisterEvent();
        this.playerEntity = player;
    }


    public void handleClientNetty(ClientPlayPacket packet) {

        this.registerEvent.handle(packet);
    }

    public void handleServerNetty(ServerPlayPacket packet) {

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
