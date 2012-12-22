package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntitySetTypeCommand extends SpawnerCommand {

	public EntitySetTypeCommand(CustomSpawners plugin) {
		super(plugin);
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
		//Jockey
		boolean createJockey = false;
		
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission(setTypePerm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				String entityType = arg3[1];
				
				if(entityType.equalsIgnoreCase("spiderjockey") || entityType.equalsIgnoreCase("skeletonjockey")) {
					
					if(!config.getBoolean("mobs.spiderjockey")) {
						p.sendMessage(NOT_ALLOWED_ENTITY);
						return;
					}
					
					type = EntityType.SPIDER;
					createJockey = true;
					
				} else {
					
					type = plugin.parseEntityType(entityType, p.hasPermission("customspawners.limitoverride"));
					
					if(type == null) {
						p.sendMessage(NOT_ALLOWED_ENTITY);
						return;
					}
					
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = CustomSpawners.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				String entityType = arg3[2];
				
				if(entityType.equalsIgnoreCase("spiderjockey") || entityType.equalsIgnoreCase("skeletonjockey")) {
					
					if(!config.getBoolean("mobs.spiderjockey")) {
						p.sendMessage(NOT_ALLOWED_ENTITY);
						return;
					}
					
					type = EntityType.SPIDER;
					createJockey = true;
					
				} else {
					
					type = plugin.parseEntityType(entityType, p.hasPermission("customspawners.limitoverride"));
					
					if(type == null) {
						p.sendMessage(NOT_ALLOWED_ENTITY);
						return;
					}
					
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			s.setType(type);
			s.setJockey(createJockey);
			
			//Success
			if(s.getName().isEmpty()) {
				p.sendMessage(ChatColor.GREEN + "Successfully set the base entity type of spawnable entity with ID " 
						+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD + plugin.parseEntityName(type)
						+ ChatColor.GREEN + "!");
			} else {
				p.sendMessage(ChatColor.GREEN + "Successfully set the base entity type of spawnable entity with ID " 
						+ ChatColor.GOLD + s.getId() + " (" + s.getName() + ") " + ChatColor.GREEN + " to " + ChatColor.GOLD
						+ plugin.parseEntityName(type) + ChatColor.GREEN + "!");
			}
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
