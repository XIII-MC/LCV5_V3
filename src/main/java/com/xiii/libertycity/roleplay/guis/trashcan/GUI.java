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

public class GUI {

    public void openTrashcan(final Player player, final boolean refresh) {

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
