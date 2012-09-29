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
					
					type = parseEntity(entityType, p);
					
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
					
					type = parseEntity(entityType, p);
					
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

	private EntityType parseEntity(String entityType, Player p) {
		
		String override = "customspawners.limitoverride";
		
		EntityType type = null;
		
		if(entityType.equalsIgnoreCase("irongolem")) {
			
			if(!config.getBoolean("mobs.irongolem") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.IRON_GOLEM;
			
		} else if(entityType.equalsIgnoreCase("mooshroom")) {
			
			if(!config.getBoolean("mobs.mushroomcow") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.MUSHROOM_COW;
			
		} else if(entityType.equalsIgnoreCase("zombiepigman")) {
			
			if(!config.getBoolean("mobs.pigzombie") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.PIG_ZOMBIE;
			
		} else if(entityType.equalsIgnoreCase("magmacube") || entityType.equalsIgnoreCase("fireslime") || entityType.equalsIgnoreCase("firecube")) {
			
			if(!config.getBoolean("mobs.magmacube") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.MAGMA_CUBE;
			
		} else if(entityType.equalsIgnoreCase("snowman") || entityType.equalsIgnoreCase("snowgolem")) {
			
			if(!config.getBoolean("mobs.snowman") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.SNOWMAN;
			
		} else if(entityType.equalsIgnoreCase("ocelot") || entityType.equalsIgnoreCase("ozelot")) {
			
			if(!config.getBoolean("mobs.ocelot") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.OCELOT;
			
		} else if(entityType.equalsIgnoreCase("ocelot") || entityType.equalsIgnoreCase("ozelot")) {
			
			if(!config.getBoolean("mobs.ocelot") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.OCELOT;
			
		} else if(entityType.equalsIgnoreCase("arrow")) {
			
			if(!config.getBoolean("mobs.arrow") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.ARROW;
			
		} else if(entityType.equalsIgnoreCase("snowball")) {
			
			if(!config.getBoolean("mobs.snowball") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.SNOWBALL;
			
		} else if(entityType.equalsIgnoreCase("falling_block") || entityType.equalsIgnoreCase("fallingblock") ||
				entityType.equalsIgnoreCase("sand") || entityType.equalsIgnoreCase("gravel")) {
			
			if(!config.getBoolean("mobs.fallingblock") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.FALLING_BLOCK; //TODO probably can't do anything with this, yet
			
		} else if(entityType.equalsIgnoreCase("tnt") || entityType.equalsIgnoreCase("primed_tnt")
				|| entityType.equalsIgnoreCase("primed_tnt")) {
			
			if(!config.getBoolean("mobs.tnt") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.PRIMED_TNT;
			
		} else if(entityType.equalsIgnoreCase("firecharge") || entityType.equalsIgnoreCase("smallfireball")
				|| entityType.equalsIgnoreCase("fire_charge")|| entityType.equalsIgnoreCase("small_fireball")) {
			
			if(!config.getBoolean("mobs.fireball") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.SMALL_FIREBALL;
			
		} else if(entityType.equalsIgnoreCase("fireball") || entityType.equalsIgnoreCase("ghastball")
				|| entityType.equalsIgnoreCase("fire_ball")|| entityType.equalsIgnoreCase("ghast_ball")) {
			
			if(!config.getBoolean("mobs.fireball") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.FIREBALL;
			
		} else if(entityType.equalsIgnoreCase("potion") || entityType.equalsIgnoreCase("splashpotion")
				|| entityType.equalsIgnoreCase("splash_potion")) {
			
			if(!config.getBoolean("mobs.potion") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.SPLASH_POTION; //TODO How to actually "launch" and change type?
			
		} else if(entityType.equalsIgnoreCase("experience_bottle") || entityType.equalsIgnoreCase("experiencebottle")
				|| entityType.equalsIgnoreCase("xpbottle") || entityType.equalsIgnoreCase("xp_bottle")
				|| entityType.equalsIgnoreCase("expbottle") || entityType.equalsIgnoreCase("exp_bottle")) {
			
			if(!config.getBoolean("mobs.potion") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.THROWN_EXP_BOTTLE; //TODO How to actually "launch" and change type?
			
		} else if(entityType.equalsIgnoreCase("item") || entityType.equalsIgnoreCase("drop")) {
			
			if(!config.getBoolean("mobs.item") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.DROPPED_ITEM; //TODO How to change type?
			
		} else if(entityType.equalsIgnoreCase("enderpearl") || entityType.equalsIgnoreCase("ender_pearl")
				|| entityType.equalsIgnoreCase("enderball") || entityType.equalsIgnoreCase("ender_ball")) {
			
			if(!config.getBoolean("mobs.enderpearl") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.ENDER_PEARL;
			
		} else if(entityType.equalsIgnoreCase("endercrystal") || entityType.equalsIgnoreCase("ender_crystal")
				|| entityType.equalsIgnoreCase("enderdragoncrystal") || entityType.equalsIgnoreCase("enderdragon_crystal")) {
			
			if(!config.getBoolean("mobs.endercrystal") && !p.hasPermission(override)) {
				return null;
			}
			
			type = EntityType.ENDER_CRYSTAL;
			
		} else {
			
			//Try to parse an entity type from input. Null if invalid.
			type = EntityType.fromName(entityType);
			
			if(type == null || !type.isSpawnable()) {
				return null;
			}
			
			if(!config.getBoolean("mobs." + type.getName().toLowerCase()) && !p.hasPermission(override)) {
				return null;
			}
		}
		
		return type;
	}
}
