package com.xiii.libertycity.core.manager.files;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;

import java.io.*;
import java.util.UUID;

public class FileManager {

    public static void saveProfile(Profile profile) {
        try {

            if (!profile.isVerified) return;

            if (!LibertyCity.getInstance().getDataFolder().exists())
                LibertyCity.getInstance().getDataFolder().mkdir();

            File profilesFolder = new File(LibertyCity.getInstance().getDataFolder() + "/players/");
            if (!profilesFolder.exists()) profilesFolder.mkdir();

            File profileFile = new File(LibertyCity.getInstance().getDataFolder() + "/players/", profile.getUUID() + ".ASCII");
            profileFile.delete();
            profileFile.createNewFile();

            FileOutputStream fileOut = new FileOutputStream(LibertyCity.getInstance().getDataFolder() + "/players/" + profile.getUUID() + ".ASCII");

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(profile);

            out.close();
            fileOut.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void readProfile(UUID uuid) {
        File profileFile = new File(LibertyCity.getInstance().getDataFolder() + "/players/", uuid + ".ASCII");

        try {

            FileInputStream fileIn = new FileInputStream(profileFile.getPath());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            LibertyCity.getInstance().getProfileManager().getProfileMap().put(uuid, (Profile) in.readObject());

            in.close();
            fileIn.close();

        } catch (IOException | ClassNotFoundException ignored) {

        }
    }

    public static boolean profileExists(UUID uuid) {
        File profileFile = new File(LibertyCity.getInstance().getDataFolder() + "/players/", uuid + ".ASCII");

        return profileFile.exists();
    }
}
