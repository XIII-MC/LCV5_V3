package com.xiii.libertycity.roleplay.utils;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class PhoneNumber {

    private static final Profile serverProfile = LibertyCity.getInstance().getServerProfile();

    public static Player getPlayer(final String phoneNumber) {
        return Bukkit.getPlayer(serverProfile.db_phoneNumbers.get(phoneNumber));
    }

    public static String getNumber(final UUID uuid) {
        return serverProfile.db_phoneNumbers.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), uuid))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet())
                .toString();
    }

    public static void newNumber(final Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        final Random rnd = new Random();
        String newPhoneNumber = "646-" + (1000000 + rnd.nextInt(9000000));
        while (numberExists(newPhoneNumber)) {
            newPhoneNumber = "646-" + (1000000 + rnd.nextInt(9000000));
        }

        serverProfile.db_phoneNumbers.put(newPhoneNumber, player.getUniqueId());
        profile.phoneNumber = newPhoneNumber;
    }

    public static boolean numberExists(final String phoneNumber) {
        return serverProfile.db_phoneNumbers.containsKey(phoneNumber);
    }
}
