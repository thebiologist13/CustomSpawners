package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityAirCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityAirCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /entities setair [id] <health>
		//Array Index with selection    0           1
		//Without selection             0     1     2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Permissions
		String perm = "customspawners.entities.setair";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));
				
				if(arg3[1].equalsIgnoreCase("MAXIMUM")) {
					s.setAir(-1);
				} else if(arg3[1].equalsIgnoreCase("MINIMUM")) {
					s.setAir(-2);
				} else {
					if(plugin.isInteger(arg3[1])) {
						s.setAir(Integer.parseInt(arg3[1]));
					} else {
						p.sendMessage(ChatColor.RED + "Air value must be an integer, \"MAXIMUM\", or \"MINIMUM\".");
						return;
					}
				}
				
				//Success
				p.sendMessage(ChatColor.GREEN + "Successfully set the air of spawnable entity with ID " 
						+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD 
						+ arg3[1] + ChatColor.GREEN + "!");
				
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
				
				if(arg3[1].equalsIgnoreCase("MAXIMUM")) {
					s.setAir(-1);
				} else if(arg3[1].equalsIgnoreCase("MINIMUM")) {
					s.setAir(-2);
				} else {
					if(plugin.isInteger(arg3[1])) {
						s.setAir(Integer.parseInt(arg3[1]));
					} else {
						p.sendMessage(ChatColor.RED + "Air value must be an integer, \"MAXIMUM\", or \"MINIMUM\".");
						return;
					}
				}
				
				//Success
				p.sendMessage(ChatColor.GREEN + "Successfully set the air of spawnable entity with ID " 
						+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD 
						+ arg3[2] + ChatColor.GREEN + "!");
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
