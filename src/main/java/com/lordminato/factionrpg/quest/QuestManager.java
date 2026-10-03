package com.lordminato.factionrpg.quest;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.player.RPGPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestManager {
    private final FactionRPG plugin;
    private final List<Quest> questPool;
    private final Random random;

    public QuestManager(FactionRPG plugin) {
        this.plugin = plugin;
        this.questPool = new ArrayList<>();
        this.random = new Random();
        loadQuests();
    }

    private void loadQuests() {
        questPool.clear();

        if (plugin.getConfigManager().getQuests().contains("quests")) {
            for (String questId : plugin.getConfigManager().getQuests().getConfigurationSection("quests").getKeys(false)) {
                Quest quest = Quest.fromConfig(questId,
                    plugin.getConfigManager().getQuests().getConfigurationSection("quests." + questId).getValues(false));
                questPool.add(quest);
            }
        }
    }

    public Quest getRandomQuest() {
        if (questPool.isEmpty()) {
            return null;
        }
        return questPool.get(random.nextInt(questPool.size()));
    }

    public void checkQuestReset(RPGPlayer player) {
        long resetInterval = plugin.getConfigManager().getConfig().getLong("quests.reset-interval", 86400) * 1000;
        long currentTime = System.currentTimeMillis();

        if (currentTime - player.getQuestResetTimestamp() >= resetInterval) {
            player.resetQuest();
            assignNewQuest(player);
        }
    }

    public void assignNewQuest(RPGPlayer player) {
        Quest newQuest = getRandomQuest();
        if (newQuest != null) {
            player.setCurrentQuest(newQuest);
            player.setQuestProgress(0);
            player.setQuestCompleted(false);
            player.setQuestResetTimestamp(System.currentTimeMillis());

            // Update in database
            plugin.getDatabaseManager().updatePlayer(player);
        }
    }

    public void reloadQuests() {
        loadQuests();
    }
}
