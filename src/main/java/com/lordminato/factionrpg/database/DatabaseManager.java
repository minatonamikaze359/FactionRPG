package com.lordminato.factionrpg.database;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.player.RPGPlayer;
import com.lordminato.factionrpg.quest.Quest;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    private final FactionRPG plugin;
    private Connection connection;

    public DatabaseManager(FactionRPG plugin) {
        this.plugin = plugin;
        initializeDatabase();
    }

    private void initializeDatabase() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        String url = "jdbc:sqlite:" + dataFolder.getPath() + "/data.db";

        try {
            connection = DriverManager.getConnection(url);
            createTables();
            plugin.getLogger().info("[FactionRPG] SQLite database initialized successfully.");
        } catch (SQLException e) {
            plugin.getLogger().severe("[FactionRPG] Failed to initialize database: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        String playersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "uuid TEXT PRIMARY KEY," +
                "username TEXT NOT NULL," +
                "faction TEXT NOT NULL," +
                "level INTEGER NOT NULL DEFAULT 1," +
                "xp DOUBLE NOT NULL DEFAULT 0," +
                "faction_xp DOUBLE NOT NULL DEFAULT 0," +
                "quest_id TEXT," +
                "quest_progress INTEGER NOT NULL DEFAULT 0," +
                "quest_completed INTEGER NOT NULL DEFAULT 0," +
                "quest_reset_at INTEGER NOT NULL DEFAULT 0," +
                "first_join INTEGER NOT NULL," +
                "last_seen INTEGER NOT NULL" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(playersTable);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("[FactionRPG] Error closing database connection: " + e.getMessage());
            }
        }
    }

    public boolean playerExists(UUID uuid) {
        String query = "SELECT 1 FROM players WHERE uuid = ? LIMIT 1;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            plugin.getLogger().severe("[FactionRPG] Error checking if player exists: " + e.getMessage());
            return false;
        }
    }

    public void createPlayer(RPGPlayer player) {
        String query = "INSERT INTO players (uuid, username, faction, level, xp, faction_xp, quest_id, " +
                "quest_progress, quest_completed, quest_reset_at, first_join, last_seen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());
            stmt.setString(3, player.getFaction().name());
            stmt.setInt(4, player.getLevel());
            stmt.setDouble(5, player.getXp());
            stmt.setDouble(6, player.getFactionXp());
            stmt.setString(7, player.getCurrentQuest() != null ? player.getCurrentQuest().getId() : null);
            stmt.setInt(8, player.getQuestProgress());
            stmt.setInt(9, player.isQuestCompleted() ? 1 : 0);
            stmt.setLong(10, player.getQuestResetTimestamp());
            stmt.setLong(11, player.getFirstJoinTimestamp());
            stmt.setLong(12, player.getLastSeenTimestamp());

            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("[FactionRPG] Error creating player record: " + e.getMessage());
        }
    }

    public RPGPlayer loadPlayer(UUID uuid) {
        String query = "SELECT * FROM players WHERE uuid = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                RPGPlayer player = new RPGPlayer(
                    UUID.fromString(rs.getString("uuid")),
                    rs.getString("username"),
                    Faction.valueOf(rs.getString("faction")),
                    rs.getInt("level"),
                    rs.getDouble("xp"),
                    rs.getDouble("faction_xp"),
                    null, // We'll set the quest separately
                    rs.getInt("quest_progress"),
                    rs.getInt("quest_completed") == 1,
                    rs.getLong("quest_reset_at"),
                    rs.getLong("first_join"),
                    rs.getLong("last_seen")
                );

                // Load quest if exists
                String questId = rs.getString("quest_id");
                if (questId != null) {
                    Quest quest = plugin.getConfigManager().getQuestById(questId);
                    if (quest != null) {
                        player.setCurrentQuest(quest);
                    }
                }

                return player;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("[FactionRPG] Error loading player: " + e.getMessage());
        }
        return null;
    }

    public void updatePlayer(RPGPlayer player) {
        String query = "UPDATE players SET username = ?, faction = ?, level = ?, xp = ?, faction_xp = ?, " +
                "quest_id = ?, quest_progress = ?, quest_completed = ?, quest_reset_at = ?, last_seen = ? " +
                "WHERE uuid = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getFaction().name());
            stmt.setInt(3, player.getLevel());
            stmt.setDouble(4, player.getXp());
            stmt.setDouble(5, player.getFactionXp());
            stmt.setString(6, player.getCurrentQuest() != null ? player.getCurrentQuest().getId() : null);
            stmt.setInt(7, player.getQuestProgress());
            stmt.setInt(8, player.isQuestCompleted() ? 1 : 0);
            stmt.setLong(9, player.getQuestResetTimestamp());
            stmt.setLong(10, player.getLastSeenTimestamp());
            stmt.setString(11, player.getUniqueId().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("[FactionRPG] Error updating player: " + e.getMessage());
        }
    }

    // Add more database methods as needed
}
