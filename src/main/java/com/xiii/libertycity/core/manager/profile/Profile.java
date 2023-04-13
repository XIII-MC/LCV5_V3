package com.xiii.libertycity.core.manager.profile;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.threads.ProfileThread;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.roleplay.ChatSystem;
import com.xiii.libertycity.roleplay.events.network.RegisterEvent;
import com.xiii.libertycity.roleplay.guis.atm.events.network.ATMHandle;
import com.xiii.libertycity.roleplay.guis.trashcan.events.network.TrashcanHandle;
import com.xiii.libertycity.roleplay.items.wallet.events.network.WalletHandle;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Profile implements java.io.Serializable {
    private static final long serialVersionUID = 5306742032478905396L;

    //----------------SERVER--------------
    public Map<String, UUID> db_rpFirstName;
    public Map<String, UUID> db_rpLastName;
    //------------------------------------
    public final UUID playerUUID;
    public transient String playerName;
    public transient Player playerEntity;
    private transient ProfileThread profileThread;
    //------------------------------------
    private transient RegisterEvent registerEvent;
    private transient ChatSystem chatSystem;
    private transient ATMHandle atmHandle;
    private transient TrashcanHandle trashcanGUI;
    private transient WalletHandle walletHandle;
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

    public Profile(final UUID uuid) {
        this.playerUUID = uuid;
    }

    public Profile(final Player player) {
        this.playerUUID = player.getUniqueId();
        initialize(player);
    }

    public void initialize(final Player player) {
        this.playerName = player.getName();
        this.playerEntity = player.getPlayer();
        this.profileThread = LibertyCity.getInstance().getThreadManager().getAvailableProfileThread();

        this.registerEvent = new RegisterEvent();
        this.chatSystem = new ChatSystem();
        this.atmHandle = new ATMHandle();
        this.trashcanGUI = new TrashcanHandle();
        this.walletHandle = new WalletHandle();
    }

    public void serverInitialize() {
        if (this.db_rpFirstName == null) this.db_rpFirstName = new ConcurrentHashMap<>();
        if (this.db_rpLastName == null) this.db_rpLastName = new ConcurrentHashMap<>();
        this.isVerified = true;
    }

    public void handleClientNetty(final ClientPlayPacket packet) {

        this.registerEvent.handle(packet);
        this.chatSystem.handle(packet);
        this.atmHandle.handle(packet);
        this.trashcanGUI.handle(packet);
        this.walletHandle.handle(packet);
    }

    public void handleServerNetty(final ServerPlayPacket packet) {

        if (this.walletHandle == null) return;

        this.walletHandle.handle(packet);
    }

    public void handleClient(final ClientPlayPacket packet) {
    }

    public void handleServer(final ServerPlayPacket packet) {
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
