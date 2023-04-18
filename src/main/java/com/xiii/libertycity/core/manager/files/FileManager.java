package com.xiii.libertycity.core.manager.files;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.utils.time.TimeFormat;
import com.xiii.libertycity.core.utils.time.TimeUtils;

import java.io.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager {

    public static void saveProfile(final Profile profile) {

        try {

            if (!profile.isVerified) return;

            if (!LibertyCity.getInstance().getDataFolder().exists()) LibertyCity.getInstance().getDataFolder().mkdir();

            final File profilesFolder = new File(LibertyCity.getInstance().getDataFolder() + "/data/");

            if (!profilesFolder.exists()) profilesFolder.mkdir();

            final File profileFile = new File(LibertyCity.getInstance().getDataFolder() + "/data/", profile.getUUID() + ".ASCII");

            profileFile.delete();
            profileFile.createNewFile();

            final FileOutputStream fileOut = new FileOutputStream(LibertyCity.getInstance().getDataFolder() + "/data/" + profile.getUUID() + ".ASCII");
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(profile);

            fileOut.flush();
            fileOut.close();
            out.flush();
            out.close();
        } catch (final IOException e) {

            e.printStackTrace();
        }
    }

    public static void readProfile(final UUID uuid) {

        try {

            final File profileFile = new File(LibertyCity.getInstance().getDataFolder() + "/data/", uuid + ".ASCII");

            final FileInputStream fileIn = new FileInputStream(profileFile.getPath());
            final ObjectInputStream in = new ObjectInputStream(fileIn);

            LibertyCity.getInstance().getProfileManager().getProfileMap().put(uuid, (Profile) in.readObject());

            fileIn.close();
            in.close();
        } catch (final IOException | ClassNotFoundException ignored) {}
    }

    public static boolean profileExists(final UUID uuid) {
        return new File(LibertyCity.getInstance().getDataFolder() + "/data/", uuid + ".ASCII").exists();
    }

    public static void log(final String message, final String customFolderPath, final String customFilePath) {

        LibertyCity.getInstance().getThread().execute(() -> {

            final Pattern pt = Pattern.compile("\\§+.");
            final Matcher match = pt.matcher(message);
            final String output = match.replaceAll("");

            final String logTime = TimeUtils.convertMillis(System.currentTimeMillis(), TimeFormat.LOG);

            File fileFolder = new File(LibertyCity.getInstance().getDataFolder() + "/logs/");
            File logFile = new File(LibertyCity.getInstance().getDataFolder() + "/logs/" + TimeUtils.convertMillis(System.currentTimeMillis(), TimeFormat.LOG_DATE) + ".log");

            if (customFolderPath != null) fileFolder = new File(LibertyCity.getInstance().getDataFolder() + customFolderPath);
            if (customFilePath != null) logFile = new File(LibertyCity.getInstance().getDataFolder() + customFilePath);

            if (!fileFolder.exists()) fileFolder.mkdir();

            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {

                final FileWriter writer = new FileWriter(logFile, true);

                writer.append(logTime).append(": ").append(output).append(System.lineSeparator());

                writer.flush();
                writer.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }

        });
    }

    public static void zipFile(final File pathIn, final File pathOut) {

        try {

            final FileOutputStream fos = new FileOutputStream(pathOut.toString());
            final ZipOutputStream zipOut = new ZipOutputStream(fos);

            final FileInputStream fis = new FileInputStream(pathIn);
            final ZipEntry zipEntry = new ZipEntry(pathIn.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }

            LibertyCity.log(Level.WARNING, "Zipped file " + pathIn);

            zipOut.flush();
            zipOut.close();
            fis.close();
            fos.flush();
            fos.close();
            pathIn.delete();
        } catch (final IOException e) {

            e.printStackTrace();
        }
    }
}
