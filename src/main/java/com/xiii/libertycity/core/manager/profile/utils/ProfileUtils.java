package com.xiii.libertycity.core.manager.profile.utils;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.Profile;

import java.util.Map;

public final class ProfileUtils {

    //Key: String, Value: UUID

    private static final Profile serverProfile = LibertyCity.getInstance().getProfileManager().getProfile(LibertyCity.getInstance().getServerUUID());

    public static boolean isNameAvailable(final String firstName, final String lastName) {
        if (!getDataBaseRPFirstNames().containsKey(firstName)) {
            if (!getDataBaseRPLastNames().containsKey(lastName)) {
                return true;
            } else return false;
        } else if (!getDataBaseRPLastNames().containsKey(lastName)) return true;
        else return false;
    }

    //Run this AFTER setting the variable
    public static void updateServerProfile() {
        FileManager.saveProfile(getServerProfile());
    }

    public static Profile getServerProfile() {
        return serverProfile;
    }

    public static Map getDataBaseRPFirstNames() {
        return serverProfile.db_rpFirstName;
    }

    public static Map getDataBaseRPLastNames() {
        return serverProfile.db_rpLastName;
    }
}
