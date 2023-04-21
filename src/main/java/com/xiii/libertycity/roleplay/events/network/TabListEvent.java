package com.xiii.libertycity.roleplay.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.roleplay.events.Data;

import java.util.Objects;

public class TabListEvent implements Data {

    public void handle(final ClientPlayPacket packet) {}

    public void handle(final ServerPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Server.PLAYER_INFO) {

            final WrapperPlayServerPlayerInfo wrapperPlayServerPlayerInfo = packet.getPlayerInfo();

            if (Objects.equals(wrapperPlayServerPlayerInfo.getAction(), WrapperPlayServerPlayerInfo.Action.ADD_PLAYER)) {
                packet.getEvent().setCancelled(true);
            }
        }
    }
}
