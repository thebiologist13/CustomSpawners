package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class PerSpawnCommand extends SpawnerCommand {

	private CustomSpawners plugin;
	
	public PerSpawnCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		//Mobs per spawn
		int mobsPerSpawn = -1;
		
		//Check to make sure the command is from a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Permission checking
		if(p.hasPermission("customspawners.spawners.setmobsperspawn")) {
			
			//If the player wants to set the mobs per spawn of a selected spawner
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = plugin.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				mobsPerSpawn = Integer.parseInt(arg3[1]);
				
			//If no spawner is selected, but arguments for a selected spawner were put in.
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				
			//If the player wants to set the mobs per spawn of a spawner by ID
			} else if(arg3.length == 3) {

				s = plugin.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(!plugin.isInteger(arg3[2])) {
					p.sendMessage(SPECIFY_NUMBER);
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
					p.sendMessage(INVALID_VALUES);
					return;
				}
			}
			
			//Set the new max mobs
			s.setMobsPerSpawn(mobsPerSpawn);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the mobs per spawn of the spawner with ID " + ChatColor.GOLD +
					plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(mobsPerSpawn) + 
					ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
