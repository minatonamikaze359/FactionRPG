package com.lordminato.factionrpg.hook;

import com.lordminato.factionrpg.FactionRPG;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private final FactionRPG plugin;
    private Economy economy;
    private boolean economyEnabled;

    public VaultHook(FactionRPG plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private void setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault not found. Economy integration disabled.");
            economyEnabled = false;
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("No economy provider found. Economy integration disabled.");
            economyEnabled = false;
            return;
        }

        economy = rsp.getProvider();
        economyEnabled = economy != null;

        if (economyEnabled) {
            plugin.getLogger().info("Vault economy integration enabled.");
        } else {
            plugin.getLogger().warning("Failed to initialize Vault economy. Economy integration disabled.");
        }
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean isEconomyEnabled() {
        return economyEnabled;
    }
}
