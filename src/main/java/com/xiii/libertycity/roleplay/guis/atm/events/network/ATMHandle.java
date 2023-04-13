package com.xiii.libertycity.roleplay.guis.atm.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.xiii.libertycity.core.enums.MsgType;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.guis.atm.GUI;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ATMHandle implements Data {

    private final GUI atmGUI = new GUI();

    public void handle(ClientPlayPacket packet) {

        final Player player = packet.getPlayer();

        //Open ATM
        if (packet.getType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {

            final WrapperPlayClientPlayerBlockPlacement wrapperPlayClientPlayerBlockPlacement = packet.getBlockPlacementWrapper();

            final Block block = new Location(player.getWorld(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getX(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getY(), wrapperPlayClientPlayerBlockPlacement.getBlockPosition().toVector3d().getZ()).getBlock();
            if (block.getType() == Material.EMERALD_BLOCK) this.atmGUI.openATM(player, false);
            //TODO: Change to real ATM block ID
        }

        if (packet.getType() == PacketType.Play.Client.CLOSE_WINDOW) {
            if (player.getOpenInventory().getTopInventory().getTitle().contains("ATM")) {
                this.atmGUI.processATMClose(player);
            }
        }

        //Click in ATM
        if (packet.getType() == PacketType.Play.Client.CLICK_WINDOW) {

            final WrapperPlayClientClickWindow wrapperPlayClientClickWindow = packet.getClickWindow();
            final String inventoryTitle = player.getOpenInventory().getTopInventory().getTitle();

            if (inventoryTitle.equalsIgnoreCase(MsgType.BANK.getMessage() + "§2§lATM")) {

                //Main ATM menu
                if (wrapperPlayClientClickWindow.getSlot() == 10) this.atmGUI.openDep(player, false);
                if (wrapperPlayClientClickWindow.getSlot() == 16) this.atmGUI.openWit(player, false);
                if (wrapperPlayClientClickWindow.getSlot() == 35) player.closeInventory();
            }

            if (inventoryTitle.equalsIgnoreCase(MsgType.BANK.getMessage() + "§2§lATM §7» §a§LDepôt") && this.atmGUI.isMoneySlot(wrapperPlayClientClickWindow.getSlot())) {
                if (this.atmGUI.processDep(player, SpigotConversionUtil.toBukkitItemStack(wrapperPlayClientClickWindow.getCarriedItemStack()), wrapperPlayClientClickWindow.getButton())) {
                    ChatUtils.multicast(MsgType.BANK.getMessage() + "§fTransaction éffectuée avec succès!", player);
                } else ChatUtils.multicast(MsgType.BANK.getMessage() + "§fTransaction echouée.", player);
                this.atmGUI.openDep(player, true);
            }

            if (inventoryTitle.equalsIgnoreCase(MsgType.BANK.getMessage() + "§2§lATM §7» §c§LRetrait") && this.atmGUI.isMoneySlot(wrapperPlayClientClickWindow.getSlot()) ) {
                if (this.atmGUI.processWit(player, SpigotConversionUtil.toBukkitItemStack(wrapperPlayClientClickWindow.getCarriedItemStack()), wrapperPlayClientClickWindow.getButton())) {
                    ChatUtils.multicast(MsgType.BANK.getMessage() + "§fTransaction éffectuée avec succès!", player);
                } else ChatUtils.multicast(MsgType.BANK.getMessage() + "§fTransaction échouée.", player);
                this.atmGUI.openWit(player, true);
            }

            if (inventoryTitle.equalsIgnoreCase(MsgType.BANK.getMessage() + "§2§lATM §7» §a§LDepôt") || inventoryTitle.equalsIgnoreCase(MsgType.BANK.getMessage() + "§2§lATM §7» §c§LRetrait")) {
                if (wrapperPlayClientClickWindow.getSlot() == 35) this.atmGUI.openATM(player, false);
            }

            if (inventoryTitle.contains("ATM")) {
                packet.getEvent().setCancelled(true);
                player.updateInventory();
            }
        }
    }

    public void handle(ServerPlayPacket packet) {
    }
}
