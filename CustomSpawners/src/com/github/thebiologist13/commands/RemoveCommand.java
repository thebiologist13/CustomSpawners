package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

public class RemoveCommand extends SpawnerSubCommand {

	private CustomSpawners plugin;
	
	public RemoveCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//ID of spawner to remove
		int removeId = -1;
		
		//Check that sender is a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Check permissions
		if(p.hasPermission("customspawners.remove")) {
			
			//If the player wants to remove the selected spawner
			if(plugin.selection.containsKey(p) && arg3.length == 1) {
				
				removeId = plugin.selection.get(p);
			
			//When the arg3 length is 1, but no spawner is selected.
			} else if(arg3.length == 1){
					
					p.sendMessage(NEEDS_SELECTION);
					return;
					
			//If the player want to remove a spawner with a specified ID
			} else if(arg3.length == 2) {
				
				//Check that the ID entered is a number
				if(!isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				removeId = Integer.parseInt(arg3[1]);
				
				//Check if the ID entered is the ID of a spawner
				if(!plugin.isValidId(removeId)) {
					p.sendMessage(NO_ID);
					return;
				}
			
			//General error message
			} else {
				
				plugin.log.info(GENERAL_ERROR);
				return;
				
			}
			
			//Remove the spawner by calling the remove() method
			plugin.getSpawnerById(removeId).remove();
			
			//Send success message
			p.sendMessage(ChatColor.GREEN + "Successfully removed spawner with ID " + ChatColor.GOLD +
					String.valueOf(removeId) + ChatColor.GREEN + "!");
		}
	}
}
