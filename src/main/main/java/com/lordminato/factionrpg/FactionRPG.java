package com.lordminato.factionrpg;

import org.bukkit.plugin.java.JavaPlugin;
import com.lordminato.factionrpg.command.*;
import com.lordminato.factionrpg.config.*;
import com.lordminato.factionrpg.database.*;
import com.lordminato.factionrpg.faction.*;
import com.lordminato.factionrpg.listener.*;
import com.lordminato.factionrpg.hook.*;
import com.lordminato.factionrpg.player.*;

public class FactionRPG extends JavaPlugin {
    private static FactionRPG instance;
    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private FactionManager factionManager;
    private ConfigManager configManager;
    private VaultHook vaultHook;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize managers
        configManager = new ConfigManager(this);
        databaseManager = new DatabaseManager(this);
        playerManager = new PlayerManager(this);
        factionManager = new FactionManager(this);

        // Initialize hooks
        vaultHook = new VaultHook(this);

        // Register commands
        getCommand("rpg").setExecutor(new RPGCommand(this));
        getCommand("faction").setExecutor(new FactionCommand(this));
        getCommand("quests").setExecutor(new QuestCommand(this));
        getCommand("factionrpg").setExecutor(new AdminCommand(this));

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);

        // Log startup
        getLogger().info("====================================");
        getLogger().info("       FactionRPG");
        getLogger().info("       MADE BY: LORD MINATO");
        getLogger().info("====================================");
        getLogger().info("Faction systems loaded.");
        getLogger().info("RPG systems loaded.");
        getLogger().info("Quest systems loaded.");
        getLogger().info("SQLite database loaded.");
        getLogger().info("FactionRPG is ready!");
        getLogger().info("====================================");
    }

    @Override
    public void onDisable() {
        // Save all online players
        playerManager.saveAllPlayers();

        // Close database connection
        databaseManager.closeConnection();

        // Log shutdown
        getLogger().info("[FactionRPG] Plugin disabled.");
    }

    public static FactionRPG getInstance() {
        return instance;
    }

    // Getters for managers
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public FactionManager getFactionManager() {
        return factionManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }
}
