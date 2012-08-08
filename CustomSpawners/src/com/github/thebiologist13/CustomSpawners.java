package com.github.thebiologist13;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import com.github.thebiologist13.listeners.InteractEvent;
import com.github.thebiologist13.listeners.MobCombustEvent;
import com.github.thebiologist13.listeners.MobDamageEvent;
import com.github.thebiologist13.listeners.MobDeathEvent;
import com.github.thebiologist13.listeners.PlayerLogoutEvent;
import com.github.thebiologist13.listeners.PlayerTargetEvent;
import com.github.thebiologist13.listeners.MobDamageByEntityEvent;

public class CustomSpawners extends JavaPlugin {
	
	//Logger
	public Logger log = Logger.getLogger("Minecraft");
	
	//All the spawners in the server.
	public static ConcurrentHashMap<Integer, Spawner> spawners = new ConcurrentHashMap<Integer, Spawner>();
	
	//All of the entity types on the server
	public static ConcurrentHashMap<Integer, SpawnableEntity> entities = new ConcurrentHashMap<Integer, SpawnableEntity>();
	
	//Selected spawners for players
	public static ConcurrentHashMap<Player, Integer> spawnerSelection = new ConcurrentHashMap<Player, Integer>();
	
	//Selected entities for players
	public static ConcurrentHashMap<Player, Integer> entitySelection = new ConcurrentHashMap<Player, Integer>();
	
	//Player selection area Point 1
	public static ConcurrentHashMap<Player, Location> selectionPointOne = new ConcurrentHashMap<Player, Location>();
	
	//Player selection area Point 2
	public static ConcurrentHashMap<Player, Location> selectionPointTwo = new ConcurrentHashMap<Player, Location>();
	
	//Default Entity to use
	public static SpawnableEntity defaultEntity = null;
	
	//YAML variable
	private FileConfiguration config;
	
	//YAML file variable
	private File configFile;
	
	public void onEnable() {
		
		//Serialization
		ConfigurationSerialization.registerClass(EntityPotionEffect.class, "Effect");
		
		//Config
		config = getCustomConfig();
		
		//Default Entity
		defaultEntity = new SpawnableEntity(EntityType.fromName(config.getString("entities.type")), -2);
		defaultEntity.setName("Default");
		
		//Commands
		//TODO Note: to check if a creature is valid, use instanceof Creature or EntityType.isSpawnable()
		SpawnerExecutor se = new SpawnerExecutor(this);
		CustomSpawnersExecutor cse = new CustomSpawnersExecutor(this);
		EntitiesExecutor ee = new EntitiesExecutor(this);
		getCommand("customspawners").setExecutor(cse);
		getCommand("spawners").setExecutor(se);
		getCommand("entities").setExecutor(ee);
		
		//Listeners
		getServer().getPluginManager().registerEvents(new PlayerLogoutEvent(), this);
		getServer().getPluginManager().registerEvents(new MobDamageEvent(), this);
		getServer().getPluginManager().registerEvents(new MobCombustEvent(), this);
		getServer().getPluginManager().registerEvents(new MobDamageByEntityEvent(), this);
		getServer().getPluginManager().registerEvents(new PlayerTargetEvent(), this);
		getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
		getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
		
		//Load entities from file
		loadEntities();
		
		//Load spawners from files
		loadSpawners();
		
		/*
		 * Spawning Thread
		 */
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			public void run() {
				
				Iterator<Spawner> spawnerItr = spawners.values().iterator();
				
				while(spawnerItr.hasNext()) {
					Spawner s = spawnerItr.next();
					s.tick();
				}
				
			}
			
		}, 20, 1);
		
		/*
		 * Entity Removal Check Thread
		 * This thread verifies that all spawned mobs still exist. For example, if a mob
		 * despawned, it will be removed.
		 */
		
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				try {
					Iterator<Spawner> spawnerItr = spawners.values().iterator();
					while(spawnerItr.hasNext()) {
						Spawner s = spawnerItr.next();
						Iterator<Integer> mobItr = s.getMobs().keySet().iterator();
						Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
						Iterator<LivingEntity> entityItr = s.getLoc().getWorld().getLivingEntities().iterator();
						ArrayList<Integer> entityIdList = new ArrayList<Integer>();
						
						while(entityItr.hasNext()) {
							entityIdList.add(entityItr.next().getEntityId());
						}
						
						while(mobItr.hasNext()) {
							int mobId = mobItr.next();
							
							if(!entityIdList.contains(mobId)) {
								mobItr.remove();
							}
							
						}
						
						while(passiveMobItr.hasNext()) {
							int mobId = passiveMobItr.next();
							
							if(!entityIdList.contains(mobId)) {
								passiveMobItr.remove();
							}
							
						}
						
					}
					
				} catch(ConcurrentModificationException e) {
					return;
				}
				
			}
			
		}, 20, 20);
		
		/*
		 * Autosave Thread
		 * This thread manages autosaving
		 */
		if(config.getBoolean("data.autosave") && config.getBoolean("data.saveOnClock")) {
			
			int interval = config.getInt("data.interval") * 1200;
			
			getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

				@Override
				public void run() {
					
					autosaveAll();
					
				}
				
			}, 20, interval);
		}
		
		//Enable message
		log.info("CustomSpawners by thebiologist13 has been enabled!");
	}
	
	public void onDisable() {
		
		//Saving Entities
		saveEntities();
		//Saving spawners
		saveSpawners();
		
		//Stop Tasks
		getServer().getScheduler().cancelTasks(this);
		
		//Disable message
		log.info("CustomSpawners by thebiologist13 has been disabled!");
	}
	
	//Config stuff
	//Credit goes to respective owners.
	public void reloadCustomConfig() {
		if (configFile == null) {
		    configFile = new File(getDataFolder(), "config.yml");
		    
		    if(!configFile.exists()){
		        configFile.getParentFile().mkdirs();
		        copy(getResource("config.yml"), configFile);
		    }
		    
		}
		
		config = YamlConfiguration.loadConfiguration(configFile);
		 
		// Look for defaults in the jar
		InputStream defConfigStream = this.getResource("config.yml");
		if (defConfigStream != null) {
		    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		    config.options().copyDefaults(true);
		    config.setDefaults(defConfig);
		}
		
	}
	
	public FileConfiguration getCustomConfig() {
		if (config == null) {
	        reloadCustomConfig();
	    }
	    return config;
	}
	
	public void saveCustomConfig() {
		if (config == null || configFile == null) {
		    return;
		}
		try {
			config.save(configFile);
		} catch (IOException ex) {
			log.severe("Could not save config to " + configFile.getPath());
		}
	}
	
	public void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			log.severe("Could not copy config from jar!");
			e.printStackTrace();
		}
	}
	
	//Gets a spawner
	public Spawner getSpawner(String ref) {
		if(this.isInteger(ref)) {
			int id = Integer.parseInt(ref);
			Iterator<Integer> spawnerItr = spawners.keySet().iterator();
			
			while(spawnerItr.hasNext()) {
				int currentId = spawnerItr.next();
				
				if(currentId == id) {
					return spawners.get(id);
				}
			}
		} else {
			Iterator<Integer> spawnerItr = spawners.keySet().iterator();
			
			while(spawnerItr.hasNext()) {
				Integer id = spawnerItr.next();
				Spawner s = spawners.get(id);
				String name = s.getName();
				
				if(name == null) {
					return null;
				}
				
				if(name.equalsIgnoreCase(ref)) {
					return s;
				}
			}
		}
		
		return null;
	}
	
	//Gets an entity
	public SpawnableEntity getEntity(String ref) {
		if(this.isInteger(ref)) {
			int id = Integer.parseInt(ref);
			Iterator<Integer> entityItr = entities.keySet().iterator();
			
			while(entityItr.hasNext()) {
				int currentId = entityItr.next();
				
				if(currentId == id) {
					return entities.get(id);
				}
			}
		} else {
			Iterator<Integer> entityItr = entities.keySet().iterator();
			
			while(entityItr.hasNext()) {
				Integer id = entityItr.next();
				SpawnableEntity s = entities.get(id);
				String name = s.getName();
				
				if(name == null) {
					return null;
				}
				
				if(name.equalsIgnoreCase(ref)) {
					return s;
				}
			}
		}
		
		return null;
	}
	
	//Next available spawner ID
	public int getNextSpawnerId() {
		int returnId = 0;
		boolean taken = true;
		ArrayList<Integer> spawnerIDs = new ArrayList<Integer>();
		
		Iterator<Integer> spawnerItr = spawners.keySet().iterator();
		while(spawnerItr.hasNext()) {
			spawnerIDs.add(spawnerItr.next());
		}
		
		while(taken) {
			
			if(spawnerIDs.size() == 0) {
				return 0;
			}
			
			for(Integer i : spawnerIDs) {
				if(returnId == i) {
					taken = true;
					break;
				} else {
					taken = false;
				}
			}
			
			if(taken) {
				returnId++;
			}
		}
		
		return returnId;
	}
	
	//Next available entity id
	public int getNextEntityId() {
		int returnId = 0;
		boolean taken = true;
		ArrayList<Integer> entityIDs = new ArrayList<Integer>();
		
		Iterator<Integer> entityItr = entities.keySet().iterator();
		while(entityItr.hasNext()) {
			entityIDs.add(entityItr.next());
		}
		
		while(taken) {
			
			if(entityIDs.size() == 0) {
				return 0;
			}
			
			for(Integer i : entityIDs) {
				if(returnId == i) {
					taken = true;
					break;
				} else {
					taken = false;
				}
			}
			
			if(taken) {
				returnId++;
			}
		}
		
		return returnId;
	}
	
	//Remove a spawner
	public void removeSpawner(Spawner s) {
		if(spawners.containsValue(s)) {
			spawners.remove(s.getId());
			resetSpawnerSelections(s.getId());
		}
	}
	
	//Remove an entity
	public void removeEntity(SpawnableEntity e) {
		if(entities.containsValue(e)) {
			entities.remove(e.getId());
			resetEntitySelections(e.getId());
		}
	}
	
	//Convenience method for accurately testing if a string can be parsed to an integer.
	public boolean isInteger(String what) {
		try {
			Integer.parseInt(what);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	//Convenience method for accurately testing if a string can be parsed to an double.
	public boolean isDouble(String what) {
		try {
			Double.parseDouble(what);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	//Gets a string to represent the name of the spawner (String version of ID or name)
	public String getFriendlyName(Spawner s) {
		if(s.getName().isEmpty()) {
			return String.valueOf(s.getId());
		} else {
			return s.getName();
		}
	}
	
	//Gets a string to represent the name of the entity (String version of ID or name)
	public String getFriendlyName(SpawnableEntity e) {
		if(e.getName().isEmpty()) {
			return String.valueOf(e.getId());
		} else {
			return e.getName();
		}
	}
	
	//Resets selections if a spawner is removed
	public void resetSpawnerSelections(int id) {
		for(Player p : spawnerSelection.keySet()) {
			if(spawnerSelection.get(p) == id) {
				p.sendMessage(ChatColor.RED + "Your selected spawner has been removed.");
				spawnerSelection.remove(p);
			}
		}
	}
	
	//Resets selections if a SpawnableEntity has been removed
	public void resetEntitySelections(int id) {
		for(Player p : entitySelection.keySet()) {
			if(entitySelection.get(p) == id) {
				p.sendMessage(ChatColor.RED + "Your selected entity has been removed.");
				entitySelection.remove(p);
			}
		}
	}
	
	//Loads spawners from file
	public void loadSpawners() {
		log.info("Loading spawners from directory " + getDataFolder().getPath() + File.separator + "Spawners");
		File sDir = new File(getDataFolder() + File.separator + "Spawners");
		if(!sDir.exists()) {
			sDir.mkdirs();
		}
		File[] sFiles = sDir.listFiles();
		log.info(String.valueOf(sFiles.length) + " total spawners.");
		for(File f : sFiles) {
			if(f.getName().endsWith(".yml")) {
				log.info("Loading " + f.getName());
				FileConfiguration yaml = YamlConfiguration.loadConfiguration(f);
				
				int id = yaml.getInt("id");
				String name = yaml.getString("name");
				double radius = yaml.getDouble("radius");
				boolean redstoneTriggered = yaml.getBoolean("redstone");
				int maxPlayerDistance = yaml.getInt("maxDistance");
				int minPlayerDistance = yaml.getInt("minDistance");
				boolean active = yaml.getBoolean("active");
				byte maxLightLevel = (byte) yaml.getInt("maxLight");
				byte minLightLevel = (byte) yaml.getInt("minLight");
				boolean hidden = yaml.getBoolean("hidden");
				int mobsPerSpawn = yaml.getInt("mobsPerSpawn"); 
				int maxMobs = yaml.getInt("maxMobs"); 
				List<?> mobs = yaml.getList("mobs"); 
				List<?> passiveMobs = yaml.getList("passiveMobs");
				int rate = yaml.getInt("rate");
				boolean usingSpawnArea = yaml.getBoolean("useSpawnArea");
				
				//Convert Raw yaml list of mobs to ArrayList<Integer>
				Iterator<?> mobItr = mobs.iterator();
				Iterator<?> passiveMobItr = passiveMobs.iterator();
				HashMap<Integer, SpawnableEntity> mobsMap = new HashMap<Integer, SpawnableEntity>();
				HashMap<Integer, SpawnableEntity> passiveMobsMap = new HashMap<Integer, SpawnableEntity>();
				
				while(mobItr.hasNext()) {
					Object o = mobItr.next();
					
					if(o instanceof String) {
						String pair = (String) o;
						int key = Integer.parseInt(pair.substring(0, pair.indexOf("_")));
						int value = Integer.parseInt(pair.substring(pair.indexOf("_") + 1, pair.length()));
						
						SpawnableEntity e = getEntity(String.valueOf(value));
						
						mobsMap.put(key, e);
					}
				}
				
				while(passiveMobItr.hasNext()) {
					Object o = passiveMobItr.next();
					
					if(o instanceof String) {
						String pair = (String) o;
						int key = Integer.parseInt(pair.substring(0, pair.indexOf("_")));
						int value = Integer.parseInt(pair.substring(pair.indexOf("_") + 1, pair.length()));
						
						SpawnableEntity e = getEntity(String.valueOf(value));
						
						passiveMobsMap.put(key, e);
					}
				}
				
				//Convert Raw yaml list of passive mobs to ArrayList<Integer>
				ArrayList<Integer> passiveMobIDs = new ArrayList<Integer>();
				for(Object o : passiveMobs) {
					if(o instanceof Integer) {
						passiveMobIDs.add((Integer) o);
					}
				}
				
				//Location
				String locWorld = yaml.getString("location.world");
				int locX = yaml.getInt("location.x");
				int locY = yaml.getInt("location.y");
				int locZ = yaml.getInt("location.z");
				Location loc = new Location(getServer().getWorld(locWorld), locX, locY, locZ);
				
				//Spawn Area Points
				Location[] areaPoints = new Location[2];
				
				//Point 1
				String p1World = yaml.getString("p1.world");
				int p1x = yaml.getInt("p1.x");
				int p1y = yaml.getInt("p1.y");
				int p1z = yaml.getInt("p1.z");
				Location p1 = new Location(getServer().getWorld(p1World), p1x, p1y, p1z);
				
				//Point 2
				String p2World = yaml.getString("p2.world");
				int p2x = yaml.getInt("p2.x");
				int p2y = yaml.getInt("p2.y");
				int p2z = yaml.getInt("p2.z");
				Location p2 = new Location(getServer().getWorld(p2World), p2x, p2y, p2z);
				
				areaPoints[0] = p1;
				areaPoints[1] = p2;
				
				//Mob types
				List<?> listType = yaml.getList("spawnableEntities");
				HashMap<Integer, SpawnableEntity> entities = new HashMap<Integer, SpawnableEntity>();
				for(Object o : listType) {
					if(o instanceof Integer) {
						int entityID = (Integer) o;
						SpawnableEntity se = getEntity(String.valueOf(entityID));
						entities.put(entityID, se);
					}
				}
				
				SpawnableEntity baseEntity = null;
				for(SpawnableEntity e : entities.values()) {
					baseEntity = e;
					break;
				}
				
				Spawner s = new Spawner(baseEntity, loc, name, id, getServer());
				s.setRadius(radius);
				s.setRedstoneTriggered(redstoneTriggered);
				s.setMaxPlayerDistance(maxPlayerDistance);
				s.setMinPlayerDistance(minPlayerDistance);
				s.setActive(active);
				s.setMaxLightLevel(maxLightLevel);
				s.setMinLightLevel(minLightLevel);
				s.setHidden(hidden);
				s.setMobsPerSpawn(mobsPerSpawn);
				s.setMaxMobs(maxMobs);
				s.setRate(rate);
				s.setUseSpawnArea(usingSpawnArea);
				s.setAreaPoints(areaPoints);
				s.setTypeData(entities);
				s.setMobs(mobsMap);
				s.setPassiveMobs(passiveMobsMap);
				
				spawners.put(id, s);
			}
		}
		
		log.info("Load Complete!");
	}
	
	//Saves then loads spawners from file
	public void reloadSpawners() {
		saveSpawners();
		loadSpawners();
	}
	
	//Saves spawners to file
	public void saveSpawners() {
		log.info("Saving spawners...");
		log.info(String.valueOf(spawners.size()) + " spawners to save.");
		boolean killOnReload = config.getBoolean("spawners.killOnReload", false);
		Iterator<Spawner> spawnerItr = spawners.values().iterator();
		
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			
			log.info("Saving spawner " + String.valueOf(s.getId()) + " to " + getDataFolder() + File.separator + "Spawners" + 
					File.separator + String.valueOf(s.getId()) + ".yml");
			File saveFile = new File(getDataFolder() + File.separator + "Spawners" + File.separator + String.valueOf(s.getId()) + ".yml");
			FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);
			
			HashMap<Integer, SpawnableEntity> allMobs = s.getMobs();
			allMobs.putAll(s.getPassiveMobs());
			if(killOnReload) {
				for(Integer e : allMobs.keySet()) {
					List<LivingEntity> entities = getServer().getWorld(s.getLoc().getWorld().getName()).getLivingEntities();
					for(LivingEntity le : entities) {
						if(le.getEntityId() == e) {
							le.remove();
						}
					}
				}
				s.getMobs().clear();
				s.getPassiveMobs().clear();
			}
			
			List<String> mobIDs = new ArrayList<String>();
			Iterator<Integer> mobItr = s.getMobs().keySet().iterator();
			while(mobItr.hasNext()) {
				int mobId = mobItr.next();
				int entityId = s.getMobs().get(mobId).getId();
				String toYaml = mobId + "_" + entityId;
				
				mobIDs.add(toYaml);
			}
			
			List<String> passiveMobIDs = new ArrayList<String>();
			Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
			while(passiveMobItr.hasNext()) {
				int passiveMobId = passiveMobItr.next();
				int entityId = s.getPassiveMobs().get(passiveMobId).getId();
				String toYaml = passiveMobId + "_" + entityId;
				
				passiveMobIDs.add(toYaml);
			}
			
			List<Integer> spawnableEntityIDs = new ArrayList<Integer>();
			for(Integer i : s.getTypeData().keySet()) {
				spawnableEntityIDs.add(i);
			}
			
			Location[] areaPoints = s.getAreaPoints();
			
			if(yaml.getList("mobs") == null) {
				yaml.set("mobs", null);
			}
			
			yaml.options().header("DO NOT MODIFY THIS FILE!");
			
			yaml.set("id", s.getId());
			yaml.set("name", s.getName());
			yaml.set("spawnableEntities", spawnableEntityIDs);
			yaml.set("active", s.isActive());
			yaml.set("hidden", s.isHidden());
			yaml.set("radius", s.getRadius());
			yaml.set("useSpawnArea", s.isUsingSpawnArea());
			yaml.set("redstone", s.isRedstoneTriggered());
			yaml.set("maxDistance", s.getMaxPlayerDistance());
			yaml.set("minDistance", s.getMinPlayerDistance());
			yaml.set("maxLight", s.getMaxLightLevel());
			yaml.set("minLight", s.getMinLightLevel());
			yaml.set("mobsPerSpawn", s.getMobsPerSpawn());
			yaml.set("maxMobs", s.getMaxMobs());
			yaml.set("location.world", s.getLoc().getWorld().getName());
			yaml.set("location.x", s.getLoc().getBlockX());
			yaml.set("location.y", s.getLoc().getBlockY());
			yaml.set("location.z", s.getLoc().getBlockZ());
			yaml.set("p1.world", areaPoints[0].getWorld().getName());
			yaml.set("p1.x", areaPoints[0].getBlockX());
			yaml.set("p1.y", areaPoints[0].getBlockY());
			yaml.set("p1.z", areaPoints[0].getBlockZ());
			yaml.set("p2.world", areaPoints[1].getWorld().getName());
			yaml.set("p2.x", areaPoints[1].getBlockX());
			yaml.set("p2.y", areaPoints[1].getBlockY());
			yaml.set("p2.z", areaPoints[1].getBlockZ());
			yaml.set("rate", s.getRate());
			yaml.set("mobs", mobIDs);
			yaml.set("passiveMobs", passiveMobIDs);
			
			try {
				yaml.save(saveFile);
			} catch (IOException e) {
				e.printStackTrace();
				log.severe("Failed to save spawner " + String.valueOf(s.getId()) + "!");
			}
		}
		
		clearSpawners();
		log.info("Save complete!");	
	}
	
	//Load entities from file
	public void loadEntities() {
		log.info("Loading entities from directory " + getDataFolder().getPath() + File.separator + "Entities");
		File sDir = new File(getDataFolder() + File.separator + "Entities");
		if(!sDir.exists()) {
			sDir.mkdirs();
		}
		File[] sFiles = sDir.listFiles();
		log.info(String.valueOf(sFiles.length) + " total entities.");
		for(File f : sFiles) {
			if(f.getName().endsWith(".yml")) {
				log.info("Loading " + f.getName());
				FileConfiguration yaml = YamlConfiguration.loadConfiguration(f);
				
				int id = yaml.getInt("id");
				String name = yaml.getString("name");
				String strType = yaml.getString("type");
				List<?> rawEffects = yaml.getList("effects");
				double xVelocity = yaml.getDouble("xVelocity");
				double yVelocity = yaml.getDouble("yVelocity");
				double zVelocity = yaml.getDouble("zVelocity");
				int age = yaml.getInt("age");
				int health = yaml.getInt("health");
				int air = yaml.getInt("air");
				String strProfession = yaml.getString("profession");
				int endermanBlockId = yaml.getInt("endermanBlock");
				boolean isSaddled = yaml.getBoolean("saddled");
				boolean isCharged = yaml.getBoolean("charged");
				boolean isJockey = yaml.getBoolean("jockey");
				boolean isTamed = yaml.getBoolean("tame");
				boolean angry = yaml.getBoolean("angry");
				boolean isSitting = yaml.getBoolean("sitting"); 
				String catType = yaml.getString("catType");
				int slimeSize = yaml.getInt("slimeSize");
				String color = yaml.getString("color");
				boolean passive = yaml.getBoolean("passive"); 
				int fireTicks = yaml.getInt("fireTicks");
				List<?> blacklist = yaml.getList("blacklist");
				List<?> whitelist = yaml.getList("whitelist");
				List<?> itemlist = yaml.getList("itemlist");
				boolean useBlack = yaml.getBoolean("useBlacklist");
				boolean useWhite = yaml.getBoolean("useWhitelist");
				
				//PotionEffect handling
				ArrayList<EntityPotionEffect> effects = new ArrayList<EntityPotionEffect>();
				for(Object o : rawEffects) {
					effects.add((EntityPotionEffect) o);
				}
				
				ArrayList<String> blacklistStrings = new ArrayList<String>();
				for(Object o : blacklist) {
					if(o instanceof String) {
						blacklistStrings.add((String) o);
					}
				}
				
				ArrayList<String> whitelistStrings = new ArrayList<String>();
				for(Object o : whitelist) {
					if(o instanceof String) {
						whitelistStrings.add((String) o);
					}
				}
				
				ArrayList<Integer> itemlistInts = new ArrayList<Integer>();
				for(Object o : itemlist) {
					if(o instanceof Integer) {
						itemlistInts.add((Integer) o);
					}
				}
				
				EntityType type = EntityType.fromName(strType);
				
				Vector velocity = new Vector(xVelocity, yVelocity, zVelocity);
				
				Villager.Profession profession = Villager.Profession.valueOf(strProfession);
				
				MaterialData endermanBlock = new MaterialData(endermanBlockId);
				
				SpawnableEntity e = new SpawnableEntity(type, id);
				e.setName(name);
				e.setEffects(effects);
				e.setVelocity(velocity);
				e.setAge(age);
				e.setHealth(health);
				e.setAir(air);
				e.setProfession(profession);
				e.setEndermanBlock(endermanBlock);
				e.setSaddled(isSaddled);
				e.setCharged(isCharged);
				e.setJockey(isJockey);
				e.setTamed(isTamed);
				e.setAngry(angry);
				e.setSitting(isSitting);
				e.setCatType(catType);
				e.setSlimeSize(slimeSize);
				e.setColor(color);
				e.setPassive(passive);
				e.setFireTicks(fireTicks);
				e.setDamageBlacklist(blacklistStrings);
				e.setDamageWhitelist(whitelistStrings);
				e.setItemDamageList(itemlistInts);
				e.setUseBlacklist(useBlack);
				e.setUseWhitelist(useWhite);
				
				entities.put(id, e);
			}
		}
		
		log.info("Load Complete!");
	}
	
	//Reload entities from file
	public void reloadEntities() {
		saveEntities();
		loadEntities();
	}
	
	//Save entities to file
	public void saveEntities() {
		log.info("Saving entities...");
		log.info(String.valueOf(spawners.size()) + " entities to save.");
		Iterator<SpawnableEntity> entityItr = entities.values().iterator();
		
		while(entityItr.hasNext()) {
			SpawnableEntity e = entityItr.next();
			
			log.info("Saving entity " + String.valueOf(e.getId()) + " to " + getDataFolder() + File.separator + "Entities" + 
					File.separator + String.valueOf(e.getId()) + ".yml");
			File saveFile = new File(getDataFolder() + File.separator + "Entities" + File.separator + String.valueOf(e.getId()) + ".yml");
			FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);
			
			yaml.options().header("DO NOT MODIFY THIS FILE!");
			
			yaml.set("id", e.getId());
			yaml.set("name", e.getName());
			yaml.set("type", e.getType().getName());
			yaml.set("effects", e.getEffects());
			yaml.set("xVelocity", e.getXVelocity());
			yaml.set("yVelocity", e.getYVelocity());
			yaml.set("zVelocity", e.getZVelocity());
			yaml.set("age", e.getAge());
			yaml.set("health", e.getHealth());
			yaml.set("air", e.getAir());
			yaml.set("profession", e.getProfession().toString());
			yaml.set("endermanBlock", e.getEndermanBlock().toItemStack().getTypeId());
			yaml.set("saddled", e.isSaddled());
			yaml.set("charged", e.isCharged());
			yaml.set("jockey", e.isJockey());
			yaml.set("tame", e.isTamed());
			yaml.set("angry", e.isAngry());
			yaml.set("sitting", e.isSitting());
			yaml.set("catType", e.getCatType());
			yaml.set("slimeSize", e.getSlimeSize());
			yaml.set("color", e.getColor());
			yaml.set("passive", e.isPassive());
			yaml.set("fireTicks", e.getFireTicks());
			yaml.set("blacklist", e.getDamageBlacklist());
			yaml.set("whitelist", e.getDamageWhitelist());
			yaml.set("itemlist", e.getItemDamageList());
			yaml.set("useBlacklist", e.isUsingBlacklist());
			yaml.set("useWhitelist", e.isUsingWhitelist());
			
			try {
				yaml.save(saveFile);
			} catch (IOException ex) {
				ex.printStackTrace();
				log.severe("Failed to save entity " + String.valueOf(e.getId()) + "!");
			}
		}
		
		clearEntities();
		log.info("Save complete!");	
	}
	
	//Removes a spawner or entity's data file
	public void removeDataFile(int id, boolean isSpawner) {
		File file = null;
		
		if(isSpawner) {
			file = new File(getDataFolder() + File.pathSeparator + "Spawners" + File.pathSeparator + id + ".yml");
			if(!file.exists()) {
				return;
			}
			file.delete();
		} else {
			file = new File(getDataFolder() + File.pathSeparator + "Entities" + File.pathSeparator + id + ".yml");
			if(!file.exists()) {
				return;
			}
			file.delete();
		}
	}
	
	//Reloads
	public void reloadData() throws Exception {
		saveEntities();
		saveSpawners();
		loadEntities();
		loadSpawners();
	}
	
	//Clears the spawners list
	public void clearSpawners() {
		synchronized(this) {
			spawners.clear();
		}
	}
	
	//Clears the entities list
	public void clearEntities() {
		synchronized(this) {
			entities.clear();
		}
	}
	
	//Autosaves everything
	public synchronized void autosaveAll() {
		
		if(config.getBoolean("data.broadcastAutosave")) {
			getServer().broadcastMessage(ChatColor.GOLD + config.getString("data.broadcastMessage"));
		}
		
		Iterator<Spawner> spawnerItr = spawners.values().iterator();
		Iterator<SpawnableEntity> entityItr = entities.values().iterator();
		
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			
			autosave(s);
		}
		
		while(entityItr.hasNext()) {
			SpawnableEntity e = entityItr.next();
			
			autosave(e);
		}
		
		if(config.getBoolean("data.broadcastAutosave")) {
			getServer().broadcastMessage(ChatColor.GREEN + config.getString("data.broadcastMessageEnd"));
		}
		
	}
	
	//Autosaves a spawner
	public synchronized void autosave(Spawner s) {

		File saveFile = new File(getDataFolder() + File.separator + "Spawners" + File.separator + String.valueOf(s.getId()) + ".yml");
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);
		
		List<String> mobIDs = new ArrayList<String>();
		Iterator<Integer> mobItr = s.getMobs().keySet().iterator();
		while(mobItr.hasNext()) {
			int mobId = mobItr.next();
			int entityId = s.getMobs().get(mobId).getId();
			String toYaml = mobId + "_" + entityId;
			
			mobIDs.add(toYaml);
		}
		
		List<String> passiveMobIDs = new ArrayList<String>();
		Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
		while(passiveMobItr.hasNext()) {
			int passiveMobId = passiveMobItr.next();
			int entityId = s.getPassiveMobs().get(passiveMobId).getId();
			String toYaml = passiveMobId + "_" + entityId;
			
			passiveMobIDs.add(toYaml);
		}
		
		List<Integer> spawnableEntityIDs = new ArrayList<Integer>();
		for(Integer i : s.getTypeData().keySet()) {
			spawnableEntityIDs.add(i);
		}
		
		Location[] areaPoints = s.getAreaPoints();
		
		if(yaml.getList("mobs") == null) {
			yaml.set("mobs", null);
		}
		
		yaml.options().header("DO NOT MODIFY THIS FILE!");
		
		yaml.set("id", s.getId());
		yaml.set("name", s.getName());
		yaml.set("spawnableEntities", spawnableEntityIDs);
		yaml.set("active", s.isActive());
		yaml.set("hidden", s.isHidden());
		yaml.set("radius", s.getRadius());
		yaml.set("useSpawnArea", s.isUsingSpawnArea());
		yaml.set("redstone", s.isRedstoneTriggered());
		yaml.set("maxDistance", s.getMaxPlayerDistance());
		yaml.set("minDistance", s.getMinPlayerDistance());
		yaml.set("maxLight", s.getMaxLightLevel());
		yaml.set("minLight", s.getMinLightLevel());
		yaml.set("mobsPerSpawn", s.getMobsPerSpawn());
		yaml.set("maxMobs", s.getMaxMobs());
		yaml.set("location.world", s.getLoc().getWorld().getName());
		yaml.set("location.x", s.getLoc().getBlockX());
		yaml.set("location.y", s.getLoc().getBlockY());
		yaml.set("location.z", s.getLoc().getBlockZ());
		yaml.set("p1.world", areaPoints[0].getWorld().getName());
		yaml.set("p1.x", areaPoints[0].getBlockX());
		yaml.set("p1.y", areaPoints[0].getBlockY());
		yaml.set("p1.z", areaPoints[0].getBlockZ());
		yaml.set("p2.world", areaPoints[1].getWorld().getName());
		yaml.set("p2.x", areaPoints[1].getBlockX());
		yaml.set("p2.y", areaPoints[1].getBlockY());
		yaml.set("p2.z", areaPoints[1].getBlockZ());
		yaml.set("rate", s.getRate());
		yaml.set("mobs", mobIDs);
		yaml.set("passiveMobs", passiveMobIDs);
		
		try {
			yaml.save(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
			log.severe("Failed to save spawner " + String.valueOf(s.getId()) + "!");
		}
			
	}
	
	//Autosaves an entity
	public synchronized void autosave(SpawnableEntity e) {
		
		File saveFile = new File(getDataFolder() + File.separator + "Entities" + File.separator + String.valueOf(e.getId()) + ".yml");
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);
		
		yaml.options().header("DO NOT MODIFY THIS FILE!");
		
		yaml.set("id", e.getId());
		yaml.set("name", e.getName());
		yaml.set("type", e.getType().getName());
		yaml.set("effects", e.getEffects());
		yaml.set("xVelocity", e.getXVelocity());
		yaml.set("yVelocity", e.getYVelocity());
		yaml.set("zVelocity", e.getZVelocity());
		yaml.set("age", e.getAge());
		yaml.set("health", e.getHealth());
		yaml.set("air", e.getAir());
		yaml.set("profession", e.getProfession().toString());
		yaml.set("endermanBlock", e.getEndermanBlock().toItemStack().getTypeId());
		yaml.set("saddled", e.isSaddled());
		yaml.set("charged", e.isCharged());
		yaml.set("jockey", e.isJockey());
		yaml.set("tame", e.isTamed());
		yaml.set("angry", e.isAngry());
		yaml.set("sitting", e.isSitting());
		yaml.set("catType", e.getCatType());
		yaml.set("slimeSize", e.getSlimeSize());
		yaml.set("color", e.getColor());
		yaml.set("passive", e.isPassive());
		yaml.set("fireTicks", e.getFireTicks());
		yaml.set("blacklist", e.getDamageBlacklist());
		yaml.set("whitelist", e.getDamageWhitelist());
		yaml.set("itemlist", e.getItemDamageList());
		yaml.set("useBlacklist", e.isUsingBlacklist());
		yaml.set("useWhitelist", e.isUsingWhitelist());
		
		try {
			yaml.save(saveFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			log.severe("Failed to save entity " + String.valueOf(e.getId()) + "!");
		}
		
	}
	
	//Removes mobs spawned by a certain spawner
	public synchronized void removeMobs(final Spawner s) { //Called in the removemobs command
		Iterator<Integer> mobs = s.getMobs().keySet().iterator();
		Iterator<Integer> passiveMobs = s.getPassiveMobs().keySet().iterator();

		while(mobs.hasNext()) {
			int spawnerMobId = mobs.next();
			Iterator<LivingEntity> livingEntities = s.getLoc().getWorld().getLivingEntities().iterator();
			
			while(livingEntities.hasNext()) {
				LivingEntity l = livingEntities.next();
				
				int entityId = l.getEntityId();

				if(spawnerMobId == entityId) {
					if(l.getPassenger() != null) {
						l.getPassenger().remove();
					}
					l.remove();
					mobs.remove();
				}
				
			}
			
		}
		
		while(passiveMobs.hasNext()) {
			int spawnerMobId = passiveMobs.next();
			Iterator<LivingEntity> livingEntities = s.getLoc().getWorld().getLivingEntities().iterator();
			
			while(livingEntities.hasNext()) {
				LivingEntity l = livingEntities.next();
				
				int entityId = l.getEntityId();

				if(spawnerMobId == entityId) {
					if(l.getPassenger() != null) {
						l.getPassenger().remove();
					}
					l.remove();
					passiveMobs.remove();
				}
				
			}
			
		}
		
	}
	
	//Removes a spawner from a mob list when it dies
	public synchronized void removeMob(final Entity e, final ArrayList<Spawner> validSpawners) { //Called when an entity dies. l is the dead entity.
		int entityId = e.getEntityId();
		Iterator<Spawner> spawnerItr = validSpawners.iterator();

		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			
			Iterator<Integer> mobs = s.getMobs().keySet().iterator();
			Iterator<Integer> passiveMobs = s.getPassiveMobs().keySet().iterator();

			while(mobs.hasNext()) {
				int spawnerMobId = mobs.next();
				
				if(spawnerMobId == entityId) {
					mobs.remove();
				}

			}
			
			while(passiveMobs.hasNext()) {
				int spawnerMobId = passiveMobs.next();
				
				if(spawnerMobId == entityId) {
					passiveMobs.remove();
				}

			}
			
		}
		
	}
	
}
