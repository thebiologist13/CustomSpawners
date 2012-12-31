package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SelectCommand extends SpawnerCommand {

	public SelectCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SelectCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "none");
		
		Player player = null;
		
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		
		if(in.equals("none") || in.equals("nothing")) {
			if(player != null) {
				CustomSpawners.spawnerSelection.remove(player);
			} else {
				CustomSpawners.consoleSpawner = -1;
			}
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "You have deselected your spawner.");
			return;
		}
		
		Spawner selection = CustomSpawners.getSpawner(in);
		
		if(selection == null) {
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}
		
		if(player != null) {
			CustomSpawners.spawnerSelection.put(player, selection.getId());
		} else {
			CustomSpawners.consoleSpawner = selection.getId();
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Selected spawner " + 
				ChatColor.GOLD + PLUGIN.getFriendlyName(selection) + ChatColor.GREEN + ".");
		
	}
	
}
