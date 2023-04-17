package com.xiii.libertycity.roleplay.items.handcuffs.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.items.handcuffs.utils.Targeter;
import com.xiii.libertycity.roleplay.utils.NameConverter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HandcuffsHandle implements Data {

    private BukkitTask handcuffDelayTask;

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {

            final Player player = packet.getPlayer();

            //TODO: Change to real handcuffs modded item's id
            if (player.getInventory().getItemInMainHand().getType().equals(Material.TRIPWIRE_HOOK)) {

                if (packet.getInteractEntity().getTarget().isPresent()) {

                    final Entity target = Targeter.getTargetEntity(player);

                    if (target instanceof Player) {

                        final Profile targetProfile = LibertyCity.getInstance().getProfileManager().getProfile(target.getUniqueId());

                        if (targetProfile.isHandcuffed) {

                            ((Player) target).sendTitle("§c§LVOUS ÊTES MENOTTÉ", "§7Veuillez coopérer avec §c" + NameConverter.getFullName(player), 0, 20*86400, 0);
                            targetProfile.isHandcuffed = false;
                            targetProfile.handcuffedBy = null;
                            ChatUtils.multicast("§aVous avez libéré §e" + NameConverter.getFullName((Player) target) + "§a!", player);
                            ChatUtils.multicast("§aVous avez été libéré par §e" + NameConverter.getFullName(player), (Player) target);
                        } else {

                            ((Player) target).sendTitle("§a§lVOUS AVEZ ÉTÉ LIBÉRÉ", "§a" + NameConverter.getFullName(player) + "§7 vous a libéré.", 0, 20*3, 20);
                            targetProfile.isHandcuffed = true;
                            targetProfile.handcuffedBy = player;
                            handcuffDelayTask.cancel();
                            handcuffDelayTask = null;
                            ChatUtils.multicast("§cVous avez menotté §e" + NameConverter.getFullName((Player) target) + "§c!", player);
                            ChatUtils.multicast("§cVous avez été menotté par §e" + NameConverter.getFullName(player), (Player) target);
                        }
                    }
                }
            }
        }

        if (packet.getType() == PacketType.Play.Client.PLAYER_POSITION || packet.getType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || packet.getType() == PacketType.Play.Client.PLAYER_FLYING) {

            final Player player = packet.getPlayer();
            final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

            if (profile.isHandcuffed) {

                packet.getEvent().setCancelled(true);
                player.teleport(player);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§lVous ne pouvez pas bouger, vous êtes menotté!"));
                if (handcuffDelayTask == null) {
                    handcuffDelayTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (Math.abs(profile.handcuffedBy.getLocation().distance(player.getLocation())) >= 8) {
                                if (profile.handcuffedDelay >= 30) {
                                    profile.isHandcuffed = false;
                                    profile.handcuffedDelay = 0;
                                    profile.handcuffedBy.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§4§lLe suspect menotté c'est échappé!"));
                                    profile.handcuffedBy = null;
                                    player.sendTitle("§a§lVOUS AVEZ ÉTÉ LIBÉRÉ", "§7Le policier est resté trop loin! Échappez-vous!", 0, 20 * 3, 20);
                                    handcuffDelayTask.cancel();
                                    handcuffDelayTask = null;
                                } else {
                                    profile.handcuffedDelay++;
                                    player.sendTitle("§c§lVOUS ÊTES MENOTTÉ", "§7Vous pourrez vous échappez dans §c" + (30 - profile.handcuffedDelay) + " secondes§7...", 0, 20 * 3, 20);
                                    profile.handcuffedBy.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§4§lVous vous éloignez du suspect menotté!"));
                                }
                            } else {
                                if (profile.handcuffedDelay > 0) {
                                    profile.handcuffedDelay--;
                                    player.sendTitle("§c§LVOUS ÊTES MENOTTÉ", "§7Veuillez coopérer avec §c" + NameConverter.getFullName(player), 0, 20*86400, 0);
                                }
                            }
                        }
                    }.runTaskTimerAsynchronously(LibertyCity.getInstance(), 20L, 20L);
                }
            }
        }
    }

    public void handle(final ServerPlayPacket packet) {
    }
}
