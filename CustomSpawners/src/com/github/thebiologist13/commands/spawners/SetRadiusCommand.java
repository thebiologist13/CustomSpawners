package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public class SetRadiusCommand extends SubCommand {

	public SetRadiusCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		//Radius
		double rad = -1;
		
		//Check to make sure the command is from a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Permission checking
		if(p.hasPermission("customspawners.spawners.setradius")) {
			
			//If the player wants to set the spawn radius of a selected spawner
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
				if(!CustomSpawners.isInteger(arg3[1]) && !CustomSpawners.isDouble(arg3[1])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				if(CustomSpawners.isInteger(arg3[1])) {
					rad = Integer.parseInt(arg3[1]);
				}
				if(CustomSpawners.isDouble(arg3[1])) {
					rad = Double.parseDouble(arg3[1]);
				}
				
			//If no spawner is selected, but arguments for a selected spawner were put in.
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//If the player wants to set the spawn radius of a spawner by ID
			} else if(arg3.length == 3) {

				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[2]) && !CustomSpawners.isDouble(arg3[2])) {
					p.sendMessage(SPECIFY_NUMBER);
					return;
				}
				
				if(CustomSpawners.isInteger(arg3[2])) {
					rad = Integer.parseInt(arg3[2]);
				}
				if(CustomSpawners.isDouble(arg3[2])) {
					rad = Double.parseDouble(arg3[2]);
				}
			
			//General error message
			} else {
				
				plugin.log.info(GENERAL_ERROR);
				return;
				
			}
			
			if(rad > plugin.getCustomConfig().getDouble("spawners.radiusLimit", 128) || rad < 0) {
				if(!p.hasPermission("customspawners.limitoverride")) {
					p.sendMessage(INVALID_VALUES);
					return;
				} else {
					p.sendMessage(NO_PERMISSION);
					return;
				}
			}
			
			//Set the new radius
			s.setRadius(rad);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the spawn radius of the spawner with ID " + ChatColor.GOLD + 
					plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD + String.valueOf(rad) + 
					ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
