package com.xiii.libertycity.core.tasks;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.files.FileManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class LogExportTask extends BukkitRunnable {

    private LibertyCity plugin;

    public LogExportTask(LibertyCity plugin) {
        this.plugin = plugin;
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
