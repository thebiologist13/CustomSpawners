package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class InfoCommand extends SpawnerCommand {

	private CustomSpawners plugin;
	
	public InfoCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		//Make sure a player issued command
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		} 
		
		p = (Player) arg0;
		
		//Permission check
		if(p.hasPermission("customspawners.spawners.info")) {
			
			//If the player wants to perform command with a selection.
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawnerById(CustomSpawners.spawnerSelection.get(p));
				
			//Arguments are for selection, but none is selected
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
			
			//If the player wants to perform command on a specific spawner
			} else if(arg3.length == 2) {
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				if(!plugin.isValidSpawner(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}
				
				s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			//Send info
			if(!p.hasPermission("customspawners.spawners.info.hidden") && s.isHidden() == true) {
				p.sendMessage(ChatColor.RED + "You are not allowed to view info on that spawner!");
				return;
			} else {
				
				String typesMessage = "";
				SpawnableEntity[] types = s.getEntitiesAsArray();
				for(int i = 0; i < types.length; i++) {
					if(i == 0) {
						if(types[i].getName().isEmpty()) {
							typesMessage += types[i].getId();
						} else {
							typesMessage += types[i].getId() + " (" + types[i].getName() + ")";
						}
					} else {
						if(types[i].getName().isEmpty()) {
							typesMessage += ", " + types[i].getId();
						} else {
							typesMessage += ", " + types[i].getId() + " (" + types[i].getName() + ")";
						}
					}
				}
				
				String header = ChatColor.GREEN + "Information on spawner with ID " + ChatColor.GOLD + String.valueOf(s.getId());
				
				if(!s.getName().isEmpty()) {
					header += " (" + s.getName() + ")";
				}
				
				String[] message = {
						"",
						header + ChatColor.GREEN + ": ",
						"",
						ChatColor.GOLD + "Active: " + String.valueOf(s.isActive()),
						ChatColor.GOLD + "Hidden: " + String.valueOf(s.isHidden()),
						ChatColor.GOLD + "Types: " + typesMessage,
						ChatColor.GOLD + "Location: " + "(" + s.getLoc().getBlockX() + ", " + s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")",
						ChatColor.GOLD + "Spawn Rate: " + String.valueOf(s.getMobsPerSpawn()) + " per " + String.valueOf(s.getRate()) + " ticks",
						ChatColor.GOLD + "Spawn Radius: " + String.valueOf(s.getRadius()),
						ChatColor.GOLD + "Maximum Mobs: " + String.valueOf(s.getMaxMobs()),
						ChatColor.GOLD + "Maximum Light: " + String.valueOf(s.getMaxLightLevel()),
						ChatColor.GOLD + "Minimum Light: " + String.valueOf(s.getMinLightLevel()),
						ChatColor.GOLD + "Maximum Distance: " + String.valueOf(s.getMaxPlayerDistance()),
						ChatColor.GOLD + "Minimum Distance: " + String.valueOf(s.getMinPlayerDistance()),
						ChatColor.GOLD + "Redstone Triggered: " + String.valueOf(s.isRedstoneTriggered())
						};
				
				p.sendMessage(message);
			}
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
