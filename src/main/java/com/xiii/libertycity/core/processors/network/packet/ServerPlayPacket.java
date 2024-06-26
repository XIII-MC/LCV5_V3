package com.xiii.libertycity.core.processors.network.packet;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import org.bukkit.entity.Player;

public class ServerPlayPacket {

    /*
    Internals - Basic cache
     */
    private final PacketType.Play.Server packet;
    private final long timeStamp;
    private final Player player;
    private final PacketPlaySendEvent packetEvent;

    /*
    TabList - Header & Footer
     */
    private WrapperPlayServerPlayerInfo playerInfo;

    public ServerPlayPacket(PacketType.Play.Server packet, PacketPlaySendEvent e, long timeStamp) {
        this.timeStamp = timeStamp;
        this.player = (Player) e.getPlayer();
        this.packetEvent = e;

        switch (this.packet = packet) {

            case PLAYER_INFO:

                this.playerInfo = new WrapperPlayServerPlayerInfo(e);

                break;
        }

    }

    public PacketType.Play.Server getType() {
        return packet;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Player getPlayer() {
        return player;
    }

    public PacketPlaySendEvent getEvent() {
        return packetEvent;
    }

    public WrapperPlayServerPlayerInfo getPlayerInfo() {
        return playerInfo;
    }
}
