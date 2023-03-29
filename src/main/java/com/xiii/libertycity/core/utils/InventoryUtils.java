package com.xiii.libertycity.core.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class InventoryUtils {

    public static ItemStack createItem(final Material material, final int amount, final String name, final String... lore) {

        final ItemStack item = new ItemStack(material, amount);

        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public static void removeOne(final Inventory inventory, final ItemStack itemStack) {
        final ItemStack one = itemStack.clone();
        one.setAmount(1);
        inventory.removeItem(one);
    }

    public static int getAmount(final Player player, final ItemStack itemStack) {

        if (itemStack == null) return 0;

        int amount = 0;
        for (int i = 0; i < 36; i++) {

            final ItemStack slot = player.getInventory().getItem(i);

            if (slot == null || !slot.isSimilar(itemStack)) continue;

            amount += slot.getAmount();
        }

        return amount;
    }

    public static boolean hasItem(final Player player, final ItemStack itemStack) {

        for (final ItemStack item : player.getInventory().getContents()) {

            if (item == null) continue;

            if (item.isSimilar(itemStack)) return true;
        }

        return false;
    }

    public static boolean canStore(final Player player, final ItemStack itemStack) {

        final ItemStack[] invRef = player.getInventory().getStorageContents();

        int count = 0;
        for(final ItemStack j : invRef) {

            if(j == null || j.getType() == Material.AIR) return true;

            if(j.isSimilar(itemStack)) count += j.getMaxStackSize() - j.getAmount();

            if(count >= itemStack.getAmount()) return true;
        }

        return false;
    }
}
