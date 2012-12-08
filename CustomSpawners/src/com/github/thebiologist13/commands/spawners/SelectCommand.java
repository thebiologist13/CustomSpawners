package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class SelectCommand extends SpawnerCommand {

	public SelectCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Spawner to select
		Spawner s = null;
		//ID to select
		int selectionId = -1;
		
		final String deselect = ChatColor.GREEN + "You have deselected your spawner.";
		
		//Make sure a player issued command
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		} 
		
		if(p == null) {

			if(arg3[1].equalsIgnoreCase("NONE")) {
				CustomSpawners.consoleSpawner = -1;
				plugin.sendMessage(arg0, deselect);
				return;
			}
			
			//Assign selectionId if a number
			s = CustomSpawners.getSpawner(arg3[1]);
			
			if(s == null) {
				plugin.sendMessage(arg0, NO_ID);
				return;
			}
			
			selectionId = s.getId();
			
			//Put selectionId as selected entity
			CustomSpawners.consoleSpawner = selectionId;
			
			//Success message
			plugin.sendMessage(arg0, ChatColor.GREEN + "You have selected spawner " + ChatColor.GOLD + 
					plugin.getFriendlyName(s) + ChatColor.GREEN + ".");
			
		} else {

			//Permission check
			if(p.hasPermission("customspawners.spawners.select")) {
				
				if(arg3[1].equalsIgnoreCase("NONE")) {
					CustomSpawners.spawnerSelection.remove(p);
					plugin.sendMessage(p, deselect);
					return;
				}
				
				//Assign selectionId if a number
				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					plugin.sendMessage(p, NO_ID);
					return;
				}
				
				selectionId = s.getId();
				
				//Put selectionId as Player's selected spawner
				CustomSpawners.spawnerSelection.put(p, selectionId);
				
				//Success message
				plugin.sendMessage(p, ChatColor.GREEN + "You have selected spawner " + ChatColor.GOLD + 
						plugin.getFriendlyName(s) + ChatColor.GREEN + ".");
			} else {
				plugin.sendMessage(p, NO_PERMISSION);
				return;
			}
			
		}
		
	}
	
}
