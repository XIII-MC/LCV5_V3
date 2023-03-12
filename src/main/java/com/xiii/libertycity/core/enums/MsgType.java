package com.xiii.libertycity.core.enums;

public enum MsgType {
    PREFIX("§2§lLiberty§a§lCity §7»§f "),
    MAIRIE("§6§lMairie §7»§f "),
    MICHEL_FULL("§e§l[NPC]§r§e Michel: §r"),
    STEVE_FIRST("§e§l[NPC]§r§e Steve §kRowland§r§e: §r"),
    STEVE_FULL("§e§l[NPC]§r§e Steve Rowland: §r"),
    STEVE_HIDDEN("§e§l[NPC]§r§e §kSteve Rowland§r§e: §r");

    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
