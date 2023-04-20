package com.xiii.libertycity;

import com.github.retrooper.packetevents.PacketEvents;
import com.keenant.tabbed.Tabbed;
import com.xiii.libertycity.core.listeners.ProfileListener;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.manager.profile.ProfileManager;
import com.xiii.libertycity.core.manager.threads.ProfileThread;
import com.xiii.libertycity.core.manager.threads.ThreadManager;
import com.xiii.libertycity.core.manager.threads.ThreadMonitor;
import com.xiii.libertycity.core.processors.bukkit.BukkitListener;
import com.xiii.libertycity.core.processors.network.NetworkListener;
import com.xiii.libertycity.core.tasks.LogExportTask;
import com.xiii.libertycity.core.tasks.clearlag.ClearLagTask;
import com.xiii.libertycity.core.utils.TPS;
import com.xiii.libertycity.core.utils.time.TimeFormat;
import com.xiii.libertycity.core.utils.time.TimeUtils;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LibertyCity extends JavaPlugin {

    // DEV: MTA2NTMxNDQ1NDk4NTc4NTQxNg.GJBEwB.YfdVOSjaLbwFQ3FN2Q3_B07xKaR52K_98JRftM
    // OFI: MTA2Mjc5Mjg2ODk2NTY2MjcyMA.G8-6hq.7UHoeLfkMn6K04KuqDX7IXFKsUe28gPCGOeSBI

    //Internals
    public static LibertyCity instance;
    private ProfileManager profileManager;
    private ThreadManager threadManager;
    private static Logger logger;
    private PluginDescriptionFile pdf;
    private final UUID serverUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    //Non Internals
    private BossBar bossBar;
    //private JDA jda;
    private Tabbed tab;

    //PacketEvents
    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().checkForUpdates(false)
                .bStats(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {

        //Init Internals
        instance = this;
        (this.profileManager = new ProfileManager()).initialize();
        (this.threadManager = new ThreadManager(this)).initialize();
        logger = this.getLogger();
        pdf = this.getDescription();

        FileManager.initialize();

        if (FileManager.profileExists(this.getServerUUID())) FileManager.readProfile(this.getServerUUID());
        this.getProfileManager().createProfile(this.getServerUUID());
        this.getProfileManager().getProfile(this.getServerUUID()).serverInitialize();

        //Init Non Internals
        log(Level.INFO, "Initialization...");
        bossBar = Bukkit.createBossBar("§k", BarColor.WHITE, BarStyle.SEGMENTED_6);
        bossBar.setVisible(false);
        // .addEventListeners(new SlashCommand())
        //jda = JDABuilder.createDefault("MTA2NTMxNDQ1NDk4NTc4NTQxNg.GJBEwB.YfdVOSjaLbwFQ3FN2Q3_B07xKaR52K_98JRftM").setActivity(Activity.playing("RUNNING DEVMODE")).build();
        tab = new Tabbed(this);

        //Startup
        log(Level.INFO, "Startup...");

        //Tasks
        log(Level.INFO, "Starting tasks...");
        new ClearLagTask(this).runTaskTimerAsynchronously(this, 20*1800, 20*1800);
        new ThreadMonitor().runTaskTimerAsynchronously(this, 0L, 20*10);
        new LogExportTask().runTaskTimerAsynchronously(this, 20*5, 20*5);

        //Bukkit Listeners
        log(Level.INFO, "Starting listeners...");

        //Packet Listeners
        Collections.singletonList(
                new NetworkListener(this)
        ).forEach(packetListener -> PacketEvents.getAPI().getEventManager().registerListener(packetListener));

        PacketEvents.getAPI().init();

        //Bukkit Listeners
        Arrays.asList(
                new ProfileListener(this),
                new BukkitListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        //ZIP log files
        log(Level.INFO, "Zipping log files...");

        final File fileFolder = new File(LibertyCity.getInstance().getDataFolder() + "/logs/");

        if (!fileFolder.exists()) fileFolder.mkdir();

        final File compare = new File(LibertyCity.getInstance().getDataFolder() + "/logs/" + TimeUtils.convertMillis(System.currentTimeMillis(), TimeFormat.LOG_DATE) + ".log");

        for (final File file : Objects.requireNonNull(fileFolder.listFiles())) {

            if (!file.getName().equals(compare.getName()) && !file.getName().contains(".zip")) FileManager.zipFile(file, new File(LibertyCity.getInstance().getDataFolder() + "/logs/" + file.getName().replace(".log", "") + ".zip"));
        }

        //Done
        log(Level.INFO, "Plugin loaded and ready.");
    }

    @Override
    public void onDisable() {

        //Save all data to prevent data loss
        Bukkit.getOnlinePlayers().forEach(player -> FileManager.saveProfile(this.getProfileManager().getProfile(player.getUniqueId())));
        FileManager.saveProfile(this.getServerProfile());

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

    //public JDA getDiscordBot() {
    //    return jda;
    //}

    public Tabbed getTabInstance() {
        return tab;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public ProfileThread getThread() {
        return getServerProfile().getProfileThread();
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public Profile getServerProfile() {
        return this.getProfileManager().getProfile(this.getServerUUID());
    }

    public double getTPS() {
        return TPS.getTPS();
    }

    public double getAverageTPS(final int time) {
        return TPS.getAverageTPS(time);
    }
}
