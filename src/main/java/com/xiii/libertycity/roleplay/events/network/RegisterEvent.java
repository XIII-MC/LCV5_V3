package com.xiii.libertycity.roleplay.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.processors.ClientPlayPacket;
import com.xiii.libertycity.core.processors.ServerPlayPacket;
import com.xiii.libertycity.roleplay.events.Data;
import org.bukkit.entity.Player;

public class RegisterEvent implements Data {

    public void handle(ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.CHAT_MESSAGE) {

            final WrapperPlayClientChatMessage wrapper = packet.getChatWrapper();

            final Player player = packet.getPlayer();

            final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player);

            if (wrapper.getMessage().equalsIgnoreCase("back")) {

                player.sendMessage("Previous saved message was: " + profile.lastMessage);

                packet.getEvent().setCancelled(true);

                player.sendMessage("Was packed canceled: " + packet.getEvent().isCancelled());

                return;
            }

            profile.lastMessage = wrapper.getMessage();
            player.sendMessage("Message saved as: " + profile.lastMessage);
        }
    }

    public void handle(ServerPlayPacket packet) {

    }

}
