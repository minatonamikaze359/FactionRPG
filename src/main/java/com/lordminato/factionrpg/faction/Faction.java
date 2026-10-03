package com.lordminato.factionrpg.faction;

public enum Faction {
    LIGHT_VANGUARD("Light Vanguard", "§6"),
    SHADOW_SYNDICATE("Shadow Syndicate", "§5");

    private final String displayName;
    private final String color;

    Faction(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }

    public static Faction fromString(String name) {
        for (Faction faction : values()) {
            if (faction.name().equalsIgnoreCase(name) ||
                faction.getDisplayName().equalsIgnoreCase(name)) {
                return faction;
            }
        }
        return null;
    }
}
