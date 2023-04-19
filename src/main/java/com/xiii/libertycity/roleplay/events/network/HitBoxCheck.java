package com.xiii.libertycity.roleplay.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.items.handcuffs.utils.Targeter;

public class HitBoxCheck implements Data {

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {

            final WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = packet.getInteractEntity();

            if (wrapperPlayClientInteractEntity.getAction().equals(WrapperPlayClientInteractEntity.InteractAction.ATTACK)) {
                if (!Targeter.getTargetHitBox(packet.getPlayer())) ChatUtils.broadcast("§4HitBox modified.");
                else ChatUtils.broadcast("§2HitBox unmodified.");
            }
        }
    }

    public void handle(final ServerPlayPacket packet) {}
}
