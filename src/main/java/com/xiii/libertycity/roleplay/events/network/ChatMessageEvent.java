package com.xiii.libertycity.roleplay.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.core.processors.ClientPlayPacket;
import com.xiii.libertycity.core.processors.ServerPlayPacket;
import com.xiii.libertycity.roleplay.events.Data;

public class ChatMessageEvent implements Data {

    public void handle(ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.CHAT_MESSAGE) {

        }

    }

    public void handle(ServerPlayPacket packet) {

    }
}
