package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityFireTicksCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityFireTicksCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Command Syntax = /customspawners setfireticks [id] <size>
		//Array Index with selection            0                1
		//Without selection                     0        1       2

		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Size
		int fireTicks = 0;
		//Permissions
		String perm = "customspawners.entities.setfireticks";
		
		final String MUST_BE_INTEGER = ChatColor.RED + "The fire ticks must be an integer.";
		
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				fireTicks = Integer.parseInt(arg3[1]);
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				int id = 0;

				//Check that the ID entered is a number
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}

				id = Integer.parseInt(arg3[1]);

				//Check if the ID entered is the ID of a entity
				if(!plugin.isValidEntity(id)) {
					p.sendMessage(NO_ID);
					return;
				}

				s = plugin.getEntityById(id);
				
				if(!plugin.isInteger(arg3[2])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				fireTicks = Integer.parseInt(arg3[2]);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Set
			s.setFireTicks(fireTicks);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the fire ticks of spawnable entity with ID " 
					+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ fireTicks + ChatColor.GREEN + "!");
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
