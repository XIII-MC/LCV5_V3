package com.xiii.libertycity.roleplay.items.search.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class InvSee {

    public static Inventory invSee(final Player player) {

        if (player == null) return null;

        return player.getInventory();
    }
}
