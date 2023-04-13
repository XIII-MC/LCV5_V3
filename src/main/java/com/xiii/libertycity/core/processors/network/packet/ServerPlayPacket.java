package com.xiii.libertycity.core.processors.network.packet;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import org.bukkit.entity.Player;

public class ServerPlayPacket {

    /*
    Internals - Basic cache
     */
    private final PacketType.Play.Server packet;
    private final long timeStamp;
    private final Player player;
    private final PacketPlaySendEvent packetEvent;

    public ServerPlayPacket(PacketType.Play.Server packet, PacketPlaySendEvent e, long timeStamp) {
        this.timeStamp = timeStamp;
        this.player = (Player) e.getPlayer();
        this.packetEvent = e;

        this.packet = packet;

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
}
