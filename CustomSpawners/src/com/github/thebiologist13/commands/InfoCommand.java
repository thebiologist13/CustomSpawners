package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class InfoCommand extends SpawnerSubCommand {

	private CustomSpawners plugin;
	
	public InfoCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		//Make sure a player issued command
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		} 
		
		p = (Player) arg0;
		
		//Permission check
		if(p.hasPermission("customspawners.info")) {
			
			//If the player wants to perform command with a selection.
			if(plugin.selection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawnerById(plugin.selection.get(p));
				
			//Arguments are for selection, but none is selected
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
			
			//If the player wants to perform command on a specific spawner
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
			
			//Send info
			if(!p.hasPermission("customspawners.info.hidden") && s.hidden == true) {
				p.sendMessage(ChatColor.RED + "You are not allowed to view info on that spawner!");
				return;
			} else {
				String[] message = {
						"",
						ChatColor.GREEN + "Information on spawner with ID " + ChatColor.GOLD + String.valueOf(s.id) + ChatColor.GREEN + ": ",
						"",
						ChatColor.GOLD + "Active: " + String.valueOf(s.active),
						ChatColor.GOLD + "Hidden: " + String.valueOf(s.hidden),
						ChatColor.GOLD + "Type: " + String.valueOf(s.type.getName()),
						ChatColor.GOLD + "Location: " + "(" + s.loc.getBlockX() + ", " + s.loc.getBlockY() + ", " + s.loc.getBlockZ() + ")",
						ChatColor.GOLD + "Spawn Rate: " + String.valueOf(s.mobsPerSpawn) + " per " + String.valueOf(s.rate) + " ticks",
						ChatColor.GOLD + "Spawn Radius: " + String.valueOf(s.radius),
						ChatColor.GOLD + "Maximum Mobs: " + String.valueOf(s.maxMobs),
						ChatColor.GOLD + "Maximum Light: " + String.valueOf(s.maxLightLevel),
						ChatColor.GOLD + "Minimum Light: " + String.valueOf(s.minLightLevel),
						ChatColor.GOLD + "Maximum Distance: " + String.valueOf(s.maxPlayerDistance),
						ChatColor.GOLD + "Minimum Distance: " + String.valueOf(s.minPlayerDistance),
						ChatColor.GOLD + "Redstone Triggered: " + String.valueOf(s.redstoneTriggered)
						};
				
				p.sendMessage(message);
			}
		}
	}
}
