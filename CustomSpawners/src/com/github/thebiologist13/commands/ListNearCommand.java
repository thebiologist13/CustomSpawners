package com.github.thebiologist13.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ListNearCommand extends SpawnerSubCommand {
	
	private CustomSpawners plugin;
	
	public ListNearCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player p = null;
		
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission("customspawners.listnear")) {
			ArrayList<Spawner> nearbySpawners = new ArrayList<Spawner>();
			for(Spawner s : plugin.spawners) {
				if(!p.hasPermission("customspawners.listnear.hidden")) {
					if(s.loc.distance(p.getLocation()) < 25 && !s.hidden) { //TODO add config option for this nearby distance (the 25)
						 nearbySpawners.add(s);
					}
				} else {
					if(s.loc.distance(p.getLocation()) < 25) { //TODO add config option for this nearby distance (the 25)
						 nearbySpawners.add(s);
					}
				}
			}
			
			if(nearbySpawners.size() == 0) {
				p.sendMessage(ChatColor.GREEN + "There are no spawners within " + String.valueOf(25) + " blocks."); //TODO here too.
				return;
			} else {
				p.sendMessage(ChatColor.GOLD + "Nearby Spawners: ");
				for(Spawner s : nearbySpawners) {
					p.sendMessage(ChatColor.DARK_AQUA + String.valueOf(s.id) + ChatColor.GREEN + " at location (" + s.loc.getBlockX() + ", " 
							+ s.loc.getBlockY() + ", " + s.loc.getBlockZ() + ")");
				}
			}
		}
	}
}
