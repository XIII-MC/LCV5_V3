package com.xiii.libertycity.roleplay.guis.trashcan;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.InventoryUtils;
import com.xiii.libertycity.roleplay.events.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Data {

    public void handle(final ClientPlayPacket packet) {

        final Player player = packet.getPlayer();

        if (packet.getType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {

            final WrapperPlayClientPlayerBlockPlacement wrapperPlayClientPlayerBlockPlacement = packet.getBlockPlacementWrapper();

            final Block block = new Location(player.getWorld(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getX(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getY(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getZ()).getBlock();
            if (block.getType() == Material.DIAMOND_BLOCK) {
                openTrashcan(player, false);
                packet.getEvent().setCancelled(true);
            }
            //TODO: Change to real trash can block ID
        }

        if (packet.getType() == PacketType.Play.Client.CLICK_WINDOW) {
            if (packet.getClickWindow().getSlot() == 53) {
                packet.getEvent().setCancelled(true);
                player.closeInventory();
                player.updateInventory();
            }
        }

    }

    public void handle(final ServerPlayPacket packet) {
    }

    private void openTrashcan(final Player player, final boolean refresh) {

        Inventory inv;

        if (!refresh) inv = Bukkit.createInventory(null, 54, "§8Poubelle");
        else inv = player.getOpenInventory().getTopInventory();

        for (int i = 0; i <= 53; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }
        inv.setItem(53, InventoryUtils.createItem(Material.BARRIER, 1, "§cVider et fermer la poubelle", ""));

        if (!refresh) {
            player.closeInventory();
            player.openInventory(inv);
        } else player.updateInventory();
    }
}
