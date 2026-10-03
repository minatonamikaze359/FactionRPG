package com.lordminato.factionrpg.player;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private final FactionRPG plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, RPGPlayer> onlinePlayers;

    public PlayerManager(FactionRPG plugin) {
        this.plugin = plugin;
        this.databaseManager = plugin.getDatabaseManager();
        this.onlinePlayers = new ConcurrentHashMap<>();
    }

    /**
     * Loads player data when a player joins the server
     * @param player The player to load data for
     */
    public void loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        // Check if player exists in database
        if (!databaseManager.playerExists(uuid)) {
            // Create new player record
            RPGPlayer newPlayer = new RPGPlayer(
                uuid,
                name,
                null, // Faction will be set later via GUI
                1,    // Start at level 1
                0,    // Start with 0 XP
                0,    // Start with 0 faction XP
                null, // No quest assigned yet
                0,    // Quest progress
                false, // Quest not completed
                0,    // Quest reset timestamp
                System.currentTimeMillis(), // First join timestamp
                System.currentTimeMillis()  // Last seen timestamp
            );

            databaseManager.createPlayer(newPlayer);
            onlinePlayers.put(uuid, newPlayer);

            // Send welcome message
            player.sendMessage(plugin.getConfigManager().getMessage("welcome-message"));
        } else {
            // Load existing player data
            RPGPlayer loadedPlayer = databaseManager.loadPlayer(uuid);

            // Update username if changed
            if (!loadedPlayer.getName().equals(name)) {
                loadedPlayer.setName(name);
                databaseManager.updatePlayer(loadedPlayer);
            }

            // Update last seen timestamp
            loadedPlayer.setLastSeenTimestamp(System.currentTimeMillis());

            onlinePlayers.put(uuid, loadedPlayer);
        }
    }

    /**
     * Saves player data when a player quits the server
     * @param uuid The UUID of the player to save
     */
    public void savePlayerData(UUID uuid) {
        if (onlinePlayers.containsKey(uuid)) {
            RPGPlayer player = onlinePlayers.get(uuid);
            player.setLastSeenTimestamp(System.currentTimeMillis());
            databaseManager.updatePlayer(player);
            onlinePlayers.remove(uuid);
        }
    }

    /**
     * Saves all online players' data
     */
    public void saveAllPlayers() {
        for (RPGPlayer player : onlinePlayers.values()) {
            player.setLastSeenTimestamp(System.currentTimeMillis());
            databaseManager.updatePlayer(player);
        }
        onlinePlayers.clear();
    }

    /**
     * Gets the RPGPlayer object for a player
     * @param uuid The UUID of the player
     * @return The RPGPlayer object or null if not found
     */
    public RPGPlayer getPlayer(UUID uuid) {
        return onlinePlayers.get(uuid);
    }

    /**
     * Checks if a player has an RPG profile loaded
     * @param uuid The UUID of the player
     * @return true if the player has a loaded profile
     */
    public boolean hasPlayer(UUID uuid) {
        return onlinePlayers.containsKey(uuid);
    }

    /**
     * Gets all online RPG players
     * @return A collection of all online RPG players
     */
    public Map<UUID, RPGPlayer> getOnlinePlayers() {
        return new HashMap<>(onlinePlayers);
    }

    /**
     * Updates a player's data in the database
     * @param player The RPGPlayer to update
     */
    public void updatePlayer(RPGPlayer player) {
        if (onlinePlayers.containsKey(player.getUniqueId())) {
            databaseManager.updatePlayer(player);
        }
    }

    /**
     * Gets a player's faction
     * @param uuid The UUID of the player
     * @return The player's faction or null if not found
     */
    public com.lordminato.factionrpg.faction.Faction getPlayerFaction(UUID uuid) {
        RPGPlayer player = getPlayer(uuid);
        return player != null ? player.getFaction() : null;
    }

    /**
     * Checks if two players are in the same faction
     * @param uuid1 First player's UUID
     * @param uuid2 Second player's UUID
     * @return true if both players exist and are in the same faction
     */
    public boolean areInSameFaction(UUID uuid1, UUID uuid2) {
        RPGPlayer player1 = getPlayer(uuid1);
        RPGPlayer player2 = getPlayer(uuid2);

        if (player1 == null || player2 == null ||
            player1.getFaction() == null || player2.getFaction() == null) {
            return false;
        }

        return player1.getFaction() == player2.getFaction();
    }

    /**
     * Handles player name changes
     * @param oldName The old player name
     * @param newName The new player name
     */
    public void handleNameChange(String oldName, String newName) {
        Player player = Bukkit.getPlayer(newName);
        if (player != null) {
            RPGPlayer rpgPlayer = getPlayer(player.getUniqueId());
            if (rpgPlayer != null) {
                rpgPlayer.setName(newName);
                updatePlayer(rpgPlayer);
            }
        }
    }
}
