package com.lordminato.factionrpg.command;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.gui.QuestGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {
    private final FactionRPG plugin;

    public QuestCommand(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getMessage("only-players"));
            return true;
        }

        Player player = (Player) sender;

        // Open quest GUI
        new QuestGUI(plugin, player).open();
        return true;
    }
}
