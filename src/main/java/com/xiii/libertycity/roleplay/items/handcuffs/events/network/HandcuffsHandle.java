package com.xiii.libertycity.roleplay.items.handcuffs.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.items.handcuffs.utils.Targeter;
import com.xiii.libertycity.roleplay.utils.ItemUtils;
import com.xiii.libertycity.roleplay.utils.NameConverter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HandcuffsHandle extends ItemUtils implements Data {

    private BukkitTask handcuffDelayTask, teleportTask;
    private long lastInteract;

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.INTERACT_ENTITY && (System.currentTimeMillis() - lastInteract) >= 25) {

            lastInteract = System.currentTimeMillis();

            final Player player = packet.getPlayer();

            if (compareMaterial(player.getInventory().getItemInMainHand().getType(), getMaterial(handcuffs))) {

                if (packet.getInteractEntity().getTarget().isPresent()) {

                    final Entity target = Targeter.getTargetEntity(player);

                    if (target instanceof Player) {

                        final Profile targetProfile = LibertyCity.getInstance().getProfileManager().getProfile(target.getUniqueId());

                        if (targetProfile.isHandcuffed) {

                            ((Player) target).sendTitle("§a§lVOUS AVEZ ÉTÉ LIBÉRÉ", "§a" + NameConverter.getFullName(player) + "§7 vous a libéré.", 0, 20*3, 20);
                            targetProfile.isHandcuffed = false;
                            targetProfile.handcuffedBy = null;
                            Bukkit.getScheduler().cancelTask(handcuffDelayTask.getTaskId());
                            Bukkit.getScheduler().cancelTask(teleportTask.getTaskId());
                            handcuffDelayTask = null;
                            teleportTask = null;
                            ChatUtils.multicast("§aVous avez libéré §e" + NameConverter.getFullName((Player) target) + "§a!", player);
                            ChatUtils.multicast("§aVous avez été libéré par §e" + NameConverter.getFullName(player), (Player) target);
                        } else {

                            ((Player) target).sendTitle("§c§LVOUS ÊTES MENOTTÉ", "§7Veuillez coopérer avec §c" + NameConverter.getFullName(player), 0, 20*86400, 0);
                            targetProfile.isHandcuffed = true;
                            targetProfile.handcuffedBy = player;
                            ChatUtils.multicast("§cVous avez menotté §e" + NameConverter.getFullName((Player) target) + "§c!", player);
                            ChatUtils.multicast("§cVous avez été menotté par §e" + NameConverter.getFullName(player), (Player) target);
                        }
                        return;
                    }
                }
            }
        }

        if (packet.getType() == PacketType.Play.Client.PLAYER_POSITION || packet.getType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || packet.getType() == PacketType.Play.Client.PLAYER_FLYING) {

            final Player player = packet.getPlayer();
            final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

            if (profile.isHandcuffed) {

                packet.getEvent().setCancelled(true);
                if (teleportTask == null && profile.handcuffedBy != null) {
                    teleportTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.teleport(player);
                        }
                    }.runTaskTimer(LibertyCity.getInstance(), 3L, 3L);
                }
                if (handcuffDelayTask == null && profile.handcuffedBy != null) {
                    handcuffDelayTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            assert profile.isHandcuffed;
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§lVous ne pouvez pas bouger, vous êtes menotté!"));
                            if (Math.abs(profile.handcuffedBy.getLocation().distance(player.getLocation())) >= 8) {
                                if (profile.handcuffedDelay >= 30) {
                                    profile.isHandcuffed = false;
                                    profile.handcuffedDelay = 0;
                                    profile.handcuffedBy.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§4§lLe suspect menotté c'est échappé!"));
                                    profile.handcuffedBy = null;
                                    player.sendTitle("§a§lVOUS AVEZ ÉTÉ LIBÉRÉ", "§7Le policier est resté trop loin! Échappez-vous!", 0, 20 * 3, 20);
                                    Bukkit.getScheduler().cancelTask(handcuffDelayTask.getTaskId());
                                    Bukkit.getScheduler().cancelTask(teleportTask.getTaskId());
                                    handcuffDelayTask = null;
                                    teleportTask = null;
                                } else {
                                    profile.handcuffedDelay++;
                                    player.sendTitle("§c§lVOUS ÊTES MENOTTÉ", "§7Vous pourrez vous échappez dans §c" + (30 - profile.handcuffedDelay) + " secondes§7...", 0, 20 * 3, 20);
                                    profile.handcuffedBy.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§4§lVous vous éloignez du suspect menotté!"));
                                }
                            } else {
                                if (profile.handcuffedDelay > 0) {
                                    profile.handcuffedDelay--;
                                    player.sendTitle("§c§LVOUS ÊTES MENOTTÉ", "§7Veuillez coopérer avec §c" + NameConverter.getFullName(profile.handcuffedBy), 0, 20*86400, 0);
                                }
                            }
                        }
                    }.runTaskTimerAsynchronously(LibertyCity.getInstance(), 20L, 20L);
                }
            }
        }
    }

    public void handle(final ServerPlayPacket packet) {}
}
