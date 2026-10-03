package com.lordminato.factionrpg.listener;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import com.lordminato.factionrpg.item.CustomItem;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageListener implements Listener {
    private final FactionRPG plugin;

    public EntityDamageListener(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        // Check for friendly fire
        if (victim instanceof Player) {
            Player victimPlayer = (Player) victim;
            RPGPlayer victimRpg = plugin.getPlayerManager().getPlayer(victimPlayer.getUniqueId());

            Player attacker = null;
            if (damager instanceof Player) {
                attacker = (Player) damager;
            } else if (damager instanceof Projectile) {
                Projectile projectile = (Projectile) damager;
                if (projectile.getShooter() instanceof Player) {
                    attacker = (Player) projectile.getShooter();
                }
            }

            if (attacker != null && victimRpg != null) {
                RPGPlayer attackerRpg = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());

                if (attackerRpg != null && victimRpg.getFaction() == attackerRpg.getFaction()) {
                    if (!plugin.getConfigManager().getConfig().getBoolean("factions.friendly-fire", false)) {
                        event.setCancelled(true);
                        attacker.sendMessage(plugin.getConfigManager().getMessage("friendly-fire-disabled"));
                        return;
                    }
                }
            }
        }

        // Handle custom weapon damage
        if (damager instanceof Player) {
            Player attacker = (Player) damager;
            ItemStack item = attacker.getInventory().getItemInMainHand();

            if (plugin.getItemManager().isCustomItem(item)) {
                String itemId = plugin.getItemManager().getCustomItemId(item);
                CustomItem customItem = plugin.getItemManager().getCustomItem(itemId);

                if (customItem != null) {
                    // Check faction restriction
                    RPGPlayer attackerRpg = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
                    if (attackerRpg != null && customItem.getRequiredFaction() != null &&
                        attackerRpg.getFaction() != customItem.getRequiredFaction()) {
                        event.setCancelled(true);
                        attacker.sendMessage(plugin.getConfigManager().getMessage("wrong-faction-weapon")
                            .replace("%faction%", customItem.getRequiredFaction().getDisplayName()));
                        return;
                    }

                    // Calculate scaled damage
                    double baseDamage = customItem.getBaseDamage();
                    double damagePerLevel = customItem.getDamagePerLevel();

                    if (attackerRpg != null) {
                        double scaledDamage = baseDamage + (damagePerLevel * attackerRpg.getLevel());
                        event.setDamage(scaledDamage);
                    }
                }
            }
        }

        // Handle mob kills for XP
        if (victim instanceof LivingEntity && !(victim instanceof Player)) {
            if (damager instanceof Player) {
                Player attacker = (Player) damager;
                RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());

                if (rpgPlayer != null) {
                    String mobType = victim.getType().name();
                    int xpAmount = plugin.getConfigManager().getMobXPValues().getOrDefault(mobType, 0);

                    if (xpAmount > 0) {
                        double xpMultiplier = plugin.getConfigManager().getConfig().getDouble("xp.multiplier", 1.0);
                        rpgPlayer.addXp(xpAmount * xpMultiplier);

                        // Show actionbar
                        attacker.sendActionBar("§a+" + (int)(xpAmount * xpMultiplier) + " RPG XP");

                        // Check quest progress
                        rpgPlayer.checkQuestProgress(Quest.QuestType.KILL_MOB, mobType, 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Handle other damage types if needed
    }
}
