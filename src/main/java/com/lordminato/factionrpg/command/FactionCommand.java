package com.lordminato.factionrpg.command;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionCommand implements CommandExecutor {
    private final FactionRPG plugin;

    public FactionCommand(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getMessage("only-players"));
            return true;
        }

        Player player = (Player) sender;
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (rpgPlayer == null) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-profile"));
            return true;
        }

        if (args.length == 0) {
            // Default to /faction info
            showFactionInfo(player, rpgPlayer);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "info":
                showFactionInfo(player, rpgPlayer);
                break;
            case "members":
                showFactionMembers(player, rpgPlayer);
                break;
            case "ability":
                showFactionAbility(player, rpgPlayer);
                break;
            default:
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-command"));
                break;
        }

        return true;
    }

    private void showFactionInfo(Player player, RPGPlayer rpgPlayer) {
        String faction = rpgPlayer.getFaction().getColor() + rpgPlayer.getFaction().getDisplayName();
        String message = plugin.getConfigManager().getMessage("faction-info")
            .replace("%faction%", faction);

        player.sendMessage(message);
    }

    private void showFactionMembers(Player player, RPGPlayer rpgPlayer) {
        // In a real implementation, you would track online faction members
        // This is a simplified version
        String faction = rpgPlayer.getFaction().getDisplayName();
        String message = plugin.getConfigManager().getMessage("faction-members")
            .replace("%faction%", faction);

        player.sendMessage(message);
    }

    private void showFactionAbility(Player player, RPGPlayer rpgPlayer) {
        String abilityName = plugin.getFactionManager().getAbility(rpgPlayer.getFaction()).getName();
        String abilityDesc = plugin.getFactionManager().getAbility(rpgPlayer.getFaction()).getDescription();
        String cooldown = String.valueOf(plugin.getFactionManager().getAbility(rpgPlayer.getFaction()).getCooldown());

        String message = plugin.getConfigManager().getMessage("faction-ability")
            .replace("%ability%", abilityName)
            .replace("%description%", abilityDesc)
            .replace("%cooldown%", cooldown);

        player.sendMessage(message);
    }
}
