package com.xiii.libertycity.core.tasks;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.files.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogExportTask extends BukkitRunnable {

    @Override
    public void run() {
        try {

            final List<String> cachedMessages = FileManager.getCachedMessages();

            if (!cachedMessages.isEmpty()) {

                final FileWriter writer = new FileWriter(FileManager.getLogFile(), true);

                for (String msg : cachedMessages) {

                    writer.append(msg).append(System.lineSeparator());
                }

                FileManager.getCachedMessages().clear();
                writer.close();
            }
        } catch (final IOException e) {

            e.printStackTrace();
        }
    }
}
