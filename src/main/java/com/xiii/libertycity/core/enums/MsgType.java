package com.xiii.libertycity.core.enums;

public enum MsgType {
    PREFIX("§2§lLiberty§a§lCity §7» ");

    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
