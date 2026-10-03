package com.lordminato.factionrpg.listener;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final FactionRPG plugin;

    public PlayerDeathListener(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Check if killer is a player and not the same as victim
        if (killer != null && !killer.equals(victim)) {
            // Check if friendly fire is disabled and players are in the same faction
            if (!plugin.getConfigManager().getConfig().getBoolean("factions.friendly-fire", false)) {
                RPGPlayer victimPlayer = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
                RPGPlayer killerPlayer = plugin.getPlayerManager().getPlayer(killer.getUniqueId());

                if (victimPlayer != null && killerPlayer != null &&
                    victimPlayer.getFaction() == killerPlayer.getFaction()) {
                    event.setDeathMessage(null); // Cancel death message
                    killer.sendMessage(plugin.getConfigManager().getMessage("friendly-fire-disabled"));
                    return;
                }
            }

            // Award XP to killer if configured
            if (plugin.getConfigManager().getConfig().getBoolean("xp.enabled", true)) {
                RPGPlayer killerPlayer = plugin.getPlayerManager().getPlayer(killer.getUniqueId());
                if (killerPlayer != null) {
                    double xpMultiplier = plugin.getConfigManager().getConfig().getDouble("xp.multiplier", 1.0);
                    double xpAmount = plugin.getConfigManager().getConfig().getDouble("xp.player-kill", 50) * xpMultiplier;
                    killerPlayer.addXp(xpAmount);

                    // Show actionbar
                    killer.sendActionBar("§a+" + (int)xpAmount + " RPG XP");
                }
            }
        }
    }
}
