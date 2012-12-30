package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public class PerSpawnCommand extends SubCommand {

	public PerSpawnCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		//Mobs per spawn
		int mobsPerSpawn = 1;
		
		//Check to make sure the command is from a player
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {

			//If the player wants to set the mobs per spawn of a selected spawner
			if(CustomSpawners.consoleSpawner != -1 && arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(String.valueOf(CustomSpawners.consoleSpawner));
				
				if(!CustomSpawners.isInteger(arg3[1])) {
					plugin.sendMessage(arg0, SPECIFY_NUMBER);
					return;
				}
				
				mobsPerSpawn = Integer.parseInt(arg3[1]);
				
			//If no spawner is selected, but arguments for a selected spawner were put in.
			} else if(arg3.length == 2) {
				
				plugin.sendMessage(arg0, NEEDS_SELECTION);
				
			//If the player wants to set the mobs per spawn of a spawner by ID
			} else if(arg3.length == 3) {

				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					plugin.sendMessage(arg0, NO_ID);
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[2])) {
					plugin.sendMessage(arg0, SPECIFY_NUMBER);
					return;
				}
				
				mobsPerSpawn = Integer.parseInt(arg3[2]);
			
			//General error message
			} else {
				
				plugin.sendMessage(arg0, GENERAL_ERROR);
				return;
				
			}
			
			//Set the new max mobs
			s.setMobsPerSpawn(mobsPerSpawn);
			
			//Success message
			plugin.sendMessage(arg0, ChatColor.GREEN + "Set the mobs per spawn of the spawner with ID " + ChatColor.GOLD +
					plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(mobsPerSpawn) + 
					ChatColor.GREEN + "!");
			
		} else {

			//Permission checking
			if(p.hasPermission("customspawners.spawners.setmobsperspawn")) {
				
				//If the player wants to set the mobs per spawn of a selected spawner
				if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
					
					s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
					
					if(!CustomSpawners.isInteger(arg3[1])) {
						plugin.sendMessage(p, SPECIFY_NUMBER);
						return;
					}
					
					mobsPerSpawn = Integer.parseInt(arg3[1]);
					
				//If no spawner is selected, but arguments for a selected spawner were put in.
				} else if(arg3.length == 2) {
					
					plugin.sendMessage(p, NEEDS_SELECTION);
					
				//If the player wants to set the mobs per spawn of a spawner by ID
				} else if(arg3.length == 3) {

					s = CustomSpawners.getSpawner(arg3[1]);

					if(s == null) {
						plugin.sendMessage(p, NO_ID);
						return;
					}
					
					if(!CustomSpawners.isInteger(arg3[2])) {
						plugin.sendMessage(p, SPECIFY_NUMBER);
						return;
					}
					
					mobsPerSpawn = Integer.parseInt(arg3[2]);
				
				//General error message
				} else {
					
					plugin.log.info(GENERAL_ERROR);
					return;
					
				}
				
				if(mobsPerSpawn > plugin.getCustomConfig().getDouble("spawners.mobsPerSpawnLimit", 128) || mobsPerSpawn < 0) {
					if(!p.hasPermission("customspawners.limitoverride")) {
						plugin.sendMessage(p, INVALID_VALUES);
						return;
					}
				}
				
				//Set the new max mobs
				s.setMobsPerSpawn(mobsPerSpawn);
				
				//Success message
				plugin.sendMessage(p, ChatColor.GREEN + "Set the mobs per spawn of the spawner with ID " + ChatColor.GOLD +
						plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(mobsPerSpawn) + 
						ChatColor.GREEN + "!");
			} else {
				plugin.sendMessage(p, NO_PERMISSION);
				return;
			}
			
		}
		
	}
	
}
