package com.xiii.libertycity.roleplay.guis.trashcan.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.guis.trashcan.GUI;
import com.xiii.libertycity.roleplay.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TrashcanHandle extends ItemUtils implements Data {

    private final GUI trashcanGUI = new GUI();

    public void handle(final ClientPlayPacket packet) {

        final Player player = packet.getPlayer();

        if (packet.getType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {

            final WrapperPlayClientPlayerBlockPlacement wrapperPlayClientPlayerBlockPlacement = packet.getBlockPlacementWrapper();

            final Block block = new Location(player.getWorld(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getX(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getY(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getZ()).getBlock();
            if (compareMaterial(block.getType(), getMaterial(trashcan))) {
                this.trashcanGUI.openTrashcan(player, false);
                packet.getEvent().setCancelled(true);
            }
        }

        if (packet.getType() == PacketType.Play.Client.CLICK_WINDOW) {
            if (packet.getClickWindow().getSlot() == 53) {
                packet.getEvent().setCancelled(true);
                player.closeInventory();
                player.updateInventory();
            }
        }

    }

    public void handle(final ServerPlayPacket packet) {}
}
