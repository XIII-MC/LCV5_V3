package com.xiii.libertycity.roleplay.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import org.bukkit.Bukkit;

public class TabListEvent implements Data {

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.ADVANCEMENT_TAB) {
            ChatUtils.broadcast("Advancement tab");
        }

        if (packet.getType() == PacketType.Play.Client.TAB_COMPLETE) {
            ChatUtils.broadcast("Tab complete");
        }
    }


    public void handle(final ServerPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER) {

            ChatUtils.broadcast("YES");

            final WrapperPlayServerPlayerListHeaderAndFooter wrapperPlayServerPlayerListHeaderAndFooter = packet.getTabList();

            Bukkit.broadcastMessage(wrapperPlayServerPlayerListHeaderAndFooter.getHeaderComponent() + " " + wrapperPlayServerPlayerListHeaderAndFooter.getFooterComponent());
        }
    }
}
