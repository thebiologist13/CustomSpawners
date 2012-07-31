package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class RemoveMobsCommand extends SpawnerCommand {
	
	private CustomSpawners plugin;
	
	public RemoveMobsCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Cast the player
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		//Console
		if(p == null) {
			if(arg3[0].equalsIgnoreCase("removeallmobs")) {
				if(arg3.length == 1) {
					
					plugin.log.info("Removing all spawned mobs...");
					removeAllMobs();
					return;
					
				} else {
					
					plugin.log.info("An error has occured with this command. Did you type everything right?");
					return;
					
				}
			} else if(arg3[0].equalsIgnoreCase("removemobs")) {
				if(arg3.length == 2){
					
					if(!plugin.isInteger(arg3[1])) {
						plugin.log.info("IDs must be a number.");
						return;
					}
					
					if(!plugin.isValidSpawner(Integer.parseInt(arg3[1]))) {
						plugin.log.info("Spawner ID does not exist.");
						return;
					}
					
					Spawner s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
					plugin.log.info("Removing mobs spawned by spawner with ID " + String.valueOf(s.getId()) + "...");
					plugin.removeMobs(s);
					
					return;
					
				} else {
					
					plugin.log.info("An error has occured with this command. Did you type everything right?");
					return;
					
				}
			}
			
		//Player
		} else {
			if(arg3[0].equalsIgnoreCase("removeallmobs")) {
				if(p.hasPermission("customspawners.spawners.removeallmobs")) {
					if(arg3.length == 1) {
						
						p.sendMessage(ChatColor.GREEN + "Removing all spawned mobs...");
						removeAllMobs();
						return;
						
					} else {
						
						p.sendMessage(GENERAL_ERROR);
						return;
						
					}
				} else {
					p.sendMessage(NO_PERMISSION);
					return;
				}
			} else if(arg3[0].equalsIgnoreCase("removemobs")) {
				if(p.hasPermission("customspawners.spawners.removemobs")) {
					if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
						
						Spawner s = plugin.getSpawnerById(CustomSpawners.spawnerSelection.get(p));
						p.sendMessage(ChatColor.GREEN + "Removing mobs spawned by spawner with ID " + ChatColor.GOLD +
								String.valueOf(s.getId()) + ChatColor.GREEN + "...");
						plugin.removeMobs(s);
						
						return;
						
					} else if(arg3.length == 1) {
						
						p.sendMessage(NEEDS_SELECTION);
						return;
						
					} else if(arg3.length == 2){
						
						if(!plugin.isInteger(arg3[1])) {
							p.sendMessage(ID_NOT_NUMBER);
							return;
						}
						
						if(!plugin.isValidSpawner(Integer.parseInt(arg3[1]))) {
							p.sendMessage(NO_ID);
							return;
						}
						
						Spawner s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
						p.sendMessage(ChatColor.GREEN + "Removing mobs spawned by spawner with ID " + ChatColor.GOLD +
								String.valueOf(s.getId()) + ChatColor.GREEN + "...");
						
						plugin.removeMobs(s);
						
						return;
						
					} else {
						
						p.sendMessage(GENERAL_ERROR);
						return;
						
					}
				} else {
					p.sendMessage(NO_PERMISSION);
					return;
				}
			}
		}
	}
	
	private void removeAllMobs() {
		for(Spawner s : CustomSpawners.spawners) {
			plugin.removeMobs(s);
		}
	}
}
