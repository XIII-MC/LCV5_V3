package com.xiii.libertycity.roleplay.items.handcuffs.utils;

import com.xiii.libertycity.core.utils.ChatUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class Targeter {

    public static Player getTargetPlayer(final Player player) {
        return getTarget(player, player.getWorld().getPlayers());
    }

    public static Entity getTargetEntity(final Entity entity) {
        return getTarget(entity, entity.getWorld().getEntities());
    }

    public static boolean getTargetHitBox(final Entity entity) {
        return getHitBox(entity, entity.getWorld().getEntities()) != null;
    }

    public static <T extends Entity> T getTarget(final Entity entity,
                                                 final Iterable<T> entities) {
        if (entity == null)
            return null;
        T target = null;
        final double threshold = 1;
        for (final T other : entities) {
            final Vector n = other.getLocation().toVector()
                    .subtract(entity.getLocation().toVector());
            ChatUtils.broadcast("" + entity.getLocation().getDirection().normalize().crossProduct(n)
                    .lengthSquared());
            if (entity.getLocation().getDirection().normalize().crossProduct(n)
                    .lengthSquared() < threshold
                    && n.normalize().dot(
                    entity.getLocation().getDirection().normalize()) >= 0) {
                if (target == null
                        || target.getLocation().distanceSquared(
                        entity.getLocation()) > other.getLocation()
                        .distanceSquared(entity.getLocation()))
                    target = other;
            }
        }
        return target;
    }

    public static <T extends Entity> T getHitBox(final Entity entity,
                                                 final Iterable<T> entities) {
        if (entity == null) return null;
        T target = null;
        int count = 0;
        for (final T other : entities) {
            final Vector n = other.getLocation().toVector()
                    .subtract(entity.getLocation().toVector());
            final double result = entity.getLocation().getDirection().normalize().crossProduct(n).lengthSquared();
            if (result < 1000 && result > 1) {
                count++;
                ChatUtils.broadcast("" + result);
                if (count >= 10) return null;
                else target = other;
            } else target = other;
        }

        return target;
    }

}
