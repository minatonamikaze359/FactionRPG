package com.lordminato.factionrpg.ability;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ShadowDash extends Ability {

    public ShadowDash(FactionRPG plugin) {
        super(plugin, Faction.SHADOW_SYNDICATE);
    }

    @Override
    public void activate(Player player) {
        if (isOnCooldown(player)) {
            long remaining = getRemainingCooldown(player);
            player.sendMessage(plugin.getConfigManager().getMessage("ability-on-cooldown")
                .replace("%ability%", getName())
                .replace("%time%", String.valueOf(remaining)));
            return;
        }

        // Calculate safe destination
        Location start = player.getLocation();
        Vector direction = start.getDirection().normalize();

        double distance = plugin.getConfigManager().getConfig().getDouble("abilities.shadow-dash.distance", 3.0);
        Location destination = start.clone().add(direction.multiply(distance));

        // Check for safe teleport
        if (!isSafeDestination(destination)) {
            // Try to find a safe spot
            for (double d = distance; d >= 1.0; d -= 0.5) {
                destination = start.clone().add(direction.clone().multiply(d));
                if (isSafeDestination(destination)) {
                    break;
                }
            }
        }

        // Teleport player
        player.teleport(destination);

        // Apply speed effect
        player.addPotionEffect(new PotionEffect(
            PotionEffectType.SPEED,
            20 * plugin.getConfigManager().getConfig().getInt("abilities.shadow-dash.speed-duration", 3),
            plugin.getConfigManager().getConfig().getInt("abilities.shadow-dash.speed-amplifier", 2)
        ));

        // Play sound
        player.playSound(player.getLocation(), "entity.enderman.teleport", 1.0f, 1.0f);

        // Spawn particles
        player.spawnParticle(org.bukkit.Particle.SMOKE_NORMAL, player.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);
        player.spawnParticle(org.bukkit.Particle.PORTAL, player.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);

        // Show actionbar
        player.sendActionBar("§5☾ " + getName() + " activated!");

        // Start cooldown
        startCooldown(player);
    }

    private boolean isSafeDestination(Location location) {
        // Check if the destination is safe (not in a block, not in water/lava)
        return location.getBlock().getType().isSolid() &&
               location.clone().add(0, 1, 0).getBlock().getType().isSolid() &&
               !location.clone().add(0, 1, 0).getBlock().getType().name().contains("WATER") &&
               !location.clone().add(0, 1, 0).getBlock().getType().name().contains("LAVA");
    }

    @Override
    public String getName() {
        return "Shadow Dash";
    }

    @Override
    public String getDescription() {
        return "Dash forward a short distance and gain speed.";
    }

    @Override
    public long getCooldown() {
        return plugin.getConfigManager().getConfig().getLong("abilities.shadow-dash.cooldown", 30);
    }
}
