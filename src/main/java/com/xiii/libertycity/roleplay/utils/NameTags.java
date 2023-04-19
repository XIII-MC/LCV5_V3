package com.xiii.libertycity.roleplay.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class NameTags {

    private final static Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
    private static Team t = score.getTeam("nhide");

    public static void hideNameTag(final Player player) {

        if(t == null) {
            t = score.registerNewTeam("nhide");
            t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        t.addEntry(player.getName());
    }
}
