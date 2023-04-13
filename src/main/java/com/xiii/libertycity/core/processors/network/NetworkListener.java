package com.xiii.libertycity.core.processors.network;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.TaskUtils;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class NetworkListener extends SimplePacketListenerAbstract {

    private final LibertyCity plugin;

    public NetworkListener(LibertyCity plugin) {
        super(PacketListenerPriority.LOWEST);

        this.plugin = plugin;

        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        if (e.isCancelled() || e.getPlayer() == null) return;

        final Player player = (Player) e.getPlayer();

        final ClientPlayPacket packet = new ClientPlayPacket(e.getPacketType(), e, System.currentTimeMillis());

        if (checkCrasher(packet)) {

            e.setCancelled(true);

            LibertyCity.log(Level.SEVERE, player.getName() + " tried to crash the server!");

            TaskUtils.task(() -> player.kickPlayer("Invalid Packet"));

            return;
        }

        final Profile profile = this.plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile == null || profile.playerEntity == null) return;

        profile.handleClientNetty(packet);
        profile.getProfileThread().execute(() -> profile.handleClient(packet));
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent e) {
        if (e.isCancelled() || e.getPlayer() == null) return;

        final ServerPlayPacket packet = new ServerPlayPacket(e.getPacketType(), e, System.currentTimeMillis());

        final Player player = (Player) e.getPlayer();

        final Profile profile = this.plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile == null || profile.playerEntity == null || profile.getProfileThread() == null) return;

        profile.handleServerNetty(packet);
        profile.getProfileThread().execute(() -> profile.handleServer(packet));
    }

    private boolean checkCrasher(ClientPlayPacket packet) {

        double x = 0D, y = 0D, z = 0D;
        float yaw = 0F, pitch = 0F;

        switch (packet.getType()) {

            case PLAYER_POSITION:

                WrapperPlayClientPlayerPosition pos = packet.getPositionWrapper();

                x = Math.abs(pos.getPosition().getX());
                y = Math.abs(pos.getPosition().getY());
                z = Math.abs(pos.getPosition().getZ());

                break;

            case PLAYER_POSITION_AND_ROTATION:

                WrapperPlayClientPlayerPositionAndRotation posLook = packet.getPositionLookWrapper();

                x = Math.abs(posLook.getPosition().getX());
                y = Math.abs(posLook.getPosition().getY());
                z = Math.abs(posLook.getPosition().getZ());
                yaw = Math.abs(posLook.getYaw());
                pitch = Math.abs(posLook.getPitch());

                break;

            case PLAYER_ROTATION:

                WrapperPlayClientPlayerRotation look = packet.getLookWrapper();

                yaw = Math.abs(look.getYaw());
                pitch = Math.abs(look.getPitch());

                break;
        }

        final double invalidValue = 3.0E7D;

        final boolean invalid = x > invalidValue || y > invalidValue || z > invalidValue
                || yaw > 3.4028235e+35F
                || pitch > 90F;

        final boolean impossible = !Double.isFinite(x)
                || !Double.isFinite(y)
                || !Double.isFinite(z)
                || !Float.isFinite(yaw)
                || !Float.isFinite(pitch);

        return invalid || impossible;
    }
}
