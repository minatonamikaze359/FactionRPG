package com.lordminato.factionrpg.quest;

import java.util.Map;

public class Quest {
    public enum QuestType {
        KILL_MOB,
        BREAK_BLOCK,
        CRAFT_ITEM,
        PLACE_BLOCK
    }

    private final String id;
    private final String displayName;
    private final QuestType type;
    private final String target;
    private final int amount;
    private final double xpReward;
    private final double factionXpReward;
    private final double moneyReward;

    public Quest(String id, String displayName, QuestType type, String target, int amount,
                double xpReward, double factionXpReward, double moneyReward) {
        this.id = id;
        this.displayName = displayName;
        this.type = type;
        this.target = target;
        this.amount = amount;
        this.xpReward = xpReward;
        this.factionXpReward = factionXpReward;
        this.moneyReward = moneyReward;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public QuestType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }

    public double getXpReward() {
        return xpReward;
    }

    public double getFactionXpReward() {
        return factionXpReward;
    }

    public double getMoneyReward() {
        return moneyReward;
    }

    // Factory method to create from config
    public static Quest fromConfig(String id, Map<String, Object> config) {
        String displayName = (String) config.getOrDefault("display-name", id);
        QuestType type = QuestType.valueOf((String) config.get("type"));
        String target = (String) config.get("target");
        int amount = (int) config.get("amount");

        double xpReward = 0;
        double factionXpReward = 0;
        double moneyReward = 0;

        if (config.containsKey("rewards")) {
            Map<String, Object> rewards = (Map<String, Object>) config.get("rewards");
            xpReward = rewards.containsKey("xp") ? ((Number) rewards.get("xp")).doubleValue() : 0;
            factionXpReward = rewards.containsKey("faction-xp") ? ((Number) rewards.get("faction-xp")).doubleValue() : 0;
            moneyReward = rewards.containsKey("money") ? ((Number) rewards.get("money")).doubleValue() : 0;
        }

        return new Quest(id, displayName, type, target, amount, xpReward, factionXpReward, moneyReward);
    }
}
