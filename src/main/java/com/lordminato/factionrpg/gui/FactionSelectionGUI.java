package com.lordminato.factionrpg.gui;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FactionSelectionGUI implements Listener {
    private final FactionRPG plugin;
    private final Player player;
    private final Inventory inventory;

    public FactionSelectionGUI(FactionRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 27, "Choose Your Faction");

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Create GUI items
        createFactionItems();
    }

    private void createFactionItems() {
        // Light Vanguard item
        ItemStack lightItem = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta lightMeta = lightItem.getItemMeta();
        if (lightMeta != null) {
            lightMeta.setDisplayName("§6✨ LIGHT VANGUARD");
            lightMeta.setLore(java.util.Arrays.asList(
                "",
                "§7Guardians of light and order.",
                "",
                "§eAbility: Holy Shield",
                "§eStyle: Defense / Sustain",
                "",
                "§a[ JOIN LIGHT ]"
            ));
            lightItem.setItemMeta(lightMeta);
        }
        inventory.setItem(11, lightItem);

        // Shadow Syndicate item
        ItemStack shadowItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta shadowMeta = shadowItem.getItemMeta();
        if (shadowMeta != null) {
            shadowMeta.setDisplayName("§5☾ SHADOW SYNDICATE");
            shadowMeta.setLore(java.util.Arrays.asList(
                "",
                "§7Masters of darkness and speed.",
                "",
                "§eAbility: Shadow Dash",
                "§eStyle: Mobility / Burst",
                "",
                "§a[ JOIN SHADOW ]"
            ));
            shadowItem.setItemMeta(shadowMeta);
        }
        inventory.setItem(15, shadowItem);

        // Filler items
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
    }

    public void open() {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(inventory)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player clicker = (Player) event.getWhoClicked();
        if (!clicker.equals(player)) {
            return;
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }

        if (clicked.getItemMeta() != null) {
            String displayName = clicked.getItemMeta().getDisplayName();
            if (displayName != null) {
                if (displayName.contains("LIGHT VANGUARD")) {
                    setFaction(Faction.LIGHT_VANGUARD);
                } else if (displayName.contains("SHADOW SYNDICATE")) {
                    setFaction(Faction.SHADOW_SYNDICATE);
                }
            }
        }
    }

    private void setFaction(Faction faction) {
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (rpgPlayer == null) {
            return;
        }

        rpgPlayer.setFaction(faction);
        plugin.getDatabaseManager().updatePlayer(rpgPlayer);

        // Close inventory
        player.closeInventory();

        // Send welcome message
        String welcomeMessage = plugin.getConfigManager().getMessage("faction-selected")
            .replace("%faction%", faction.getColor() + faction.getDisplayName());
        player.sendMessage(welcomeMessage);

        // Assign first quest
        plugin.getQuestManager().assignNewQuest(rpgPlayer);

        // Give starter item if configured
        if (plugin.getConfigManager().getConfig().getBoolean("factions.give-starter-item", false)) {
            String itemId = faction == Faction.LIGHT_VANGUARD ?
                "factionrpg_vanguard_blade" : "factionrpg_shadow_blade";
            CustomItem starterItem = plugin.getItemManager().getCustomItem(itemId);
            if (starterItem != null) {
                player.getInventory().addItem(starterItem.createItemStack());
            }
        }
    }
}
