package com.xiii.libertycity.roleplay;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.enums.MsgType;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.manager.threads.ThreadMonitor;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatSystem implements Data {

    private String chatFormat;
    private int chatType;
    private float cpuUsage = 0f;

    public void handle(ClientPlayPacket packet) {

        if (!packet.getEvent().isCancelled() && packet.getType() == PacketType.Play.Client.CHAT_MESSAGE && !packet.getChatWrapper().getMessage().startsWith("/")) {

            if (packet.getChatWrapper().getMessage().equalsIgnoreCase("threads")) {

                ChatUtils.multicast("Performance recap across " + ((LibertyCity.getInstance().getThreadManager().getProfileThreads().size()) + 2) + " threads:", packet.getPlayer());
                final long now = System.currentTimeMillis();
                Bukkit.getScheduler().runTask(LibertyCity.getInstance(), () -> cpuUsage = ThreadMonitor.getThreadUsage(Thread.currentThread().getId()));
                //main thread
                ChatUtils.multicast("- main thread: §6TPS: " + LibertyCity.getInstance().getTPS() + "§8 | §3CPU: " + cpuUsage + "%" + " §7(" + (System.currentTimeMillis() - now) + "ms)", packet.getPlayer());
                ChatUtils.multicast("- netty io thread: §6TPS: " + LibertyCity.getInstance().getTPS() + "§8 | §3CPU: " + ThreadMonitor.getThreadUsage(Thread.currentThread().getId()) + "%" + " §7(" + (System.currentTimeMillis() - now) + "ms)", packet.getPlayer());
                //players thread
                for (Player player : Bukkit.getOnlinePlayers()) {
                    LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId()).getProfileThread().execute(() -> ChatUtils.multicast("- " + player.getName() + "'s thread: " + "§6TPS: " + LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId()).tps + "§8 | §3CPU: " + ThreadMonitor.getThreadUsage(Thread.currentThread().getId()) + "%" + " §7(" + (System.currentTimeMillis() - now) + "ms)", packet.getPlayer()));
                }
                //server thread
                LibertyCity.getInstance().getThread().execute(() -> ChatUtils.multicast("- server thread: §6TPS: " + LibertyCity.getInstance().getServerProfile().tps + "§8 | §3CPU: " + ThreadMonitor.getThreadUsage(Thread.currentThread().getId()) + "%" + " §7(" + (System.currentTimeMillis() - now) + "ms)", packet.getPlayer()));
            }

            packet.getEvent().setCancelled(true);

            final Player player = packet.getPlayer();
            final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
            final String message = packet.getChatWrapper().getMessage();

            if (profile.isVerified) {

                if (profile.rpChat == 0) {

                    setChatFormat(MsgType.HRP.getMessage() + "§a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + " §8| §e" + profile.getPlayer().getName() + " §7» §f" + message);
                    setChatType(0);
                }
                if (profile.rpChat == 1) {

                    setChatFormat(MsgType.RP.getMessage() + "§a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + " §7» §f" + message);
                    setChatType(1);
                }
                if (profile.rpChat == 2) {

                    setChatFormat(MsgType.POLICE.getMessage() + "§a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + " §8| §9" + profile.policeRank + " §7» §f" + message);
                    setChatType(2);
                }
                if (profile.rpChat == 3) {

                    setChatFormat(MsgType.GANG.getMessage() + "§a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + " §8| §4" + profile.gangName + " §7» §f" + message);
                    setChatType(3);
                }
                if (profile.rpChat == 4) {

                    setChatFormat(MsgType.STAFF.getMessage() + "§7" + profile.getPlayer().getName() + " (" + profile.rpFirstName + " " + profile.rpLastName + " » §8" + message);
                    setChatType(4);
                }

            } else return;

            Bukkit.getOnlinePlayers().stream().filter(p -> LibertyCity.getInstance().getProfileManager().getProfile(p.getUniqueId()).spyChat == getChatType() || LibertyCity.getInstance().getProfileManager().getProfile(p.getUniqueId()).spyGlobal).forEach(p -> ChatUtils.multicast("§c§l[CS]§8 | " + getChatFormat(), p));
            Bukkit.getOnlinePlayers().stream().filter(p -> LibertyCity.getInstance().getProfileManager().getProfile(p.getUniqueId()).rpChat == getChatType()).forEach(p -> ChatUtils.multicast(getChatFormat(), p));
            FileManager.log(getChatFormat());
        }
    }

    public void handle(ServerPlayPacket packet) {

    }

    public String getChatFormat() {
        return chatFormat;
    }

    public void setChatFormat(String chatFormat) {
        this.chatFormat = chatFormat;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
}
