package com.xiii.libertycity.manager.profile;

import com.xiii.libertycity.manager.Initializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager implements Initializer {

    private final Map<UUID, Profile> profiles = new ConcurrentHashMap<>();

    @Override
    public void initialize() {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(Objects::nonNull)
                .forEach(this::createProfile);
    }

    public void createProfile(Player player) {

        UUID uuid = player.getUniqueId();

        if (this.profiles.containsKey(uuid)) return;

        this.profiles.put(uuid, new Profile(player));
    }

    public void removeProfile(Player player) {
        this.profiles.remove(player.getUniqueId());
    }

    public Profile getProfile(Player player) {
        return this.profiles.get(player.getUniqueId());
    }

    public Map<UUID, Profile> getProfileMap() {
        return this.profiles;
    }

    @Override
    public void shutdown() {
        this.profiles.clear();
    }
}
