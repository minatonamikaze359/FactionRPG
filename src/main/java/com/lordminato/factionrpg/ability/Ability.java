package com.lordminato.factionrpg.ability;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Ability {
    protected final FactionRPG plugin;
    protected final Faction faction;
    private final Map<UUID, Long> cooldowns;

    public Ability(FactionRPG plugin, Faction faction) {
        this.plugin = plugin;
        this.faction = faction;
        this.cooldowns = new HashMap<>();
    }

    public abstract void activate(Player player);

    public abstract String getName();

    public abstract String getDescription();

    public abstract long getCooldown();

    public boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long cooldownEnd = cooldowns.get(player.getUniqueId());

        return currentTime < cooldownEnd;
    }

    public long getRemainingCooldown(Player player) {
        if (!isOnCooldown(player)) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long cooldownEnd = cooldowns.get(player.getUniqueId());

        return (cooldownEnd - currentTime) / 1000;
    }

    protected void startCooldown(Player player) {
        long cooldownTime = getCooldown() * 1000;
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldownTime);
    }

    protected void clearCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }

    public Faction getFaction() {
        return faction;
    }
}
