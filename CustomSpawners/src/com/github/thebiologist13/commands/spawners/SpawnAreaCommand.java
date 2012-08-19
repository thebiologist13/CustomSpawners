package com.github.thebiologist13.commands.spawners;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
				
				s = plugin.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
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

				s = plugin.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
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
				
				World p1World = CustomSpawners.selectionPointOne.get(p).getWorld();
				World p2World = CustomSpawners.selectionPointTwo.get(p).getWorld();
				
				if(!p1World.equals(p2World)) {
					p.sendMessage(ChatColor.RED + "Spawn area selection points must be in the same world.");
					return;
				}
				
				Location[] areaPoints = new Location[2];
				
				areaPoints[0] = CustomSpawners.selectionPointOne.get(p);
				areaPoints[1] = CustomSpawners.selectionPointTwo.get(p);
				
				//Set
				s.setAreaPoints(areaPoints);
				s.setUseSpawnArea(useSpawnArea);
				
				//Success Message
				p.sendMessage(ChatColor.GREEN + "Successfully set the spawn area value of spawner with ID " + 
						ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD +
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
