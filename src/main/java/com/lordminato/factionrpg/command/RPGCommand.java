package com.lordminato.factionrpg.command;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.player.RPGPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RPGCommand implements CommandExecutor {
    private final FactionRPG plugin;

    public RPGCommand(FactionRPG plugin) {
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

        // Display RPG profile
        String faction = rpgPlayer.getFaction().getColor() + rpgPlayer.getFaction().getDisplayName();
        String level = String.valueOf(rpgPlayer.getLevel());
        String xp = String.valueOf((int) rpgPlayer.getXp());
        String requiredXp = String.valueOf((int) rpgPlayer.getRequiredXpForNextLevel());
        String factionXp = String.valueOf((int) rpgPlayer.getFactionXp());
        String ability = rpgPlayer.getFaction() == null ? "None" :
            plugin.getFactionManager().getAbility(rpgPlayer.getFaction()).getName();

        String message = plugin.getConfigManager().getMessage("rpg-profile")
            .replace("%faction%", faction)
            .replace("%level%", level)
            .replace("%xp%", xp)
            .replace("%required_xp%", requiredXp)
            .replace("%faction_xp%", factionXp)
            .replace("%ability%", ability);

        player.sendMessage(message);
        return true;
    }
}
