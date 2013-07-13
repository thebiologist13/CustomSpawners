package com.github.thebiologist13.commands.spawners;

import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ListAllCommand extends SpawnerCommand {

	public ListAllCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public ListAllCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		int[] ids = new int[CustomSpawners.spawners.size()];
		int current = 0;
		
		if(ids.length == 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "No spawners have been created yet.");
			return;
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GOLD + "Created Spawners:");
		
		Iterator<Spawner> itr = CustomSpawners.spawners.values().iterator();
		while(itr.hasNext()) {
			int id = itr.next().getId();
			ids[current] = id;
			current++;
		}

		Arrays.sort(ids);
			
		for(int i : ids) {
			Spawner sp = CustomSpawners.getSpawner(i);
			
			Location loc = sp.getLoc();
			
			String baseMessage =  ChatColor.GOLD + String.valueOf(sp.getId()) + 
					" -> Main Entity (" + PLUGIN.getFriendlyName(sp.getMainEntity()) + ChatColor.GOLD + ") at (" +
					loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")"; 
			
			if(!sp.getName().isEmpty()) 
				baseMessage += ChatColor.GREEN + " with name " + ChatColor.GOLD + sp.getName();
			
			PLUGIN.sendMessage(sender, baseMessage);
		}
		
	}
	
}
