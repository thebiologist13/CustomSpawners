package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;


/**
 * This command reloads the data for the plugin.
 * 
 * @author thebiologist13
 */
public class ReloadDataCommand extends CustomSpawnersCommand {

	public ReloadDataCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public ReloadDataCommand(CustomSpawners plugin, String perm) {
		super(plugin, perm);
	}

	@Override
	public void run(CommandSender sender, String subCommand, String[] args) {

		try {
			PLUGIN.getFileManager().reloadData();
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Data reloaded.");
		} catch (Exception e) {
			PLUGIN.printDebugMessage(e.getMessage(), this.getClass());
			PLUGIN.sendMessage(sender, ChatColor.RED + "Failed to reload entities.");
		}
		
	}
}
