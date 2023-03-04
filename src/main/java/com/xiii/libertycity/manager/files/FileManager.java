package com.xiii.libertycity.manager.files;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.manager.profile.Profile;
import org.bukkit.entity.Player;

import java.io.*;

public class FileManager {

    public static void saveProfile(Profile profile) {

        try {

            if (!LibertyCity.getInstance().getDataFolder().exists()) LibertyCity.getInstance().getDataFolder().mkdir();

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

    public static void readProfile(Player player) {

        File profileFile = new File(LibertyCity.getInstance().getDataFolder() + "/players/", player.getUniqueId() + ".ASCII");

        try {

            FileInputStream fileIn = new FileInputStream(profileFile.getPath());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            LibertyCity.getInstance().getProfileManager().getProfileMap().put(player.getUniqueId(), (Profile) in.readObject());

            in.close();
            fileIn.close();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();
        }
    }
}
