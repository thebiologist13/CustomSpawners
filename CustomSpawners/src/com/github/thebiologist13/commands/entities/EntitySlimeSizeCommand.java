package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntitySlimeSizeCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntitySlimeSizeCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		//Command Syntax = /customspawners setslimesize [id] <size>
		//Array Index with selection           0                1
		//Without selection                    0        1       2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Size
		int slimeSize = 1;
		//Permissions
		String perm = "customspawners.entities.setslimesize";
		
		final String MUST_BE_INTEGER = ChatColor.RED + "The size of a slime must be an integer.";
		final String INTEGER_OUT_OF_BOUNDS = ChatColor.RED + "A slime must be a size between 1 and 256.";

		//Make sure a player issued command
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		} 

		p = (Player) arg0;

		//Permission check
		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				slimeSize = Integer.parseInt(arg3[1]);
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = plugin.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(!plugin.isInteger(arg3[2])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				slimeSize = Integer.parseInt(arg3[2]);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			if(slimeSize < 1 || slimeSize > 256) {
				p.sendMessage(INTEGER_OUT_OF_BOUNDS);
				return;
			}
			
			s.setSlimeSize(slimeSize);

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the slime size of spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ slimeSize + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}

	}

}
