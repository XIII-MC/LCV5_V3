package com.xiii.libertycity.roleplay.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    //TODO: Change items to real modded items
    public static final ItemStack

            /*
            Item - Bills/Cash
             */
            b1 = new ItemStack(Material.IRON_NUGGET),
            b2 = new ItemStack(Material.GOLD_NUGGET),
            b5 = new ItemStack(Material.BRICK),
            b10 = new ItemStack(Material.COAL),
            b20 = new ItemStack(Material.IRON_INGOT),
            b50 = new ItemStack(Material.GOLD_INGOT),
            b100 = new ItemStack(Material.REDSTONE),
            b200 = new ItemStack(Material.LAPIS_BLOCK),
            b500 = new ItemStack(Material.DIAMOND),
            b1000 = new ItemStack(Material.EMERALD),
            b2000 = new ItemStack(Material.NETHER_STAR),
            b5000 = new ItemStack(Material.EMERALD_BLOCK),

            /*
            Block - ATM
             */
            ATM = new ItemStack(Material.EMERALD_BLOCK),

            /*
            Block - Trashcan
             */
            trashcan = new ItemStack(Material.DIAMOND_BLOCK),

            /*
            Item - Handcuffs
             */
            handcuffs = new ItemStack(Material.TRIPWIRE_HOOK),

            /*
            Item - IDCard
             */
            IDCard = new ItemStack(Material.PAPER),

            /*
            Item - Search item
             */
            searchItem = new ItemStack(Material.STICK),

            /*
            Item - Wallet
             */
            wallet = new ItemStack(Material.DIAMOND);

    public static ItemStack getItemStack(final Inventory inventory, final int inventorySlot) {
        return inventory.getItem(inventorySlot);
    }

    public static Material getItemMaterial(final Inventory inventory, final int inventorySlot) {
        return inventory.getItem(inventorySlot).getType();
    }

    public static ItemStack getItemStack(final InventoryView inventory, final int inventorySlot) {
        return inventory.getItem(inventorySlot);
    }

    public static Material getItemMaterial(final InventoryView inventory, final int inventorySlot) {
        return inventory.getItem(inventorySlot).getType();
    }

    public static boolean compareItemStack(final ItemStack originalItem, final ItemStack compareItem) {
        return originalItem.equals(compareItem);
    }

    public static boolean compareMaterial(final ItemStack originalItem, final ItemStack compareItem) {
        return originalItem.getType().equals(compareItem.getType());
    }

    public static boolean compareMaterial(final Material originalItem, final Material compareItem) {
        return originalItem.equals(compareItem);
    }

    public static Material getMaterial(final ItemStack itemStack) {
        return itemStack.getType();
    }
}
