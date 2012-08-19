package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.material.MaterialData;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityCreateCommand extends SpawnerCommand {

	public EntityCreateCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Base entity type
		EntityType type = null;
		//ID of entity to make
		int id = 0;
		//Override perm
		String override = "customspawners.limitoverride";
		
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
				
				if(!config.getBoolean("mobs.spiderjockey") && !p.hasPermission(override)) {
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
			entity.setPassive(config.getBoolean("entities.passive", false));
			entity.setUsingCustomDamage(config.getBoolean("entities.useCustomDamage", false));
			entity.setDamage(config.getInt("entities.dealtDamage", 2));
			
			if(createJockey) {
				entity.setJockey(true);
			}
			
			CustomSpawners.entities.put(id, entity);
			
			if(config.getBoolean("data.autosave") && config.getBoolean("data.saveOnCreate")) {
				plugin.getFileManager().autosave(entity);
			}
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Sucessfully created a spawnable entity with ID " + ChatColor.GOLD 
					+ id + ChatColor.GREEN + "!");
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
			
			type = EntityType.FALLING_BLOCK;
			
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
