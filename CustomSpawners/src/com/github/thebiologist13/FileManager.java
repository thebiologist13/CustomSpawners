package com.github.thebiologist13;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class FileManager {
	
	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	private FileConfiguration config = null;
	
	public FileManager(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
		this.config = plugin.getCustomConfig();
	}
	
	//Loads spawners from file
	public void loadSpawners() {
		log.info("Loading spawners from directory " + plugin.getDataFolder().getPath() + File.separator + "Spawners");
		File sDir = new File(plugin.getDataFolder() + File.separator + "Spawners");
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
				String name = yaml.getString("name", "");
				double radius = yaml.getDouble("radius", config.getDouble("spawners.radius", 32));
				boolean redstoneTriggered = yaml.getBoolean("redstone",config.getBoolean("spawners.redstoneTriggered", false));
				int maxPlayerDistance = yaml.getInt("maxDistance", config.getInt("spawners.maxPlayerDistance", 32));
				int minPlayerDistance = yaml.getInt("minDistance", config.getInt("spawners.minPlayerDistance", 0));
				boolean active = yaml.getBoolean("active", config.getBoolean("spawners.hidden", false));
				byte maxLightLevel = (byte) yaml.getInt("maxLight", (byte) config.getInt("spawners.maxLightLevel", 7));
				byte minLightLevel = (byte) yaml.getInt("minLight", (byte) config.getInt("spawners.minLightLevel", 0));
				boolean hidden = yaml.getBoolean("hidden", config.getBoolean("spawners.hidden", false));
				int mobsPerSpawn = yaml.getInt("mobsPerSpawn", config.getInt("spawners.mobsPerSpawn", 2)); 
				int maxMobs = yaml.getInt("maxMobs", config.getInt("spawners.maxMobs", 64)); 
				List<?> mobs = yaml.getList("mobs"); 
				List<?> passiveMobs = yaml.getList("passiveMobs");
				int rate = yaml.getInt("rate", config.getInt("spawners.rate", 120));
				boolean usingSpawnArea = yaml.getBoolean("useSpawnArea", false);
				String locWorld = yaml.getString("location.world");
				int locX = yaml.getInt("location.x", 0);
				int locY = yaml.getInt("location.y", 0);
				int locZ = yaml.getInt("location.z", 0);
				String p1World = yaml.getString("p1.world");
				int p1x = yaml.getInt("p1.x", 0);
				int p1y = yaml.getInt("p1.y", 0);
				int p1z = yaml.getInt("p1.z", 0);
				String p2World = yaml.getString("p2.world");
				int p2x = yaml.getInt("p2.x", 0);
				int p2y = yaml.getInt("p2.y", 0);
				int p2z = yaml.getInt("p2.z", 0);

				//Make sure no values are null 
				if(Integer.valueOf(id).equals(null)) {
					log.info("Cannot load ID from spawner! Please check that " + f.getName() + " has a valid ID.");
					return;
				}
				if(locWorld.equals(null)) {
					log.info("Cannot load world that spawner is from file " + f.getName() + "! Using default world. " + plugin.getServer().getWorlds().get(0).getName());
					locWorld = plugin.getServer().getWorlds().get(0).getName();
				}
				if(p1World.equals(null) || p2World.equals(null)) {
					log.info("Cannot load spawn area world locations in file " + f.getName() + "! Using default world. " + plugin.getServer().getWorlds().get(0).getName());
					locWorld = plugin.getServer().getWorlds().get(0).getName();
				}

				//Convert Raw yaml list of mobs to ArrayList<Integer>
				Iterator<?> mobItr = mobs.iterator();
				Iterator<?> passiveMobItr = passiveMobs.iterator();
				ConcurrentHashMap<Integer, SpawnableEntity> mobsMap = new ConcurrentHashMap<Integer, SpawnableEntity>();
				ConcurrentHashMap<Integer, SpawnableEntity> passiveMobsMap = new ConcurrentHashMap<Integer, SpawnableEntity>();

				while(mobItr.hasNext()) {
					Object o = mobItr.next();

					if(o instanceof String) {
						String pair = (String) o;
						int key = Integer.parseInt(pair.substring(0, pair.indexOf("_")));
						int value = Integer.parseInt(pair.substring(pair.indexOf("_") + 1, pair.length()));

						SpawnableEntity e = plugin.getEntity(String.valueOf(value));

						mobsMap.put(key, e);
					}
				}

				while(passiveMobItr.hasNext()) {
					Object o = passiveMobItr.next();

					if(o instanceof String) {
						String pair = (String) o;
						int key = Integer.parseInt(pair.substring(0, pair.indexOf("_")));
						int value = Integer.parseInt(pair.substring(pair.indexOf("_") + 1, pair.length()));

						SpawnableEntity e = plugin.getEntity(String.valueOf(value));

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
				Location loc = new Location(plugin.getServer().getWorld(locWorld), locX, locY, locZ);

				//Spawn Area Points
				Location[] areaPoints = new Location[2];

				//Point 1
				Location p1 = new Location(plugin.getServer().getWorld(p1World), p1x, p1y, p1z);

				//Point 2
				Location p2 = new Location(plugin.getServer().getWorld(p2World), p2x, p2y, p2z);

				areaPoints[0] = p1;
				areaPoints[1] = p2;

				//Mob types
				List<?> listType = yaml.getList("spawnableEntities");
				LinkedHashMap<Integer, SpawnableEntity> entities = new LinkedHashMap<Integer, SpawnableEntity>();
				for(Object o : listType) {
					if(o instanceof Integer) {
						int entityID = (Integer) o;
						SpawnableEntity se = plugin.getEntity(String.valueOf(entityID));
						entities.put(entityID, se);
					}
				}

				SpawnableEntity baseEntity = null;
				for(SpawnableEntity e : entities.values()) {
					baseEntity = e;
					break;
				}

				Spawner s = new Spawner(baseEntity, loc, name, id, plugin.getServer());
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

				CustomSpawners.spawners.put(id, s);
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
		log.info(String.valueOf(CustomSpawners.spawners.size()) + " spawners to save.");
		boolean killOnReload = config.getBoolean("spawners.killOnReload", false);
		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();

		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();

			log.info("Saving spawner " + String.valueOf(s.getId()) + " to " + plugin.getDataFolder() + File.separator + "Spawners" + 
					File.separator + String.valueOf(s.getId()) + ".yml");
			File saveFile = new File(plugin.getDataFolder() + File.separator + "Spawners" + File.separator + String.valueOf(s.getId()) + ".yml");
			FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);

			ConcurrentHashMap<Integer, SpawnableEntity> allMobs = s.getMobs();
			allMobs.putAll(s.getPassiveMobs());
			if(killOnReload) {
				for(Integer e : allMobs.keySet()) {
					List<LivingEntity> entities = plugin.getServer().getWorld(s.getLoc().getWorld().getName()).getLivingEntities();
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
		log.info("Loading entities from directory " + plugin.getDataFolder().getPath() + File.separator + "Entities");
		File sDir = new File(plugin.getDataFolder() + File.separator + "Entities");
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
				String name = yaml.getString("name", "");
				String strType = yaml.getString("type");
				List<?> rawEffects = yaml.getList("effects");
				double xVelocity = yaml.getDouble("xVelocity", 0);
				double yVelocity = yaml.getDouble("yVelocity", 0);
				double zVelocity = yaml.getDouble("zVelocity", 0);
				int age = yaml.getInt("age", -1);
				int health = yaml.getInt("health", -1);
				int air = yaml.getInt("air", -1);
				String strProfession = yaml.getString("profession", config.getString("entities.profession", "FARMER"));
				int endermanBlockId = yaml.getInt("endermanBlock", config.getInt("entities.endermanBlock", 2));
				boolean isSaddled = yaml.getBoolean("saddled", config.getBoolean("entities.isSaddled", false));
				boolean isCharged = yaml.getBoolean("charged", config.getBoolean("entities.isCharged", false));
				boolean isJockey = yaml.getBoolean("jockey", config.getBoolean("entities.isJockey", false));
				boolean isTamed = yaml.getBoolean("tame", config.getBoolean("entities.isTamed", false));
				boolean angry = yaml.getBoolean("angry", config.getBoolean("entities.isAngry", false));
				boolean isSitting = yaml.getBoolean("sitting", config.getBoolean("entities.isSitting", false)); 
				String catType = yaml.getString("catType", config.getString("entities.catType", "WILD_OCELOT"));
				int slimeSize = yaml.getInt("slimeSize", config.getInt("entities.slimeSize", 1));
				String color = yaml.getString("color", config.getString("entities.color", "WHITE"));
				boolean passive = yaml.getBoolean("passive", config.getBoolean("entities.passive", false)); 
				int fireTicks = yaml.getInt("fireTicks", 0);
				List<?> blacklist = yaml.getList("blacklist");
				List<?> whitelist = yaml.getList("whitelist");
				List<?> itemlist = yaml.getList("itemlist");
				boolean useBlack = yaml.getBoolean("useBlacklist", config.getBoolean("entities.useBlacklist", true));
				boolean useWhite = yaml.getBoolean("useWhitelist", !config.getBoolean("entities.useBlacklist", true));

				//Make sure no values are null 
				if(Integer.valueOf(id).equals(null)) {
					log.info("Cannot load ID from entity! Please check that " + f.getName() + " has a valid ID.");
					return;
				}
				if(strType.equals(null)) {
					log.info("Cannot load type of entity in file " + f.getName() + "! Using default world. ");
				}
				
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

				CustomSpawners.entities.put(id, e);
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
		log.info(String.valueOf(CustomSpawners.entities.size()) + " entities to save.");
		Iterator<SpawnableEntity> entityItr = CustomSpawners.entities.values().iterator();

		while(entityItr.hasNext()) {
			SpawnableEntity e = entityItr.next();

			log.info("Saving entity " + String.valueOf(e.getId()) + " to " + plugin.getDataFolder() + File.separator + "Entities" + 
					File.separator + String.valueOf(e.getId()) + ".yml");
			File saveFile = new File(plugin.getDataFolder() + File.separator + "Entities" + File.separator + String.valueOf(e.getId()) + ".yml");
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
			file = new File(plugin.getDataFolder() + File.pathSeparator + "Spawners" + File.pathSeparator + id + ".yml");
			if(!file.exists()) {
				return;
			}
			file.delete();
		} else {
			file = new File(plugin.getDataFolder() + File.pathSeparator + "Entities" + File.pathSeparator + id + ".yml");
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
			CustomSpawners.spawners.clear();
		}
	}

	//Clears the entities list
	public void clearEntities() {
		synchronized(this) {
			CustomSpawners.entities.clear();
		}
	}

	//Autosaves everything
	public synchronized void autosaveAll() {

		if(config.getBoolean("data.broadcastAutosave")) {
			plugin.getServer().broadcastMessage(ChatColor.GOLD + config.getString("data.broadcastMessage"));
		}

		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();
		Iterator<SpawnableEntity> entityItr = CustomSpawners.entities.values().iterator();

		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();

			autosave(s);
		}

		while(entityItr.hasNext()) {
			SpawnableEntity e = entityItr.next();

			autosave(e);
		}

		if(config.getBoolean("data.broadcastAutosave")) {
			plugin.getServer().broadcastMessage(ChatColor.GREEN + config.getString("data.broadcastMessageEnd"));
		}

	}

	//Autosaves a spawner
	public synchronized void autosave(Spawner s) {

		File saveFile = new File(plugin.getDataFolder() + File.separator + "Spawners" + File.separator + String.valueOf(s.getId()) + ".yml");
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

		File saveFile = new File(plugin.getDataFolder() + File.separator + "Entities" + File.separator + String.valueOf(e.getId()) + ".yml");
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
}
