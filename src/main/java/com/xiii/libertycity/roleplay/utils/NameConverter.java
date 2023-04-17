package com.xiii.libertycity.roleplay.utils;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import org.bukkit.entity.Player;

public final class NameConverter {

    public static String getFullName(final Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        return "§a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + "§r";
    }

    public static String getFirstName(final Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        return "§a§l" + profile.rpFirstName + "§r";
    }
    public static String getLastName(final Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        return "§2§l" + profile.rpLastName + "§r";
    }
}
