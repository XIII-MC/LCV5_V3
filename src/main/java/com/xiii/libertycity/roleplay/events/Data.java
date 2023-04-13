package com.xiii.libertycity.roleplay.events;

import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;

public interface Data {
    void handle(final ClientPlayPacket packet);

    void handle(final ServerPlayPacket packet);
}
