package com.lordminato.factionrpg.player;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import com.lordminato.factionrpg.quest.Quest;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RPGPlayer {
    private final UUID uniqueId;
    private String name;
    private Faction faction;
    private int level;
    private double xp;
    private double factionXp;
    private Quest currentQuest;
    private int questProgress;
    private boolean questCompleted;
    private long questResetTimestamp;
    private long firstJoinTimestamp;
    private long lastSeenTimestamp;

    public RPGPlayer(UUID uniqueId, String name, Faction faction, int level, double xp, double factionXp,
                    Quest currentQuest, int questProgress, boolean questCompleted,
                    long questResetTimestamp, long firstJoinTimestamp, long lastSeenTimestamp) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.faction = faction;
        this.level = level;
        this.xp = xp;
        this.factionXp = factionXp;
        this.currentQuest = currentQuest;
        this.questProgress = questProgress;
        this.questCompleted = questCompleted;
        this.questResetTimestamp = questResetTimestamp;
        this.firstJoinTimestamp = firstJoinTimestamp;
        this.lastSeenTimestamp = lastSeenTimestamp;
    }

    // Getters and setters
    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getFactionXp() {
        return factionXp;
    }

    public void setFactionXp(double factionXp) {
        this.factionXp = factionXp;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
    }

    public int getQuestProgress() {
        return questProgress;
    }

    public void setQuestProgress(int questProgress) {
        this.questProgress = questProgress;
    }

    public boolean isQuestCompleted() {
        return questCompleted;
    }

    public void setQuestCompleted(boolean questCompleted) {
        this.questCompleted = questCompleted;
    }

    public long getQuestResetTimestamp() {
        return questResetTimestamp;
    }

    public void setQuestResetTimestamp(long questResetTimestamp) {
        this.questResetTimestamp = questResetTimestamp;
    }

    public long getFirstJoinTimestamp() {
        return firstJoinTimestamp;
    }

    public long getLastSeenTimestamp() {
        return lastSeenTimestamp;
    }

    public void setLastSeenTimestamp(long lastSeenTimestamp) {
        this.lastSeenTimestamp = lastSeenTimestamp;
    }

    // RPG leveling methods
    public void addXp(double amount) {
        if (level >= FactionRPG.getInstance().getConfigManager().getMaxLevel()) {
            return;
        }

        this.xp += amount;
        checkLevelUp();
    }

    public void checkLevelUp() {
        int maxLevel = FactionRPG.getInstance().getConfigManager().getMaxLevel();
        if (level >= maxLevel) {
            return;
        }

        double requiredXp = getRequiredXpForNextLevel();
        while (xp >= requiredXp && level < maxLevel) {
            level++;
            xp -= requiredXp;
            requiredXp = getRequiredXpForNextLevel();

            // Level up effects
            Player player = FactionRPG.getInstance().getServer().getPlayer(uniqueId);
            if (player != null && player.isOnline()) {
                String levelUpMessage = FactionRPG.getInstance().getConfigManager().getMessage("level-up")
                    .replace("%level%", String.valueOf(level));
                player.sendMessage(levelUpMessage);

                // Play sound
                player.playSound(player.getLocation(), "entity.player.levelup", 1.0f, 1.0f);

                // Spawn particles
                player.spawnParticle(org.bukkit.Particle.TOTEM, player.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);

                // Show title
                player.sendTitle("§6LEVEL UP!", "§eYou reached Level " + level + "!", 10, 70, 20);
            }
        }
    }

    public double getRequiredXpForNextLevel() {
        // Default formula: 100 + ((Level - 1) * 50)
        return 100 + ((level - 1) * 50);
    }

    // Quest methods
    public void checkQuestProgress(Quest.QuestType type, String target, int amount) {
        if (currentQuest == null || questCompleted) {
            return;
        }

        if (currentQuest.getType() == type && currentQuest.getTarget().equalsIgnoreCase(target)) {
            questProgress += amount;

            if (questProgress >= currentQuest.getAmount()) {
                questProgress = currentQuest.getAmount();
                completeQuest();
            }

            // Update progress in database
            FactionRPG.getInstance().getDatabaseManager().updatePlayer(this);
        }
    }

    private void completeQuest() {
        questCompleted = true;

        // Give rewards
        Player player = FactionRPG.getInstance().getServer().getPlayer(uniqueId);
        if (player != null && player.isOnline()) {
            String completionMessage = FactionRPG.getInstance().getConfigManager().getMessage("quest-complete");
            player.sendMessage(completionMessage);

            // Add XP rewards
            addXp(currentQuest.getXpReward());
            factionXp += currentQuest.getFactionXpReward();

            // Add money reward if Vault is available
            if (FactionRPG.getInstance().getVaultHook().isEconomyEnabled()) {
                FactionRPG.getInstance().getVaultHook().getEconomy().depositPlayer(player, currentQuest.getMoneyReward());
            }

            // Show actionbar
            player.sendActionBar("§aQuest completed! §e+" + currentQuest.getXpReward() + " RPG XP");
        }

        // Update in database
        FactionRPG.getInstance().getDatabaseManager().updatePlayer(this);
    }

    public void resetQuest() {
        currentQuest = null;
        questProgress = 0;
        questCompleted = false;
        questResetTimestamp = System.currentTimeMillis();

        // Update in database
        FactionRPG.getInstance().getDatabaseManager().updatePlayer(this);
    }
}
