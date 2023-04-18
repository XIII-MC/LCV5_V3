package com.xiii.libertycity.core.tasks;

import com.xiii.libertycity.core.manager.files.FileManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class LogExportTask extends BukkitRunnable {

    public LogExportTask() {
    }

    @Override
    public void run() {
        //if (FileManager.getCfg() != null) FileManager.getCfg().save(FileManager.getLogFile());
    }
}
