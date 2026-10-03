package com.lordminato.factionrpg.listener;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.ability.Ability;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final FactionRPG plugin;

    public PlayerInteractListener(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (rpgPlayer == null) {
            return;
        }

        // Check for ability activation
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Check if player is holding an item (not a block)
            if (event.getItem() != null && event.getItem().getType() != Material.AIR) {
                return;
            }

            // Get player's faction ability
            Ability ability = plugin.getFactionManager().getAbility(rpgPlayer.getFaction());
            if (ability != null) {
                ability.activate(player);
                event.setCancelled(true);
            }
        }
    }
}
