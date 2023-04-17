package com.xiii.libertycity.roleplay.items.wallet.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.items.wallet.WalletManager;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

public class WalletHandle implements Data {

    private final WalletManager walletManager = new WalletManager();

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.USE_ITEM) {

            final Player player = packet.getPlayer();

            if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND)) {

                this.walletManager.openWallet(player.getInventory().getItemInMainHand(), player);

                //TODO: Change to real wallet modded ID
            }
        }

        if (packet.getType() == PacketType.Play.Client.CLICK_WINDOW) {

            final Player player = packet.getPlayer();

            //TODO: Change to real wallet modded item's id
            if (player.getOpenInventory().getTopInventory().getTitle().contains("Porte feuille")) {

                if (!player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND) || !Objects.equals(this.walletManager.getDecryptedID(player.getInventory().getItemInMainHand().getItemMeta().getLore()), this.walletManager.getCachedDecryptedID())) {
                    packet.getEvent().setCancelled(true);
                    player.closeInventory();
                    player.updateInventory();
                    ChatUtils.multicast("§cUne erreur est survenue. Veuillez garder le porte feuille en §cmain.", player);
                }

                //TODO: Change to real wallet modded item's id
                if (SpigotConversionUtil.toBukkitItemStack(packet.getClickWindow().getCarriedItemStack()).getType().equals(Material.DIAMOND)) {

                    packet.getEvent().setCancelled(true);
                    player.updateInventory();
                    ChatUtils.multicast("§cUne erreur est survenue. Vous ne pouvez pas déplacer un porte feuille maintenant.", player);
                }
            }
        }

        if (packet.getType() == PacketType.Play.Client.CLOSE_WINDOW) {

            final Player player = packet.getPlayer();

            //TODO: Change to real wallet modded item's id
            if (player.getOpenInventory().getTopInventory().getTitle().contains("Porte feuille") && player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND) && this.walletManager.getDecryptedID(player.getInventory().getItemInMainHand().getItemMeta().getLore()).equalsIgnoreCase(this.walletManager.getCachedDecryptedID())) {

                this.walletManager.closeWallet(player.getOpenInventory().getTopInventory(), player);
            }
        }
    }

    public void handle(final ServerPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Server.OPEN_WINDOW) {

            final Player player = packet.getPlayer();

            if (player.getOpenInventory().getTopInventory().getTitle().contains("Porte feuille")) {

                if (!this.walletManager.getDecryptedID(player.getInventory().getItemInMainHand().getItemMeta().getLore()).equalsIgnoreCase(this.walletManager.getCachedDecryptedID())) {

                    packet.getEvent().setCancelled(true);
                    player.closeInventory();
                    ChatUtils.multicast("§cUne erreur est survenue. Veuillez garder le porte feuille en §cmain.", player);
                }
            }
        }
    }
}
