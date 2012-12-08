package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class InfoCommand extends SpawnerCommand {

	public InfoCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		p = (Player) arg0;
		
		if(p == null) {

			//If the player wants to perform command with a selection.
			if(CustomSpawners.consoleSpawner != -1 && arg3.length == 1) {

				s = CustomSpawners.getSpawner(String.valueOf(CustomSpawners.consoleSpawner));

				//Arguments are for selection, but none is selected
			} else if(arg3.length == 1) {

				plugin.sendMessage(arg0, NEEDS_SELECTION);
				return;

				//If the player wants to perform command on a specific spawner
			} else if(arg3.length == 2) {

				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					plugin.sendMessage(arg0, NO_ID);
					return;
				}

				//General error
			} else {

				plugin.sendMessage(arg0, GENERAL_ERROR);
				return;

			}

			//Send info
			plugin.sendMessage(arg0, getInfo(s));
			
		} else {
			
			//Permission check
			if(p.hasPermission("customspawners.spawners.info")) {
				
				//If the player wants to perform command with a selection.
				if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
					
					s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
					
				//Arguments are for selection, but none is selected
				} else if(arg3.length == 1) {
					
					plugin.sendMessage(p, NEEDS_SELECTION);
					return;
				
				//If the player wants to perform command on a specific spawner
				} else if(arg3.length == 2) {

					s = CustomSpawners.getSpawner(arg3[1]);

					if(s == null) {
						plugin.sendMessage(p, NO_ID);
						return;
					}
					
				//General error
				} else {
					
					plugin.sendMessage(arg0, GENERAL_ERROR);
					return;
					
				}
				
				//Send info
				if(!p.hasPermission("customspawners.spawners.info.hidden") && s.isHidden() == true) {
					
					plugin.sendMessage(p, ChatColor.RED + "You are not allowed to view info on that spawner!");
					return;
					
				} else {
					
					plugin.sendMessage(p, getInfo(s));
					
				}
				
			} else {
				
				plugin.sendMessage(p, NO_PERMISSION);
				return;
				
			}
			
		}
		
	}
	
	private String[] getInfo(Spawner s) {
		String typesMessage = "";
		ArrayList<SpawnableEntity> types = new ArrayList<SpawnableEntity>();
		for(SpawnableEntity e : s.getTypeData().values()) {
			types.add(e);
		}
		
		for(int i = 0; i < types.size(); i++) {
			if(i == 0) {
				if(types.get(i).getName().isEmpty()) {
					typesMessage += types.get(i).getId();
				} else {
					typesMessage += types.get(i).getId() + " (" + types.get(i).getName() + ")";
				}
			} else {
				if(types.get(i).getName().isEmpty()) {
					typesMessage += ", " + types.get(i).getId();
				} else {
					typesMessage += ", " + types.get(i).getId() + " (" + types.get(i).getName() + ")";
				}
			}
		}
		
		String header = ChatColor.GREEN + "Information on spawner with ID " + ChatColor.GOLD + String.valueOf(s.getId());
		
		if(!s.getName().isEmpty()) {
			header += " (" + s.getName() + ")";
		}
		
		String converted = "";
		if(s.isConverted()) {
			converted = ChatColor.RED + String.valueOf(s.isConverted());
		} else {
			converted = ChatColor.GREEN + String.valueOf(s.isConverted());
		}
		
		String[] message = {
				"",
				header + ChatColor.GREEN + ": ",
				"",
				ChatColor.GOLD + "Active: " + String.valueOf(s.isActive()),
				ChatColor.GOLD + "Converted: " + converted,
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
				ChatColor.GOLD + "Redstone Triggered: " + String.valueOf(s.isRedstoneTriggered()),
				ChatColor.GOLD + "Uses Spawn Area: " + String.valueOf(s.isUsingSpawnArea()),
				ChatColor.GOLD + "Spawn Area Locations: ",
				ChatColor.GOLD + "  Point 1 - (" + s.getAreaPoints()[0].getBlockX() + "," +
						s.getAreaPoints()[0].getBlockY() + "," + s.getAreaPoints()[0].getBlockZ() + ") ",
				ChatColor.GOLD + "  Point 2 - (" + s.getAreaPoints()[1].getBlockX() + "," + 
						s.getAreaPoints()[1].getBlockY() + "," + s.getAreaPoints()[1].getBlockZ() + ")"
				};
		
		return message;
		
	}
	
}
