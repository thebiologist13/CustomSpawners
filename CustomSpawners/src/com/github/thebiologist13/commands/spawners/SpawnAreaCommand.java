package com.github.thebiologist13.commands.spawners;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class SpawnAreaCommand extends SpawnerCommand {
	
	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public SpawnAreaCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Use area?
		boolean useSpawnArea = false;
		//Spawner
		Spawner s = null;
		//Perms
		String perm = "customspawners.spawners.setspawnarea";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;
		
		if(p.hasPermission(perm)) {
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = plugin.getSpawnerById(CustomSpawners.spawnerSelection.get(p));
				
				String value = arg3[1];
				
				if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					if(value.equals("true")) {
						useSpawnArea = true;
					}
					//Doesn't need one for false, already set above
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
					return;
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
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
				
				String value = arg3[2];
				
				if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					if(value.equals("true")) {
						useSpawnArea = true;
					}
					//Doesn't need one for false, already set above
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
			}
			
			if(CustomSpawners.selectionPointOne.containsKey(p) && 
					CustomSpawners.selectionPointTwo.containsKey(p)) {
				
				Location[] areaPoints = new Location[2];
				
				areaPoints[0] = CustomSpawners.selectionPointOne.get(p);
				areaPoints[1] = CustomSpawners.selectionPointTwo.get(p);
				
				//Set
				s.setAreaPoints(areaPoints);
				s.setUseSpawnArea(useSpawnArea);
				
				//Success Message
				p.sendMessage(ChatColor.GREEN + "Successfully set the spawn area value of spawner with ID " + 
						ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " to " + ChatColor.GOLD +
						String.valueOf(useSpawnArea) + ChatColor.GREEN + "!");
				
			} else {
				p.sendMessage(ChatColor.RED + "You must have an area selected for a spawner to use as the spawn area.");
				return;
			}
			
		} else {
			p.sendMessage(NO_PERMISSION);
		}
		
	}

}
