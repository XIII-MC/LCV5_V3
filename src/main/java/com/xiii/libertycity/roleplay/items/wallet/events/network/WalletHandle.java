package com.xiii.libertycity.roleplay.items.wallet.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.guis.atm.GUI;
import com.xiii.libertycity.roleplay.items.wallet.WalletManager;
import com.xiii.libertycity.roleplay.utils.ItemUtils;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class WalletHandle extends ItemUtils implements Data {

    private final WalletManager walletManager = new WalletManager();

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.USE_ITEM) {

            final Player player = packet.getPlayer();

            if (compareMaterial(player.getInventory().getItemInMainHand().getType(), getMaterial(wallet))) {

                this.walletManager.openWallet(player.getInventory().getItemInMainHand(), player);
            }
        }

        if (packet.getType() == PacketType.Play.Client.CLICK_WINDOW) {

            final Player player = packet.getPlayer();

            if (player.getOpenInventory().getTopInventory().getTitle().contains("Porte feuille")) {

                if (!compareMaterial(player.getInventory().getItemInMainHand().getType(), getMaterial(wallet)) || !Objects.equals(this.walletManager.getDecryptedID(player.getInventory().getItemInMainHand().getItemMeta().getLore()), this.walletManager.getCachedDecryptedID())) {
                    packet.getEvent().setCancelled(true);
                    player.closeInventory();
                    player.updateInventory();
                    ChatUtils.multicast("§cUne erreur est survenue. Veuillez garder le porte feuille en §cmain.", player);
                }

                final ItemStack itemStack = getItemStack(player.getOpenInventory(), packet.getClickWindow().getSlot());

                if ((!isCard(itemStack) && !GUI.isMoneyItem(itemStack) && !itemStack.getType().equals(Material.AIR)) || compareMaterial(itemStack.getType(), getMaterial(wallet))) {

                    packet.getEvent().setCancelled(true);
                    player.updateInventory();
                    ChatUtils.multicast("§cUne erreur est survenue. Vous ne pouvez pas déplacer cet §cobjet maintenant.", player);
                }
            }
        }

        if (packet.getType() == PacketType.Play.Client.CLOSE_WINDOW) {

            final Player player = packet.getPlayer();

            if (player.getOpenInventory().getTopInventory().getTitle().contains("Porte feuille") && compareMaterial(player.getInventory().getItemInMainHand().getType(), getMaterial(wallet)) && this.walletManager.getDecryptedID(player.getInventory().getItemInMainHand().getItemMeta().getLore()).equalsIgnoreCase(this.walletManager.getCachedDecryptedID())) {

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

    private boolean isCard(final ItemStack itemStack) {
        return compareMaterial(itemStack.getType(), getMaterial(IDCard));
    }
}
