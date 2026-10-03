package com.lordminato.factionrpg.command;

import com.lordminato.factionrpg.FactionRPG;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminTabCompleter implements TabCompleter {
    private final FactionRPG plugin;

    public AdminTabCompleter(FactionRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // First argument - subcommands
            completions.add("reload");
            completions.add("give");
            completions.add("setlevel");
            completions.add("addxp");
            completions.add("setxp");
            completions.add("addfactionxp");
            completions.add("setfaction");
            completions.add("resetfaction");
            completions.add("resetquest");
            completions.add("info");
        } else if (args.length == 2) {
            // Second argument - depends on subcommand
            switch (args[0].toLowerCase()) {
                case "give":
                case "setlevel":
                case "addxp":
                case "setxp":
                case "addfactionxp":
                case "setfaction":
                case "resetfaction":
                case "resetquest":
                case "info":
                    // Player names
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        completions.add(player.getName());
                    }
                    break;
                default:
                    break;
            }
        } else if (args.length == 3) {
            // Third argument - depends on subcommand
            switch (args[0].toLowerCase()) {
                case "give":
                    // Custom item IDs
                    completions.addAll(plugin.getItemManager().getCustomItems().keySet());
                    break;
                case "setlevel":
                case "addxp":
                case "setxp":
                case "addfactionxp":
                    // Numbers
                    completions.add("<amount>");
                    break;
                case "setfaction":
                    // Faction options
                    completions.add("LIGHT");
                    completions.add("SHADOW");
                    break;
                default:
                    break;
            }
        }

        return completions;
    }
}
