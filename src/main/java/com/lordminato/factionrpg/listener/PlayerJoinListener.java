package com.lordminato.factionrpg.listener;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.gui.FactionSelectionGUI;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final FactionRPG plugin;

    public PlayerJoinListener(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load or create player data
        plugin.getPlayerManager().loadPlayerData(event.getPlayer());

        // Check if player has a faction
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if (rpgPlayer != null && rpgPlayer.getFaction() == null) {
            // Open faction selection GUI
            new FactionSelectionGUI(plugin, event.getPlayer()).open();
        }

        // Check quest reset
        if (rpgPlayer != null) {
            plugin.getQuestManager().checkQuestReset(rpgPlayer);
        }
    }
}
