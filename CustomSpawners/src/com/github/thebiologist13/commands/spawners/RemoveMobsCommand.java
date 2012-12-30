package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public class RemoveMobsCommand extends SubCommand {

	public RemoveMobsCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Spawner
		Spawner s = null;
		
		//Cast the player
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		//Console
		if(p == null) {
			if(arg3[0].equalsIgnoreCase("removeallmobs")) {
				if(arg3.length == 1) {
					
					log.info("Removing all spawned mobs...");
					removeAllMobs();
					return;
					
				} else {
					
					log.info(GENERAL_ERROR);
					return;
					
				}
			} else if(arg3[0].equalsIgnoreCase("invadersmustdie")) {
				if(arg3.length == 1) {
					
					log.info("Removing all spawned mobs...");
					log.info("INVADERS MUST DIE!");
					removeAllMobs();
					return;
					
				} else {
					
					log.info(GENERAL_ERROR);
					return;
					
				}
			} else if(arg3[0].equalsIgnoreCase("removemobs")) {
				if(CustomSpawners.consoleSpawner != -1 && arg3.length == 1) {
					
					s = CustomSpawners.getSpawner(String.valueOf(CustomSpawners.consoleSpawner));
					
				} else if(arg3.length == 1) {
					
					plugin.sendMessage(arg0, NEEDS_SELECTION);
					return;
					
				} else if(arg3.length == 2){

					s = CustomSpawners.getSpawner(arg3[1]);

					if(s == null) {
						log.info(NO_ID);
						return;
					}
					
				} else {
					
					plugin.log.info(GENERAL_ERROR);
					return;
					
				}
				
				plugin.log.info("Removing mobs spawned by spawner with ID " + plugin.getFriendlyName(s) + "...");
				plugin.removeMobs(s);
				return;
				
			} else if(arg3[0].equalsIgnoreCase("destroythemwithlasers") || arg3[0].equalsIgnoreCase("knifeparty")) {
				
				if(CustomSpawners.consoleSpawner != -1 && arg3.length == 1) {
					
					s = CustomSpawners.getSpawner(String.valueOf(CustomSpawners.consoleSpawner));
					
				} else if(arg3.length == 1) {
					
					plugin.sendMessage(arg0, NEEDS_SELECTION);
					return;
					
				} else if(arg3.length == 2){

					s = CustomSpawners.getSpawner(arg3[1]);

					if(s == null) {
						log.info(NO_ID);
						return;
					}
					
				} else {
					
					plugin.log.info(GENERAL_ERROR);
					return;
					
				}
				
				plugin.sendMessage(arg0, "Removing mobs spawned by spawner with ID " + plugin.getFriendlyName(s) + "...");
				plugin.sendMessage(arg0, ChatColor.DARK_RED + "KNIFE PARTY!");
				plugin.removeMobs(s);
				return;
				
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
			}  else if(arg3[0].equalsIgnoreCase("invadersmustdie")) {
				
				if(p.hasPermission("customspawners.spawners.removeallmobs")) {
					
					if(arg3.length == 1) {
						
						plugin.sendMessage(p, ChatColor.GREEN + "Removing all spawned mobs...");
						plugin.sendMessage(p, ChatColor.DARK_RED + "INVADERS MUST DIE!");
						removeAllMobs();
						return;
						
					} else {
						
						log.info(GENERAL_ERROR);
						return;
						
					}
					
				}
				
			} else if(arg3[0].equalsIgnoreCase("removemobs")) {
				if(p.hasPermission("customspawners.spawners.removemobs")) {
					if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
						
						s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
						
						p.sendMessage(ChatColor.GREEN + "Removing mobs spawned by spawner with ID " + ChatColor.GOLD +
								plugin.getFriendlyName(s) + ChatColor.GREEN + "...");
						plugin.removeMobs(s);
						
						return;
						
					} else if(arg3.length == 1) {
						
						p.sendMessage(NEEDS_SELECTION);
						return;
						
					} else if(arg3.length == 2){

						s = CustomSpawners.getSpawner(arg3[1]);

						if(s == null) {
							p.sendMessage(NO_ID);
							return;
						}
						
						p.sendMessage(ChatColor.GREEN + "Removing mobs spawned by spawner with ID " + ChatColor.GOLD +
								plugin.getFriendlyName(s) + ChatColor.GREEN + "...");
						
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
			} else if(arg3[0].equalsIgnoreCase("destroythemwithlasers") || arg3[0].equalsIgnoreCase("knifeparty")) {
				if(p.hasPermission("customspawners.spawners.removemobs")) {
					if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
						
						s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
						
					} else if(arg3.length == 1) {
						
						p.sendMessage(NEEDS_SELECTION);
						
					} else if(arg3.length == 2){

						s = CustomSpawners.getSpawner(arg3[1]);

						if(s == null) {
							p.sendMessage(NO_ID);
							return;
						}
						
					} else {
						
						p.sendMessage(GENERAL_ERROR);
						
					}
					
					plugin.sendMessage(arg0, ChatColor.GREEN + "Removing mobs spawned by spawner with ID " + ChatColor.GOLD +
							plugin.getFriendlyName(s) + ChatColor.GREEN + "...");
					plugin.sendMessage(arg0, ChatColor.DARK_RED + "KNIFE PARTY!");
					plugin.removeMobs(s);
					
				} else {
					p.sendMessage(NO_PERMISSION);
					return;
				}
			}
		}
	}
	
	private void removeAllMobs() {
		for(Spawner s : CustomSpawners.spawners.values()) {
			plugin.removeMobs(s);
		}
	}
}
