package com.xiii.libertycity.core.tasks;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.files.FileManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class YAMLSaveTask extends BukkitRunnable {

    private final LibertyCity plugin;

    public YAMLSaveTask(LibertyCity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        try {
            FileManager.getYamlConfig().save(FileManager.getLogFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
