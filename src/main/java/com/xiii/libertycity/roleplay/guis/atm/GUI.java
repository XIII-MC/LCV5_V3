package com.xiii.libertycity.roleplay.guis.atm;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.enums.MsgType;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.processors.ClientPlayPacket;
import com.xiii.libertycity.core.processors.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.core.utils.InventoryUtils;
import com.xiii.libertycity.roleplay.events.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Data {

    private Inventory gui;
    private final ItemStack glass_empty = InventoryUtils.createItem(Material.STAINED_GLASS_PANE, 1, " ", " ");
    private final ItemStack close_item = InventoryUtils.createItem(Material.BARRIER, 1, "§cRetour");
    private Material b1, b2, b5, b10, b20, b50, b100, b200, b500, b1000, b2000, b5000;

    public void handle(ClientPlayPacket packet) {

        final Player player = packet.getPlayer();

        //Open ATM
        if (packet.getType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT && packet.getBlocKPlacementWrapper().getItemStack().equals(new ItemStack(Material.EMERALD_BLOCK))) {

            openATM(packet.getPlayer());
        }

        if (!packet.getBlocKPlacementWrapper().getItemStack().equals(new ItemStack(Material.EMERALD_BLOCK))) ChatUtils.broadcast("ITEMSTACK: " + packet.getBlocKPlacementWrapper().getItemStack());

        //Click in ATM
        if (packet.getType() == PacketType.Play.Client.CLICK_WINDOW) {

            player.sendMessage("id=" + packet.getClickWindow().getWindowId());

            //Main ATM menu
            com.github.retrooper.packetevents.protocol.item.ItemStack carriedItemStack = packet.getClickWindow().getCarriedItemStack();
            if (carriedItemStack.equals(new ItemStack(b1))) openDep(player);
            if (carriedItemStack.equals(new ItemStack(b5000))) openWit(player);
        }
    }

    public void handle(ServerPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Server.CLOSE_WINDOW) {

            ChatUtils.broadcast("CLOSE_WINDOW: " + packet.getPlayer());
        }

    }

    private void openATM(Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final long bankBalance = profile.rpBankBalance;

        gui.setItem(13, InventoryUtils.createItem(b1, 1, "§aDéposer des espèces", "§7Cet argent sera sera ajouté à votre compte banquaire."));
        gui.setItem(15, InventoryUtils.createItem(Material.PAPER, 1, MsgType.BANK.getMessage(), "§7Informations sur votre compte banquaire", "", "§7Votre compte » §6$" + bankBalance, "§7Type de compte » §8§LBASIC"));
        gui.setItem(17, InventoryUtils.createItem(b5000, 1, "§cRetirer des espèces", "§7Cet argent sera retiré de votre compte banquaire."));

        player.closeInventory();
        player.openInventory(gui);
        reset();
    }

    private void openWit(Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final long bankBalance = profile.rpBankBalance;

        gui.setItem(10, InventoryUtils.createItem(b1, Math.round(bankBalance / 1), "§cRetirer $1", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 1), "§7Transaction » §4-$1"));
        gui.setItem(11, InventoryUtils.createItem(b2, Math.round(bankBalance / 2), "§cRetirer $2", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 2), "§7Transaction » §4-$2"));
        gui.setItem(12, InventoryUtils.createItem(b5, Math.round(bankBalance / 5), "§cRetirer $5", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 5), "§7Transaction » §4-$5"));
        gui.setItem(13, InventoryUtils.createItem(b10, Math.round(bankBalance / 10), "§cRetirer $10", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 10), "§7Transaction » §4-$10"));
        gui.setItem(14, InventoryUtils.createItem(b20, Math.round(bankBalance / 20), "§cRetirer $20", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 20), "§7Transaction » §4-$20"));
        gui.setItem(15, InventoryUtils.createItem(b50, Math.round(bankBalance / 50), "§cRetirer $50", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 50), "§7Transaction » §4-$50"));
        gui.setItem(16, InventoryUtils.createItem(b100, Math.round(bankBalance / 100), "§cRetirer $100", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 100), "§7Transaction » §4-$100"));
        gui.setItem(17, InventoryUtils.createItem(b200, Math.round(bankBalance / 200), "§cRetirer $200", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 200), "§7Transaction » §4-$200"));
        gui.setItem(21, InventoryUtils.createItem(b500, Math.round(bankBalance / 500), "§cRetirer $500", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 500), "§7Transaction » §4-$500"));
        gui.setItem(22, InventoryUtils.createItem(b1000, Math.round(bankBalance / 1000), "§cRetirer $1000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 1000), "§7Transaction » §4-$1000"));
        gui.setItem(23, InventoryUtils.createItem(b2000, Math.round(bankBalance / 2000), "§cRetirer $2000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 2000), "§7Transaction » §4-$2000"));
        gui.setItem(24, InventoryUtils.createItem(b5000, Math.round(bankBalance / 5000), "§cRetirer $5000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 5000), "§7Transaction » §4-$5000"));

        player.closeInventory();
        player.openInventory(gui);
        reset();
    }

    private void openDep(Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final long bankBalance = profile.rpBankBalance;

        gui.setItem(10, InventoryUtils.createItem(b1, InventoryUtils.getAmount(player, new ItemStack(b1)), "§aDéposer $1", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 1), "§7Transaction » §2+$1"));
        gui.setItem(11, InventoryUtils.createItem(b2, InventoryUtils.getAmount(player, new ItemStack(b2)), "§aDéposer $2", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 2), "§7Transaction » §2+$2"));
        gui.setItem(12, InventoryUtils.createItem(b5, InventoryUtils.getAmount(player, new ItemStack(b5)), "§aDéposer $5", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 5), "§7Transaction » §2+$5"));
        gui.setItem(13, InventoryUtils.createItem(b10, InventoryUtils.getAmount(player, new ItemStack(b10)), "§aDéposer $10", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 10), "§7Transaction » §2+$10"));
        gui.setItem(14, InventoryUtils.createItem(b20, InventoryUtils.getAmount(player, new ItemStack(b20)), "§aDéposer $20", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 20), "§7Transaction » §2+$20"));
        gui.setItem(15, InventoryUtils.createItem(b50, InventoryUtils.getAmount(player, new ItemStack(b50)), "§aDéposer $50", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 50), "§7Transaction » §2+$50"));
        gui.setItem(16, InventoryUtils.createItem(b100, InventoryUtils.getAmount(player, new ItemStack(b100)), "§aDéposer $100", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 100), "§7Transaction » §2+$100"));
        gui.setItem(17, InventoryUtils.createItem(b200, InventoryUtils.getAmount(player, new ItemStack(b200)), "§aDéposer $200", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 200), "§7Transaction » §2+$200"));
        gui.setItem(21, InventoryUtils.createItem(b500, InventoryUtils.getAmount(player, new ItemStack(b500)), "§aDéposer $500", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 500), "§7Transaction » §2+$500"));
        gui.setItem(22, InventoryUtils.createItem(b1000, InventoryUtils.getAmount(player, new ItemStack(b1000)), "§aDéposer $1000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 1000), "§7Transaction » §2+$1000"));
        gui.setItem(23, InventoryUtils.createItem(b2000, InventoryUtils.getAmount(player, new ItemStack(b2000)), "§aDéposer $2000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 2000), "§7Transaction » §2+$2000"));
        gui.setItem(24, InventoryUtils.createItem(b5000, InventoryUtils.getAmount(player, new ItemStack(b5000)), "§aDéposer $5000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Votre compte » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 5000), "§7Transaction » §2+$5000"));

        player.closeInventory();
        player.openInventory(gui);
        reset();
    }

    public void initialize() {

        gui = Bukkit.createInventory(null, 36, MsgType.BANK.getMessage() + "§2§lATM");
        reset();

        b1 = Material.IRON_NUGGET;
        b2 = Material.GOLD_NUGGET;
        b5 = Material.BRICK;
        b10 = Material.COAL;
        b20 = Material.IRON_INGOT;
        b50 = Material.GOLD_INGOT;
        b100 = Material.REDSTONE;
        b200 = Material.LAPIS_BLOCK;
        b500 = Material.DIAMOND;
        b1000 = Material.EMERALD;
        b2000 = Material.NETHER_STAR;
        b5000 = Material.EMERALD_BLOCK;
    }

    private void reset() {
        for(int i = 0; i < 35; i++) {
            gui.setItem(i, glass_empty);
        }
        gui.setItem(35, close_item);
    }
}
