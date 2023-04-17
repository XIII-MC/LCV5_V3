package com.xiii.libertycity.roleplay.items.search.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.items.handcuffs.utils.Targeter;
import com.xiii.libertycity.roleplay.items.search.utils.InvSee;
import com.xiii.libertycity.roleplay.utils.ItemUtils;
import com.xiii.libertycity.roleplay.utils.NameConverter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SearchHandle extends ItemUtils implements Data {

    public void handle(final ClientPlayPacket packet) {

        if (packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {

            final Player player = packet.getPlayer();

            if (compareMaterial(player.getInventory().getItemInMainHand().getType(), getMaterial(searchItem))) {

                if (packet.getInteractEntity().getTarget().isPresent()) {

                    final Entity target = Targeter.getTargetEntity(player);

                    if (target instanceof Player) {

                        final Profile targetProfile = LibertyCity.getInstance().getProfileManager().getProfile(target.getUniqueId());
                        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

                        if (targetProfile.isSearched) {

                            targetProfile.isSearched = false;
                            profile.isSearching = false;
                            profile.searchingWho = null;
                            targetProfile.searchedBy.closeInventory();
                            targetProfile.searchedBy = null;
                            ChatUtils.multicast("§aVous avez libéré §e" + NameConverter.getFullName((Player) target) + "§a!", player);
                            ChatUtils.multicast("§aVous avez été libéré par §e" + NameConverter.getFullName(player) + "§a!", (Player) target);
                        } else {

                            targetProfile.isSearched = true;
                            profile.isSearching = true;
                            profile.searchingWho = (Player) target;
                            targetProfile.searchedBy = player;
                            player.openInventory(InvSee.invSee((Player) target));
                            ChatUtils.multicast("§cVous fouillez §e" + NameConverter.getFullName((Player) target) + "§c!", player);
                            ChatUtils.multicast("§cVous vous faites fouillez par §e" + NameConverter.getFullName(player) + "§c!", (Player) target);
                        }
                    }
                }
            }
        }

        if (packet.getType() == PacketType.Play.Client.CLOSE_WINDOW) {

            final Player player = packet.getPlayer();
            final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

            if (profile.isSearching && profile.searchingWho != null) {

                final Profile targetProfile = LibertyCity.getInstance().getProfileManager().getProfile(profile.searchingWho.getUniqueId());

                targetProfile.isSearching = false;
                targetProfile.searchedBy = null;
                profile.isSearching = false;

                ChatUtils.multicast("§aVous avez libéré §e" + profile.searchingWho.getName() + "§a!", player);
                ChatUtils.multicast("§aVous avez été libéré par §e" + player.getName() + "§a!", profile.searchingWho);

                profile.searchingWho = null;
            }
        }

    }


    public void handle(final ServerPlayPacket packet) {
    }
}
