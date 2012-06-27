package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SetLocationCommand extends SpawnerSubCommand {

	private CustomSpawners plugin;
	
	public SetLocationCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Location
		Location loc = null;
		
		//Spawner
		Spawner s = null;
		
		//Target block
		Block target = null;
		
		//Check for player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Permission
		if(p.hasPermission("customspawners.setlocation")) {
			//If the player wants to set the location of the selected spawner
			if(plugin.selection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawnerById(plugin.selection.get(p));
				
			//If the arguments are for selection but nothing is selected
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//If the player wants to set the location of a spawner by ID 
			} else if(arg3.length == 2) {
				
				if(!isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				if(!plugin.isValidId(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}
				
				s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			

			//Try to get the target block after scanning the distance from config
			target = p.getTargetBlock(null, plugin.getConfig().getInt("players.maxDistance", 50));
			
			loc = target.getLocation();
			
			//Set the new location
			s.loc = loc;
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the location of the spawner with ID " + ChatColor.GOLD +
					" to (" + s.loc.getBlockX() + ", " + s.loc.getBlockY() + ", " + s.loc.getBlockZ() + ")" + 
					ChatColor.GREEN + "!");
 		}
	}

}
