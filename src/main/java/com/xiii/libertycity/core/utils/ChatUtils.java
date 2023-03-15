package com.xiii.libertycity.core.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ChatUtils {

    public static void broadcast(final String message) {

        LibertyCity.getInstance().getThread().submit(() -> {

            final ChatMessage chatMessage = new ChatMessageLegacy(Component.text(message), ChatTypes.CHAT);

            final WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(chatMessage);

            Bukkit.getOnlinePlayers().stream().filter(player -> LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId()).isVerified).forEach(player -> PacketEvents.getAPI().getProtocolManager().sendPacket(PacketEvents.getAPI().getPlayerManager().getChannel(player), packet));
        });
    }

    public static void multicast(final String message, final Player... players) {

        LibertyCity.getInstance().getThread().submit(() -> {

            final ChatMessage chatMessage = new ChatMessageLegacy(Component.text(message), ChatTypes.CHAT);

            final WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(chatMessage);

            BetterStream.filter(players, player -> LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId()).isVerified).forEach(player -> PacketEvents.getAPI().getProtocolManager().sendPacket(PacketEvents.getAPI().getPlayerManager().getChannel(player), packet));
        });
    }

    public static void multicast(final String message, final Collection<Player> players) {

        LibertyCity.getInstance().getThread().submit(() -> {

            final ChatMessage chatMessage = new ChatMessageLegacy(Component.text(message), ChatTypes.CHAT);

            final WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(chatMessage);

            BetterStream.filter(players, player -> LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId()).isVerified).forEach(player -> PacketEvents.getAPI().getProtocolManager().sendPacket(PacketEvents.getAPI().getPlayerManager().getChannel(player), packet));
        });
    }
}
