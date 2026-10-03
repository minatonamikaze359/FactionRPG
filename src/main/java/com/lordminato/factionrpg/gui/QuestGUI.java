package com.lordminato.factionrpg.gui;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.player.RPGPlayer;
import com.lordminato.factionrpg.quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QuestGUI implements Listener {
    private final FactionRPG plugin;
    private final Player player;
    private final Inventory inventory;

    public QuestGUI(FactionRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 27, "Daily Quest");

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Create GUI items
        createQuestItems();
    }

    private void createQuestItems() {
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (rpgPlayer == null || rpgPlayer.getCurrentQuest() == null) {
            // No quest available
            ItemStack noQuest = new ItemStack(Material.BARRIER);
            ItemMeta noQuestMeta = noQuest.getItemMeta();
            if (noQuestMeta != null) {
                noQuestMeta.setDisplayName("§cNo Quest Available");
                noQuestMeta.setLore(java.util.Arrays.asList(
                    "",
                    "§7You don't have a daily quest right now."
                ));
                noQuest.setItemMeta(noQuestMeta);
            }
            inventory.setItem(13, noQuest);
            return;
        }

        Quest quest = rpgPlayer.getCurrentQuest();
        Material questMaterial = getMaterialForQuestType(quest.getType());

        ItemStack questItem = new ItemStack(questMaterial);
        ItemMeta questMeta = questItem.getItemMeta();
        if (questMeta != null) {
            questMeta.setDisplayName(quest.getDisplayName());
            questMeta.setLore(java.util.Arrays.asList(
                "",
                "§7" + getQuestDescription(quest),
                "",
                "§eProgress: " + rpgPlayer.getQuestProgress() + " / " + quest.getAmount(),
                "§a" + getProgressBar(rpgPlayer.getQuestProgress(), quest.getAmount()),
                "",
                "§6Rewards:",
                "§7+ " + (int)quest.getXpReward() + " RPG XP",
                "§7+ " + (int)quest.getFactionXpReward() + " Faction XP",
                "§7+ $" + (int)quest.getMoneyReward(),
                "",
                rpgPlayer.isQuestCompleted() ? "§a✔ COMPLETED" : "§e✦ IN PROGRESS"
            ));
            questItem.setItemMeta(questMeta);
        }
        inventory.setItem(13, questItem);

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

    private Material getMaterialForQuestType(Quest.QuestType type) {
        switch (type) {
            case KILL_MOB:
                return Material.ZOMBIE_HEAD;
            case BREAK_BLOCK:
                return Material.DIAMOND_PICKAXE;
            case CRAFT_ITEM:
                return Material.CRAFTING_TABLE;
            case PLACE_BLOCK:
                return Material.GRASS_BLOCK;
            default:
                return Material.PAPER;
        }
    }

    private String getQuestDescription(Quest quest) {
        switch (quest.getType()) {
            case KILL_MOB:
                return "Kill " + quest.getAmount() + " " + quest.getTarget() + "s";
            case BREAK_BLOCK:
                return "Mine " + quest.getAmount() + " " + quest.getTarget();
            case CRAFT_ITEM:
                return "Craft " + quest.getAmount() + " " + quest.getTarget();
            case PLACE_BLOCK:
                return "Place " + quest.getAmount() + " " + quest.getTarget();
            default:
                return "Complete the task";
        }
    }

    private String getProgressBar(int progress, int required) {
        int totalBars = 20;
        int filledBars = (int) ((double) progress / required * totalBars);
        filledBars = Math.min(filledBars, totalBars);

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }

        return bar.toString();
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
    }
}
