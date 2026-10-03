package com.lordminato.factionrpg.faction;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.ability.Ability;
import com.lordminato.factionrpg.ability.HolyShield;
import com.lordminato.factionrpg.ability.ShadowDash;

import java.util.HashMap;
import java.util.Map;

public class FactionManager {
    private final FactionRPG plugin;
    private final Map<Faction, Ability> factionAbilities;

    public FactionManager(FactionRPG plugin) {
        this.plugin = plugin;
        this.factionAbilities = new HashMap<>();

        // Initialize faction abilities
        initializeAbilities();
    }

    private void initializeAbilities() {
        factionAbilities.put(Faction.LIGHT_VANGUARD, new HolyShield(plugin));
        factionAbilities.put(Faction.SHADOW_SYNDICATE, new ShadowDash(plugin));
    }

    public Ability getAbility(Faction faction) {
        return factionAbilities.get(faction);
    }

    // Add more faction-related methods as needed
}
