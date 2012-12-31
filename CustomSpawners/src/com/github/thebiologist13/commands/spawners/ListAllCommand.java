package com.github.thebiologist13.commands.spawners;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.ChatColor;
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
		
		Collection<Spawner> spawners = CustomSpawners.spawners.values();
		
		if(spawners.size() == 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "No spawners have been created yet.");
			return;
		}
		
		Iterator<Spawner> itr = spawners.iterator();
		while(itr.hasNext()) {
			
			Spawner sp = itr.next();
			
			String baseMessage =  ChatColor.GOLD + String.valueOf(sp.getId()) + 
					" -> Main Entity (" + PLUGIN.getFriendlyName(sp.getMainEntity()) + ")"; 
			
			if(sp.getName().isEmpty()) {
				PLUGIN.sendMessage(sender, baseMessage + ChatColor.GREEN + " with name " + ChatColor.GOLD + sp.getName());
			} else {
				PLUGIN.sendMessage(sender, baseMessage);
			}
			
		}
		
	}
	
}
