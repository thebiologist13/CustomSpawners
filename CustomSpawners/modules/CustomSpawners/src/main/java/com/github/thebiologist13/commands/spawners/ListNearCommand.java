package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ListNearCommand extends SpawnerCommand {

	public ListNearCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public ListNearCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		int near = CONFIG.getInt("players.maxNear", 25);
		List<Spawner> list = new ArrayList<Spawner>();
		
		Iterator<Spawner> itr = CustomSpawners.spawners.values().iterator();
		while(itr.hasNext()) {
			Spawner sp = itr.next();
			
			if(sp.getLoc().distance(player.getLocation()) <= near) {
				
				if(sp.isHidden() && player.hasPermission("customspawners.spawners.listnear.hidden")) {
					list.add(sp);
				} else if(!sp.isHidden()) {
					list.add(sp);
				}	
				
			}
			
		}
		
		if(list.size() == 0) {
			PLUGIN.sendMessage(player, ChatColor.RED + "There are no spawners within " + near + " blocks.");
			return;
		}
		
		PLUGIN.sendMessage(player, ChatColor.GOLD + "Nearby Spawners:");
		for(Spawner sp : list) {
			
			Location loc = sp.getLoc();
			
			String baseMessage =  ChatColor.GOLD + String.valueOf(sp.getId()) + 
					" -> Main Entity (" + PLUGIN.getFriendlyName(sp.getMainEntity()) + ChatColor.GOLD + ") at (" +
					loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")"; 
			
			if(!sp.getName().isEmpty()) {
				PLUGIN.sendMessage(sender, baseMessage + ChatColor.GREEN + " with name " + ChatColor.GOLD + sp.getName());
			} else {
				PLUGIN.sendMessage(sender, baseMessage);
			}
			
		}
		
	}
}
