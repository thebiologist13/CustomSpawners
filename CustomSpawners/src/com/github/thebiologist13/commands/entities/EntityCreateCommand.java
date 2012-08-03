package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.material.MaterialData;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityCreateCommand extends SpawnerCommand {

	//TODO This needs cleanup
	
	private CustomSpawners plugin = null;
	
	@SuppressWarnings("unused")
	private Logger log = null;
	
	private FileConfiguration config = null;
	
	public EntityCreateCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		config = plugin.getCustomConfig();
		log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Base entity type
		EntityType type = null;
		//ID of entity to make
		int id = 0;
		
		//Makes sure the command was from in-game
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Permission check
		if(p.hasPermission("customspawners.entities.create")) {
			
			boolean createJockey = false;
			
			String entityType = arg3[1];
			
			if(entityType.equalsIgnoreCase("spiderjockey") || entityType.equalsIgnoreCase("skeletonjockey")) {
				
				if(!config.getBoolean("mobs.spiderjockey")) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				type = EntityType.SPIDER;
				createJockey = true;
				
			} else if(entityType.equalsIgnoreCase("irongolem")) {
				
				if(!config.getBoolean("mobs.irongolem")) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				type = EntityType.IRON_GOLEM;
				
			} else if(entityType.equalsIgnoreCase("mooshroom")) {
				
				if(!config.getBoolean("mobs.mushroomcow")) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				type = EntityType.MUSHROOM_COW;
				
			} else if(entityType.equalsIgnoreCase("zombiepigman")) {
				
				if(!config.getBoolean("mobs.pigzombie")) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				type = EntityType.PIG_ZOMBIE;
				
			} else if(entityType.equalsIgnoreCase("magmacube") || entityType.equalsIgnoreCase("fireslime") || entityType.equalsIgnoreCase("firecube")) {
				
				if(!config.getBoolean("mobs.magmacube")) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				type = EntityType.MAGMA_CUBE;
				
			} else if(entityType.equalsIgnoreCase("snowman") || entityType.equalsIgnoreCase("snowgolem")) {
				
				if(!config.getBoolean("mobs.snowman")) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				type = EntityType.SNOWMAN;
				
			} else if(entityType.equalsIgnoreCase("ocelot") || entityType.equalsIgnoreCase("ozelot")) {
				
				if(!config.getBoolean("mobs.ocelot")) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				type = EntityType.OCELOT;
				
			} else {
				
				//Try to parse an entity type from input. Null if invalid.
				type = EntityType.fromName(arg3[1]);
				
				if(type == null || !type.isSpawnable()) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
				
				if(!config.getBoolean("mobs." + type.getName().toLowerCase())) {
					p.sendMessage(NOT_ALLOWED_ENTITY);
					return;
				}
			}
			
			//Gets the next available ID for a spawner
			id = plugin.getNextEntityId();
			
			//Creates a new instance of spawner using variables parsed from command
			SpawnableEntity entity = new SpawnableEntity(type, id);
			
			//Converting and setting convenience properties from config
			Object ageObj = config.get("entities.age", "ADULT");
			Object healthObj = config.get("entities.health", "MAXIMUM");
			Object airObj = config.get("entities.air", "MAXIMUM");
			
			if(ageObj.equals("ADULT")) {
				entity.setAge(-1);
			} else if(ageObj.equals("BABY")) {
				entity.setAge(-2);
			} else {
				if(ageObj instanceof Integer) {
					entity.setAge(((Integer) ageObj).intValue());
				} else {
					p.sendMessage(ChatColor.RED + "Invalid config default for entity age. Using \"ADULT\".");
					entity.setAge(-1);
				}
			}
			
			if(healthObj.equals("MAXIMUM")) {
				entity.setHealth(-1);
			} else if(healthObj.equals("MINIMUM")) {
				entity.setHealth(-2);
			} else {
				if(healthObj instanceof Integer) {
					entity.setHealth(((Integer) healthObj).intValue());
				} else {
					p.sendMessage(ChatColor.RED + "Invalid config default for entity health. Using \"MAXIMUM\".");
					entity.setHealth(-1);
				}
			}
			
			if(airObj.equals("MAXIMUM")) {
				entity.setAir(-1);
			} else if(airObj.equals("MINIMUM")) {
				entity.setAir(-2);
			} else {
				if(airObj instanceof Integer) {
					entity.setAir(((Integer) airObj).intValue());
				} else {
					p.sendMessage(ChatColor.RED + "Invalid config default for entity air. Using \"MAXIMUM\".");
					entity.setAir(-1);
				}
			}
			
			//Setting default properties from config
			Villager.Profession profession = Villager.Profession.valueOf(config.getString("entities.profession", "FARMER"));
			entity.setProfession(profession);
			
			MaterialData enderBlock = new MaterialData(config.getInt("entities.endermanBlock", 2));
			entity.setEndermanBlock(enderBlock);
			
			entity.setSaddled(config.getBoolean("entities.isSaddled", false));
			entity.setCharged(config.getBoolean("entities.isCharged", false));
			entity.setTamed(config.getBoolean("entities.isTamed", false));
			entity.setSitting(config.getBoolean("entities.isSitting", false));
			entity.setAngry(config.getBoolean("entities.isAngry", false));
			entity.setJockey(config.getBoolean("entities.isJockey", false));
			String catStr = config.getString("entities.catType", "NONE");
			entity.setCatType(catStr);
			entity.setSlimeSize(config.getInt("entities.slimeSize", 1));
			String color = config.getString("entities.color", "WHITE");
			entity.setColor(color);
			
			if(createJockey) {
				entity.setJockey(true);
			}
			
			CustomSpawners.entities.add(entity);
			
			if(config.getBoolean("data.autosave") && config.getBoolean("data.saveOnCreate")) {
				plugin.autosave(entity);
			}
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Sucessfully created a spawnable entity with ID " + ChatColor.GOLD 
					+ String.valueOf(entity.getId()) + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
