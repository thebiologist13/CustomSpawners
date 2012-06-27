package com.github.thebiologist13.commands;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ListAllCommand extends SpawnerSubCommand {
    
	private CustomSpawners plugin;
	
	private Logger log;
	
	public ListAllCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		log = plugin.log;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player p = null;
		final String NO_SPAWNERS = "No spawners have been created yet.";
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p != null) {
			if(p.hasPermission("customspawners.listall")) {
				
				ArrayList<Spawner> unhiddenSpawners = new ArrayList<Spawner>();
				for(Spawner s : plugin.spawners) {
					if(!s.hidden) {
						unhiddenSpawners.add(s);
					}
				}
				
				if(p.hasPermission("customspawners.listall.hidden")) {
					if(plugin.spawners.size() == 0) {
						p.sendMessage(ChatColor.RED + NO_SPAWNERS);
					} else {
						p.sendMessage(ChatColor.GOLD + "Spawners: ");
						for(Spawner s : plugin.spawners) {
							p.sendMessage(ChatColor.DARK_AQUA + String.valueOf(s.id) + ChatColor.GREEN + " at location (" + s.loc.getBlockX() + ", " 
									+ s.loc.getBlockY() + ", " + s.loc.getBlockZ() + ")");
						}
					}
				} else {
					if(unhiddenSpawners.size() == 0) {
						p.sendMessage(ChatColor.RED + NO_SPAWNERS);
					} else {
						p.sendMessage(ChatColor.GOLD + "Spawners: ");
						for(Spawner s : unhiddenSpawners) {
							p.sendMessage(ChatColor.DARK_AQUA + String.valueOf(s.id) + ChatColor.GREEN + " at location (" + s.loc.getBlockX() + ", " 
									+ s.loc.getBlockY() + ", " + s.loc.getBlockZ() + ")");
						}
					}
				}
			}
		} else {
			if(plugin.spawners.size() == 0) {
				log.info(NO_SPAWNERS);
			} else {
				log.info("Spawners: ");
				for(Spawner s : plugin.spawners) {
					log.info(String.valueOf(s.id) + " at location (" + s.loc.getBlockX() + ", " 
							+ s.loc.getBlockY() + ", " + s.loc.getBlockZ() + ")");
				}
			}
		}
	}
}
