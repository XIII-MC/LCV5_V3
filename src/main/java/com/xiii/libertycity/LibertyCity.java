package com.xiii.libertycity;

import com.github.retrooper.packetevents.PacketEvents;
import com.keenant.tabbed.Tabbed;
import com.xiii.libertycity.core.listeners.ProfileListener;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.ProfileManager;
import com.xiii.libertycity.core.manager.threads.ThreadManager;
import com.xiii.libertycity.core.processors.bukkit.BukkitListener;
import com.xiii.libertycity.core.processors.network.NetworkListener;
import com.xiii.libertycity.core.tasks.TickTask;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
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
import java.util.Collections;
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

    //Non Internals
    private BossBar bossBar;
    private JDA jda;
    private Tabbed tab;

    //PacketEvents
    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //Are all listeners read only?
        PacketEvents.getAPI().getSettings().readOnlyListeners(true)
                .checkForUpdates(false)
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

        //Init Non Internals
        log(Level.INFO, "Initialization...");
        bossBar = Bukkit.createBossBar("§k", BarColor.WHITE, BarStyle.SEGMENTED_6);
        // .addEventListeners(new SlashCommand())
        jda = JDABuilder.createDefault("MTA2NTMxNDQ1NDk4NTc4NTQxNg.GJBEwB.YfdVOSjaLbwFQ3FN2Q3_B07xKaR52K_98JRftM").setActivity(Activity.playing("RUNNING DEVMODE")).build();
        tab = new Tabbed(this);

        //Startup
        log(Level.INFO, "Startup...");

        //Tasks
        log(Level.INFO, "Starting tasks...");
        new TickTask(this).runTaskTimerAsynchronously(this, 50L, 0L);

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
