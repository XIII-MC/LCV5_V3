package com.xiii.libertycity.roleplay.items.idcard;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class IDCardManager {

    public static void createIDCard(final Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final String encryptedID = "" + (Math.random() * Math.random() * 698745215 + Math.random() / Math.random() % Math.random() * 325874169 / (Math.random() * Math.random() / Math.random() - Math.random()) + Math.random() * 982735110);
        //TODO: Change to real id card modded item's id
        final ItemStack idCard = new ItemStack(Material.PAPER);
        final ItemMeta idCard_meta = idCard.getItemMeta();

        idCard_meta.setDisplayName("§rCarte d'identitée");
        final List<String> newLore = new ArrayList<>();
        newLore.add("");
        newLore.add("§7Prénom » §a§l" + profile.rpFirstName);
        newLore.add("§7Nom » §2§l" + profile.rpLastName);
        newLore.add("§7Âge » §6§l" + profile.rpAge);
        newLore.add("§7Pseudo » §c§l" + player.getName());
        newLore.add("");
        newLore.add("§0§k" + encryptedID);
        newLore.add("§0§k" + profile.getUUID());
        newLore.add("");
        idCard_meta.setLore(newLore);

        idCard.setItemMeta(idCard_meta);

        profile.encryptedIDCardID = encryptedID;
        player.getInventory().addItem(idCard);
    }

    public boolean verify(final ItemStack itemStack) {

        if (itemStack == null || itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null) return false;

        final String decryptedID = getDecryptedID(itemStack.getItemMeta().getLore());
        final UUID decryptedUUID = getDecryptedUUID(itemStack.getItemMeta().getLore());
        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(decryptedUUID);

        return Objects.equals(profile.encryptedIDCardID, decryptedID);
    }

    private String getDecryptedID(final List<String> lore) {
        return lore.get(6).replace("§0§k", "");
    }

    private UUID getDecryptedUUID(final List<String> lore) {
        return UUID.fromString(lore.get(7).replace("§0§k", ""));
    }
}
