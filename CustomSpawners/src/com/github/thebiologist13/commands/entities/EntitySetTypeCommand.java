package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntitySetTypeCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntitySetTypeCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Type
		EntityType type = null;
		//Permissions
		String setTypePerm = "customspawners.entities.settype";
		
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission(setTypePerm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {
				
				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));
				
				if(arg3[1].equalsIgnoreCase("spiderjockey") || arg3[1].equalsIgnoreCase("skeletonjockey")) {
					type = EntityType.SPIDER;
					s.setJockey(true);
				} else {
					type = EntityType.fromName(arg3[1].toUpperCase());
					
					if(type == null || !type.isSpawnable()) {
						p.sendMessage(NOT_ALLOWED_ENTITY);
						return;
					}
				}
				
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
				
				type = EntityType.fromName(arg3[1]);
				
				if(type == null || !type.isSpawnable()) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			s.setType(type);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the base entity type of spawnable entity with ID " 
					+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD + type.getName()
					+ ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
