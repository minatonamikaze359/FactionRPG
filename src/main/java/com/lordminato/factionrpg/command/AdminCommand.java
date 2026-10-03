package com.lordminato.factionrpg.command;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import com.lordminato.factionrpg.item.CustomItem;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    private final FactionRPG plugin;

    public AdminCommand(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("factionrpg.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("factionrpg.admin.reload")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                plugin.getConfigManager().reloadConfigs();
                plugin.getQuestManager().reloadQuests();
                plugin.getItemManager().reloadItems();
                sender.sendMessage(plugin.getConfigManager().getMessage("config-reloaded"));
                break;
            case "give":
                if (!sender.hasPermission("factionrpg.admin.give")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                handleGiveCommand(sender, args);
                break;
            case "setlevel":
                if (!sender.hasPermission("factionrpg.admin.level")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                handleSetLevelCommand(sender, args);
                break;
            case "addxp":
                if (!sender.hasPermission("factionrpg.admin.level")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                handleAddXpCommand(sender, args);
                break;
            case "setfaction":
                if (!sender.hasPermission("factionrpg.admin.faction")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                handleSetFactionCommand(sender, args);
                break;
            case "resetfaction":
                if (!sender.hasPermission("factionrpg.admin.faction")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                handleResetFactionCommand(sender, args);
                break;
            case "resetquest":
                if (!sender.hasPermission("factionrpg.admin.quest")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                handleResetQuestCommand(sender, args);
                break;
            case "info":
                handleInfoCommand(sender, args);
                break;
            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6§lFactionRPG Admin Commands:");
        sender.sendMessage("§e/frpg reload §7- Reload configurations");
        sender.sendMessage("§e/frpg give <player> <item> §7- Give custom item");
        sender.sendMessage("§e/frpg setlevel <player> <level> §7- Set RPG level");
        sender.sendMessage("§e/frpg addxp <player> <amount> §7- Add XP");
        sender.sendMessage("§e/frpg setfaction <player> <LIGHT|SHADOW> §7- Set faction");
        sender.sendMessage("§e/frpg resetfaction <player> §7- Reset faction");
        sender.sendMessage("§e/frpg resetquest <player> §7- Reset daily quest");
        sender.sendMessage("§e/frpg info <player> §7- View player info");
    }

    private void handleGiveCommand(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /frpg give <player> <item>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getConfigManager().getMessage("player-not-found"));
            return;
        }

        CustomItem item = plugin.getItemManager().getCustomItem(args[2]);
        if (item == null) {
            sender.sendMessage(plugin.getConfigManager().getMessage("item-not-found"));
            return;
        }

        target.getInventory().addItem(item.createItemStack());
        sender.sendMessage(plugin.getConfigManager().getMessage("item-given")
            .replace("%player%", target.getName())
            .replace("%item%", item.getDisplayName()));
    }

    private void handleSetLevelCommand(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /frpg setlevel <player> <level>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getConfigManager().getMessage("invalid-number"));
            return;
        }

        RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(target.getUniqueId());
        if (rpgPlayer == null) {
            sender.sendMessage(plugin.getConfigManager().getMessage("player-not-found"));
            return;
        }

        int maxLevel = plugin.getConfigManager().getMaxLevel();
        if (level < 1) {
            level = 1;
        } else if (level > maxLevel) {
            level = maxLevel;
        }

        rpgPlayer.setLevel(level);
        plugin.getDatabaseManager().updatePlayer(rpgPlayer);

        sender.sendMessage(plugin.getConfigManager().getMessage("level-set")
            .replace("%player%", target.getName())
            .replace("%level%", String.valueOf(level)));
    }

    // Implement other command handlers similarly
}
