package com.xiii.libertycity.roleplay.items.idcard.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.items.idcard.IDCardManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IDCardHandle implements Data {

    private final IDCardManager idCardManager = new IDCardManager();

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.USE_ITEM) {

            final Player player = packet.getPlayer();
            final ItemStack itemHand = player.getInventory().getItemInMainHand();

            if (itemHand.getType().equals(Material.PAPER)) {

                if (this.idCardManager.verify(itemHand)) ChatUtils.multicast("§aCarte valide!", player);
                else ChatUtils.multicast("§cCarte invalide.", player);
            }
        }
    }

    public void handle(final ServerPlayPacket packet) {
    }
}
