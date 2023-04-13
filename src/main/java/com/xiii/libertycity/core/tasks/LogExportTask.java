package com.xiii.libertycity.core.tasks;

import com.xiii.libertycity.core.manager.files.FileManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class LogExportTask extends BukkitRunnable {

    public LogExportTask() {
    }

    @Override
    public void run() {
        try {
            FileManager.getCfg().save(FileManager.getLogFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
