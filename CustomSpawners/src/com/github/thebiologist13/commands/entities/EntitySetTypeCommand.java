package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntitySetTypeCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private FileConfiguration config = null;
	
	private Logger log = null;
	
	public EntitySetTypeCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
		this.config = plugin.getCustomConfig();
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
				
				s = plugin.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				String entityType = arg3[1];
				
				if(entityType.equalsIgnoreCase("spiderjockey") || entityType.equalsIgnoreCase("skeletonjockey")) {
					
					if(!config.getBoolean("mobs.spiderjockey")) {
						p.sendMessage(NOT_ALLOWED_ENTITY);
						return;
					}
					
					type = EntityType.SPIDER;
					createJockey = true;
					
				} else {
					
					type = parseEntity(entityType);
					
					if(type == null) {
						p.sendMessage(NOT_ALLOWED_ENTITY);
						return;
					}
					
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = plugin.getEntity(arg3[1]);

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
					
					type = parseEntity(entityType);
					
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
						+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD + type.getName()
						+ ChatColor.GREEN + "!");
			} else {
				p.sendMessage(ChatColor.GREEN + "Successfully set the base entity type of spawnable entity with ID " 
						+ ChatColor.GOLD + s.getId() + " (" + s.getName() + ") " + ChatColor.GREEN + " to " + ChatColor.GOLD
						+ type.getName() + ChatColor.GREEN + "!");
			}
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

	private EntityType parseEntity(String entityType) {
		
		EntityType type = null;
		
		if(entityType.equalsIgnoreCase("irongolem")) {
			
			if(!config.getBoolean("mobs.irongolem")) {
				return null;
			}
			
			type = EntityType.IRON_GOLEM;
			
		} else if(entityType.equalsIgnoreCase("mooshroom")) {
			
			if(!config.getBoolean("mobs.mushroomcow")) {
				return null;
			}
			
			type = EntityType.MUSHROOM_COW;
			
		} else if(entityType.equalsIgnoreCase("zombiepigman")) {
			
			if(!config.getBoolean("mobs.pigzombie")) {
				return null;
			}
			
			type = EntityType.PIG_ZOMBIE;
			
		} else if(entityType.equalsIgnoreCase("magmacube") || entityType.equalsIgnoreCase("fireslime") || entityType.equalsIgnoreCase("firecube")) {
			
			if(!config.getBoolean("mobs.magmacube")) {
				return null;
			}
			
			type = EntityType.MAGMA_CUBE;
			
		} else if(entityType.equalsIgnoreCase("snowman") || entityType.equalsIgnoreCase("snowgolem")) {
			
			if(!config.getBoolean("mobs.snowman")) {
				return null;
			}
			
			type = EntityType.SNOWMAN;
			
		} else if(entityType.equalsIgnoreCase("ocelot") || entityType.equalsIgnoreCase("ozelot")) {
			
			if(!config.getBoolean("mobs.ocelot")) {
				return null;
			}
			
			type = EntityType.OCELOT;
			
		} else {
			
			//Try to parse an entity type from input. Null if invalid.
			type = EntityType.fromName(entityType);
			
			if(type == null || !type.isSpawnable()) {
				return null;
			}
			
			if(!config.getBoolean("mobs." + type.getName().toLowerCase())) {
				return null;
			}
		}
		
		return type;
	}
}
