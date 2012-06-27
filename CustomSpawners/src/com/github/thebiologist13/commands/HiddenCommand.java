package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class HiddenCommand extends SpawnerSubCommand {

	private CustomSpawners plugin;
	
	public HiddenCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission("customspawners.sethidden") && arg3[0].equalsIgnoreCase("sethidden")) {
			//Set hidden for selection
			if(plugin.selection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawnerById(plugin.selection.get(p));
				
			//Arguments entered for selection, but there is none
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//Set hidden of specific spawner
			} else if(arg3.length == 2) {
				
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
			
			//Set the value
			s.hidden = true;
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + String.valueOf(s.id) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "hidden" + ChatColor.GREEN + "!");
		}
		
		if(p.hasPermission("customspawners.setunhidden") && arg3[0].equalsIgnoreCase("setunhidden")) {
			//Set unhidden for selection
			if(plugin.selection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawnerById(plugin.selection.get(p));
				
			//Arguments entered for selection, but there is none
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//Set unhidden of specific spawner
			} else if(arg3.length == 2) {
				
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
			
			//Set the value
			s.hidden = false;
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + String.valueOf(s.id) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "unhidden" + ChatColor.GREEN + "!");
		}
	}

}
