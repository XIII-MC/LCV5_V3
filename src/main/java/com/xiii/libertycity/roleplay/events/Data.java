package com.xiii.libertycity.roleplay.events;

import com.xiii.libertycity.core.processors.ClientPlayPacket;
import com.xiii.libertycity.core.processors.ServerPlayPacket;

public interface Data {
    void handle(ClientPlayPacket packet);

    void handle(ServerPlayPacket packet);
}
