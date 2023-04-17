package com.xiii.libertycity.core.tasks.clearlag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ClearLag {

    public static int clearLag(final String world, final boolean removeItem) {
        int i = 0;
        for(Entity current : Bukkit.getWorld(world).getEntities()) {
            if(current.getType() == EntityType.DROPPED_ITEM) {
                i++;
                if (removeItem) current.remove();
            }
        }
        return i;
    }
}
