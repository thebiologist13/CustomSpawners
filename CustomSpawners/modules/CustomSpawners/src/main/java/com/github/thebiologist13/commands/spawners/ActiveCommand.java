package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ActiveCommand extends SpawnerCommand {

	public ActiveCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public ActiveCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("setactive")) {
			
			spawner.setActive(true);
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "active" + ChatColor.GREEN + "!");
			
		} else if(subCommand.equals("setinactive")) {
			
			spawner.setActive(false);
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "inactive" + ChatColor.GREEN + "!");
			
		}
		
	}
	
}
