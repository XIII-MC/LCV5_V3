package com.xiii.libertycity.core.enums;

public enum MsgType {
    PREFIX("§2§lLiberty§a§lCity §7»§f "),
    BANK("§2§lLiberty§6§LBank §7»§f "),
    BANK_EXPRESS("§2§lLiberty§6§LBank§e§l§oEXPRESS §7»§f "),
    MAIRIE("§6§lMairie §7»§f "),
    HRP("§3§lHRP §8| "),
    RP("§2§LRP §8|"),
    POLICE("§b§lPOLICE §8| "),
    GANG("§4§lGANG §8| "),
    STAFF("§4§lSTAFF §8| "),
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
