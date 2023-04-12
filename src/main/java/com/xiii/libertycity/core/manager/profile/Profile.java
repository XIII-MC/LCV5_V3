package com.xiii.libertycity.core.manager.profile;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.threads.ProfileThread;
import com.xiii.libertycity.core.processors.ClientPlayPacket;
import com.xiii.libertycity.core.processors.ServerPlayPacket;
import com.xiii.libertycity.roleplay.ChatSystem;
import com.xiii.libertycity.roleplay.events.network.RegisterEvent;
import com.xiii.libertycity.roleplay.guis.atm.GUI;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile implements java.io.Serializable{
    private static final long serialVersionUID = 5306742032478905396L;

    //------------------------------------
    public final UUID playerUUID;
    public transient String playerName;
    public transient Player playerEntity;
    private transient ProfileThread profileThread;
    //------------------------------------
    private transient RegisterEvent registerEvent;
    private transient ChatSystem chatSystem;
    private transient GUI atmGUI;
    //------------------------------------
    public String rpFirstName;
    public String rpLastName;
    public int rpAge;
    public boolean isVerified;
    public String joinDate;
    public int rpChat; // 0=HRP 1=RP 2=POLICE 3=GANG 4=STAFF
    public String policeRank;
    public String gangName;
    public transient boolean spyGlobal = false;
    public transient int spyChat = -1; // 0=HRP 1=RP 2=POLICE 3=GANG

    //Bank
    public long rpBankBalance = 0;
    public int rpBankType = 0;
    public long rpBankMaxDep = 1500;
    public long rpBankMaxWit = 600;
    public long rpBankMaxBalance = 10000;
    public long rpBankMinBalance = -50;
    public long rpBankCurrDep = 0;
    public long rpBankCurrWit = 0;

    public Profile(UUID uuid) {
        this.playerUUID = uuid;
    }

    public Profile(Player player) {
        this.playerUUID = player.getUniqueId();
        initialize(player);
    }

    public void initialize(Player player) {
        this.playerName = player.getName();
        this.playerEntity = player.getPlayer();
        this.profileThread = LibertyCity.getInstance().getThreadManager().getAvailableProfileThread();

        this.registerEvent = new RegisterEvent();
        this.chatSystem = new ChatSystem();
        this.atmGUI = new GUI();
    }

    public void handleClientNetty(ClientPlayPacket packet) {

        this.registerEvent.handle(packet);
        this.chatSystem.handle(packet);
        this.atmGUI.handle(packet);
    }

    public void handleServerNetty(ServerPlayPacket packet) {

        if (this.atmGUI == null) return;

        this.atmGUI.handle(packet);
    }

    public void handleClient(ClientPlayPacket packet) {
    }

    public void handleServer(ServerPlayPacket packet) {
    }

    public UUID getUUID() {
        return playerUUID;
    }

    public String getName() {
        return playerName;
    }

    public Player getPlayer() {
        return playerEntity;
    }

    public ProfileThread getProfileThread() {
        return profileThread;
    }
}
