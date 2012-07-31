package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class PlayerDistanceCommand extends SpawnerCommand {

	private CustomSpawners plugin;
	
	public PlayerDistanceCommand(CustomSpawners plugin) {
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
		
		//Permissions
		if(p.hasPermission("customspawners.spawners.setmaxdistance") && arg3[0].equalsIgnoreCase("setmaxdistance")) {
			
			int maxDist = -1;
			
			//If the player wants to perform command with a selection.
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = plugin.getSpawnerById(CustomSpawners.spawnerSelection.get(p));
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				maxDist = Integer.parseInt(arg3[1]);
				
			//Arguments are for selection, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
			
			//If the player wants to perform command on a specific spawner
			} else if(arg3.length == 3) {
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				if(!plugin.isValidSpawner(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}
				
				s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
				
				if(!plugin.isInteger(arg3[2])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				maxDist = Integer.parseInt(arg3[2]);
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			if(maxDist > plugin.getCustomConfig().getDouble("spawners.playerDistanceLimit", 128) || maxDist < 0) {
				if(!p.hasPermission("customspawners.spawners.limitoverride")) {
					p.sendMessage(INVALID_VALUES);
					return;
				}
			}
			
			//Set the max player distance
			s.setMaxPlayerDistance(maxDist);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the maximum player distance of spawner with ID " + ChatColor.GOLD +
					String.valueOf(s.getId()) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(maxDist) + 
					ChatColor.GREEN + "!");
		} else if(p.hasPermission("customspawners.spawners.setmindistance") && arg3[0].equalsIgnoreCase("setmindistance")) {

			int minDist = -1;
			
			//If the player wants to perform command with a selection.
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = plugin.getSpawnerById(CustomSpawners.spawnerSelection.get(p));
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				minDist = Integer.parseInt(arg3[1]);
				
			//Arguments are for selection, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
			
			//If the player wants to perform command on a specific spawner
			} else if(arg3.length == 3) {
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				if(!plugin.isValidSpawner(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}
				
				s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
				
				if(!plugin.isInteger(arg3[2])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				minDist = Integer.parseInt(arg3[2]);
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			if(minDist > plugin.getCustomConfig().getDouble("spawners.playerDistanceLimit", 128) || minDist < 0) {
				if(!p.hasPermission("customspawners.limitoverride")) {
					p.sendMessage(INVALID_VALUES);
					return;
				} else {
					p.sendMessage(NO_PERMISSION);
					return;
				}
			}
			
			//Set the min player distance
			s.setMinPlayerDistance(minDist);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the minimum player distance of spawner with ID " + ChatColor.GOLD +
					String.valueOf(s.getId()) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(minDist) + 
					ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
