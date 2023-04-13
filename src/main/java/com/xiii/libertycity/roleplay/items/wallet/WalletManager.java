package com.xiii.libertycity.roleplay.items.wallet;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.core.utils.InventoryUtils;
import com.xiii.libertycity.roleplay.items.wallet.data.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WalletManager {

    private final DataManager dataManager = new DataManager();
    private String cachedDecryptedID;

    public void openWallet(final ItemStack itemStackHand, final Player player) {

        try {

            //Get item's lore
            final ItemMeta itemMeta = itemStackHand.getItemMeta();
            final List<String> lore = itemMeta.getLore();

            //If the wallet is new
            if (lore == null && player != null) {

                final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

                final String encryptedID = "" + (Math.random() * Math.random() * 487832156 + Math.random() / Math.random() % Math.random() * 12365754 / (Math.random() * Math.random() / Math.random() - Math.random()));

                List<String> newLore = new ArrayList<>();
                newLore.add("");
                newLore.add("§7Propriétaire » §a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName);
                newLore.add("§0§k" + encryptedID);
                newLore.add("");

                itemMeta.setLore(newLore);
                itemStackHand.setItemMeta(itemMeta);

                InventoryUtils.removeOne(player.getInventory(), itemStackHand, false);

                final String base64Inventory = this.getDataManager().encodeBase64(Bukkit.createInventory(null, 9, "§8Porte feuille"));
                final FileConfiguration wallets = this.getWalletConfig();
                wallets.set(encryptedID, base64Inventory);
                wallets.save(this.getWalletFile());

                player.getInventory().addItem(itemStackHand);
                return;
            }

            if (lore != null && lore.get(2) != null) {

                final String decryptedID = this.getDecryptedID(lore);

                //Load wallet
                if (this.getWalletConfig().contains(decryptedID)) {

                    this.cachedDecryptedID = decryptedID;
                    player.openInventory(this.dataManager.decodeBase64(this.getWalletConfig().getString(decryptedID)));
                } else {

                    ChatUtils.multicast("§cUne erreur est survenue. Porte feuille non-existant.");
                }
            }
        } catch (final IOException e) {

            ChatUtils.multicast("§cUne erreur est survenue pendant l'ouverture du porte feuille.", player);
        }
    }

    public void closeWallet(final Inventory inventory, final Player player) {
        this.set(this.getDecryptedID(player.getInventory().getItemInMainHand().getItemMeta().getLore()), this.getDataManager().encodeBase64(inventory));
    }

    public String getDecryptedID(final List<String> lore) {
        return lore.get(2).replace("§0§k", "");
    }

    private void set(final String decryptedID, final String encodedInventory) {
        try {
            final FileConfiguration wallets = getWalletConfig();
            wallets.set(decryptedID, encodedInventory);
            wallets.save(this.getWalletFile());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getWalletConfig() {
        return YamlConfiguration.loadConfiguration(getWalletFile());
    }

    public File getWalletFile() {
        return new File(LibertyCity.getInstance().getDataFolder() + "/data/00000000-0000-0000-0000-000000000000.yml");
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public String getCachedDecryptedID() {
        return cachedDecryptedID;
    }
}
