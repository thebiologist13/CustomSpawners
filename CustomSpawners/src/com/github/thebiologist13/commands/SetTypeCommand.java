package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SetTypeCommand extends SpawnerSubCommand {
	
	private CustomSpawners plugin;
	
	public SetTypeCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//New entity type
		EntityType type = null;
		
		//Spawner
		Spawner s = null;
		
		//Make sure it is a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission("customspawners.settype")) {
			//If they want to set the spawn type of a selected spawner
			if(plugin.selection.containsKey(p) && arg3.length == 2) {
				
				type = getEntityType(arg3[1], plugin, p);
				
				s = plugin.getSpawnerById(plugin.selection.get(p));
				
			//Argument length is for a selected spawner, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//If they want to set spawn type by ID
			} else if(arg3.length == 3) {
			
				type = getEntityType(arg3[2], plugin, p);
				
				if(!isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				if(!plugin.isValidId(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}
				
				s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			//Set the new type
			s.type = type;
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Successfully changed entity type of spawner with ID " + 
					ChatColor.GOLD + String.valueOf(s.id) + ChatColor.GREEN + " to " + ChatColor.GOLD +
					s.type.getName() + ChatColor.GREEN + "!");
		}
	}
}
