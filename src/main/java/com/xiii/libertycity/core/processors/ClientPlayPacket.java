package com.xiii.libertycity.core.processors;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import org.bukkit.entity.Player;

public class ClientPlayPacket {

    /*
    Internals - Basic cache
     */
    private final PacketType.Play.Client packet;
    private final long timeStamp;
    private final Player player;
    private final PacketPlayReceiveEvent packetEvent;

    /*
    Chat - Messages/Commands cache
     */
    private WrapperPlayClientChatMessage chatWrapper;

    /*
    Blocks - Blocks cache
     */
    private WrapperPlayClientPlayerBlockPlacement blockPlacement;

    /*
    Inventories - Windows cache
     */
    private WrapperPlayClientClickWindow clickWindow;

    /*
    Movement - Flying cache
    */
    private WrapperPlayClientPlayerPosition positionWrapper;
    private WrapperPlayClientPlayerPositionAndRotation positionLookWrapper;
    private WrapperPlayClientPlayerRotation lookWrapper;

    public ClientPlayPacket(PacketType.Play.Client packet, PacketPlayReceiveEvent e, long timeStamp) {
        this.timeStamp = timeStamp;
        this.player = (Player) e.getPlayer();
        this.packetEvent = e;

        switch (this.packet = packet) {

            case PLAYER_POSITION:

                this.positionWrapper = new WrapperPlayClientPlayerPosition(e);

                break;

            case PLAYER_POSITION_AND_ROTATION:

                this.positionLookWrapper = new WrapperPlayClientPlayerPositionAndRotation(e);

                break;

            case PLAYER_ROTATION:

                this.lookWrapper = new WrapperPlayClientPlayerRotation(e);

                break;

            case CHAT_MESSAGE:

                this.chatWrapper = new WrapperPlayClientChatMessage(e);

                break;

            case PLAYER_BLOCK_PLACEMENT:

                this.blockPlacement = new WrapperPlayClientPlayerBlockPlacement(e);

                break;

            case CLICK_WINDOW:

                this.clickWindow = new WrapperPlayClientClickWindow(e);

                break;
        }

    }

    public WrapperPlayClientPlayerPosition getPositionWrapper() {
        return positionWrapper;
    }

    public WrapperPlayClientPlayerPositionAndRotation getPositionLookWrapper() {
        return positionLookWrapper;
    }

    public WrapperPlayClientPlayerRotation getLookWrapper() {
        return lookWrapper;
    }

    public WrapperPlayClientChatMessage getChatWrapper() {
        return chatWrapper;
    }

    public WrapperPlayClientPlayerBlockPlacement getBlocKPlacementWrapper() {
        return blockPlacement;
    }

    public WrapperPlayClientClickWindow getClickWindow() {
        return clickWindow;
    }

    public PacketType.Play.Client getType() {
        return packet;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Player getPlayer() {
        return player;
    }

    public PacketPlayReceiveEvent getEvent() {
        return packetEvent;
    }
}
