package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class LightLevelCommand extends SpawnerCommand {

	public LightLevelCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Permissions
		if(p.hasPermission("customspawners.spawners.setmaxlight") && arg3[0].equalsIgnoreCase("setmaxlight")) {
			
			int maxLight = -1;
			
			//If the player wants to perform command with a selection.
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
				if(!CustomSpawners.isInteger(arg3[1])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				maxLight = Integer.parseInt(arg3[1]);
				
			//Arguments are for selection, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
			
			//If the player wants to perform command on a specific spawner
			} else if(arg3.length == 3) {

				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[2])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				maxLight = Integer.parseInt(arg3[2]);
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			if(maxLight > 15 || maxLight < 0) {
				p.sendMessage(INVALID_VALUES);
				return;
			}
			
			//Set the max light level
			s.setMaxLightLevel((byte) maxLight);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the maximum light level of spawner with ID " + ChatColor.GOLD +
					plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(maxLight) + 
					ChatColor.GREEN + "!");
			
		} else if(p.hasPermission("customspawners.spawners.setminlight") && arg3[0].equalsIgnoreCase("setminlight")) {

			int minLight = -1;
			
			//If the player wants to perform command with a selection.
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
				if(!CustomSpawners.isInteger(arg3[1])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				minLight = Integer.parseInt(arg3[1]);
				
			//Arguments are for selection, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
			
			//If the player wants to perform command on a specific spawner
			} else if(arg3.length == 3) {
				
				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[2])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				minLight = Integer.parseInt(arg3[2]);
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			if(minLight > 15 || minLight < 0) {
				p.sendMessage(INVALID_VALUES);
				return;
			}
			
			//Set the minimum light level
			s.setMinLightLevel((byte) minLight);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the minimum light level of spawner with ID " + ChatColor.GOLD +
					plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(minLight) + 
					ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
