package com.lordminato.factionrpg.listener;

import com.lordminato.factionrpg.FactionRPG;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final FactionRPG plugin;

    public PlayerQuitListener(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Save player data
        plugin.getPlayerManager().savePlayerData(event.getPlayer().getUniqueId());
    }
}
