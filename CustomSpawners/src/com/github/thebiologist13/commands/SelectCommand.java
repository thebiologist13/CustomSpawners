package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

public class SelectCommand extends SpawnerSubCommand {

	private CustomSpawners plugin;
	
	public SelectCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//ID to select
		int selectionId = -1;
		
		//Make sure a player issued command
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		} 
		
		p = (Player) arg0;
		
		//Permission check
		if(p.hasPermission("customspawners.select")) {
			//If the entered value is not a number
			if(!isInteger(arg3[1])) {
				p.sendMessage(ID_NOT_NUMBER);
				return;
			}
			
			//Assign selectionId if a number
			selectionId = Integer.parseInt(arg3[1]);
			
			//If selectionId isn't the ID of any spawners
			if(!plugin.isValidId(selectionId)) {
				p.sendMessage(NO_ID);
				return;
			} 
			
			//Put selectionId as Player's selected spawner
			plugin.selection.put(p, selectionId);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "You have selected spawner " + ChatColor.GOLD + 
					selectionId + ChatColor.GREEN + ".");
		}
	}
}
