package com.lordminato.factionrpg.ability;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HolyShield extends Ability {

    public HolyShield(FactionRPG plugin) {
        super(plugin, Faction.LIGHT_VANGUARD);
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

        // Apply effects
        player.addPotionEffect(new PotionEffect(
            PotionEffectType.REGENERATION,
            20 * plugin.getConfigManager().getConfig().getInt("abilities.holy-shield.duration", 5),
            plugin.getConfigManager().getConfig().getInt("abilities.holy-shield.regeneration-amplifier", 1)
        ));

        player.addPotionEffect(new PotionEffect(
            PotionEffectType.DAMAGE_RESISTANCE,
            20 * plugin.getConfigManager().getConfig().getInt("abilities.holy-shield.duration", 5),
            plugin.getConfigManager().getConfig().getInt("abilities.holy-shield.resistance-amplifier", 1)
        ));

        // Play sound
        player.playSound(player.getLocation(), "block.beacon.activate", 1.0f, 1.0f);

        // Spawn particles
        player.spawnParticle(org.bukkit.Particle.END_ROD, player.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);

        // Show actionbar
        player.sendActionBar("§6✨ " + getName() + " activated!");

        // Start cooldown
        startCooldown(player);
    }

    @Override
    public String getName() {
        return "Holy Shield";
    }

    @Override
    public String getDescription() {
        return "Grants regeneration and resistance for a short duration.";
    }

    @Override
    public long getCooldown() {
        return plugin.getConfigManager().getConfig().getLong("abilities.holy-shield.cooldown", 45);
    }
}
