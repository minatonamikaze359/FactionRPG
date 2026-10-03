package com.lordminato.factionrpg.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.lordminato.factionrpg.FactionRPG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final FactionRPG plugin;
    private FileConfiguration config;
    private FileConfiguration messages;
    private FileConfiguration quests;
    private FileConfiguration items;

    public ConfigManager(FactionRPG plugin) {
        this.plugin = plugin;
        createConfig();
        createMessages();
        createQuests();
        createItems();
    }

    private void createConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void createMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    private void createQuests() {
        File questsFile = new File(plugin.getDataFolder(), "quests.yml");
        if (!questsFile.exists()) {
            plugin.saveResource("quests.yml", false);
        }
        quests = YamlConfiguration.loadConfiguration(questsFile);
    }

    private void createItems() {
        File itemsFile = new File(plugin.getDataFolder(), "items.yml");
        if (!itemsFile.exists()) {
            plugin.saveResource("items.yml", false);
        }
        items = YamlConfiguration.loadConfiguration(itemsFile);
    }

    public void reloadConfigs() {
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        messages = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "messages.yml"));
        quests = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "quests.yml"));
        items = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "items.yml"));
    }

    // Getters for configuration sections
    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public FileConfiguration getQuests() {
        return quests;
    }

    public FileConfiguration getItems() {
        return items;
    }

    // Helper methods to get specific config values
    public String getMessage(String path) {
        return messages.getString(path, "Message not found: " + path);
    }

    public int getMaxLevel() {
        return config.getInt("rpg.max-level", 50);
    }

    public Map<String, Integer> getMobXPValues() {
        Map<String, Integer> mobXP = new HashMap<>();
        if (config.contains("xp.mobs")) {
            for (String key : config.getConfigurationSection("xp.mobs").getKeys(false)) {
                mobXP.put(key, config.getInt("xp.mobs." + key));
            }
        }
        return mobXP;
    }

    // Add more helper methods as needed
}
