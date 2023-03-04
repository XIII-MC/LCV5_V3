package com.xiii.libertycity;

import com.keenant.tabbed.Tabbed;
import com.xiii.libertycity.listeners.ProfileListener;
import com.xiii.libertycity.manager.files.FileManager;
import com.xiii.libertycity.manager.profile.ProfileManager;
import com.xiii.libertycity.manager.threads.ThreadManager;
import com.xiii.libertycity.tasks.TickTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LibertyCity extends JavaPlugin {

    //Internals
    public static LibertyCity instance;
    private ProfileManager profileManager;
    private ThreadManager threadManager;
    private static Logger logger;
    private PluginDescriptionFile pdf;

    //Non Internals
    private BossBar bossBar;
    private String botToken = "";
    private JDA jda;
    private Tabbed tab;

    @Override
    public void onEnable() {

        //Init Internals
        instance = this;
        (this.profileManager = new ProfileManager()).initialize();
        (this.threadManager = new ThreadManager(this)).initialize();
        logger = Bukkit.getLogger();
        pdf = this.getDescription();
        // .addEventListeners(new SlashCommand())
        jda = JDABuilder.createDefault(botToken).setActivity(Activity.watching("??? joueurs")).build();
        tab = new Tabbed(this);

        //Init Non Internals
        log(Level.INFO, "Initialization...");
        bossBar = Bukkit.createBossBar("§k", BarColor.WHITE, BarStyle.SEGMENTED_6);

        //Startup
        log(Level.INFO, "Startup...");

        //Tasks
        log(Level.INFO, "Starting tasks...");
        new TickTask(this).runTaskTimerAsynchronously(this, 50L, 0L);

        //Bukkit Listeners
        log(Level.INFO, "Starting listeners...");
        Arrays.asList(
                new ProfileListener(this),
                //new ViolationListener(this),
                new ThreadManager(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        //Done
        log(Level.INFO, "Plugin loaded and ready.");
    }

    @Override
    public void onDisable() {

        //Save all data to prevent data loss
        for(Player p : Bukkit.getOnlinePlayers()) FileManager.saveProfile(this.getProfileManager().getProfile(p));

        //Shutdown all managers
        this.profileManager.shutdown();
        this.threadManager.shutdown();

        //Cancel all tasks
        Bukkit.getScheduler().cancelTasks(this);

        instance = null;

    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static LibertyCity getInstance() {
        return instance;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public ThreadManager getThreadManager() {
        return threadManager;
    }

    public PluginDescriptionFile getPluginDescription() {
        return pdf;
    }

    public JDA getDiscordBot() {
        return jda;
    }

    public Tabbed getTabInstance() {
        return tab;
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
