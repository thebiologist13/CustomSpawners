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
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class FileManager {
	
	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	private FileConfiguration config = null;
	
	private final int logLevel;
	
	private final String SPAWNER_PATH = plugin.getDataFolder() + File.separator + "Spawners";
	
	private final String ENTITY_PATH = plugin.getDataFolder() + File.separator + "Entities";
	
	private final String ch = File.separator;
	
	public FileManager(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
		this.config = plugin.getCustomConfig();
		this.logLevel = config.getInt("data.logLevel", 2);
	}
	
	//Loads spawners from file
	public void loadSpawners() {
		
		if(logLevel > 0)
			log.info("Loading spawners from directory " + SPAWNER_PATH);
		
		File sDir = new File(SPAWNER_PATH);
		if(!sDir.exists()) {
			sDir.mkdirs();
		}
		File[] sFiles = sDir.listFiles();
		
		if(logLevel > 1) 
			log.info(String.valueOf(sFiles.length) + " total spawners.");
		
		for(File f : sFiles) {
			
			Spawner s = loadSpawner(f);
			CustomSpawners.spawners.put(s.getId(), s);
			
		}

		if(logLevel > 0)
			log.info("Load Complete!");
	}

	//Saves then loads spawners from file
	public void reloadSpawners() {
		saveSpawners();
		loadSpawners();
	}

	//Saves spawners to file
	public void saveSpawners() {
		if(logLevel > 0)
			log.info("Saving spawners...");
		
		if(logLevel > 1)
			log.info(String.valueOf(CustomSpawners.spawners.size()) + " spawners to save.");
		
		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();

		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			boolean killOnReload = config.getBoolean("spawners.killOnReload", false);
			ConcurrentHashMap<Integer, SpawnableEntity> allMobs = s.getMobs();
			allMobs.putAll(s.getPassiveMobs());
			
			if(logLevel > 1)
				log.info("Saving spawner " + String.valueOf(s.getId()) + " to " + SPAWNER_PATH + ch + String.valueOf(s.getId()) + ".yml");
			
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
			
			File saveFile = new File(SPAWNER_PATH + ch + String.valueOf(s.getId()) + ".yml");
			
			saveSpawner(s, saveFile);
			
		}

		clearSpawners();
		
		if(logLevel > 0)
			log.info("Save complete!");	
	}

	//Load entities from file
	public void loadEntities() {
		if(logLevel > 0)
			log.info("Loading entities from directory " + ENTITY_PATH + ch + "Entities");
		
		File sDir = new File(ENTITY_PATH);
		if(!sDir.exists()) {
			sDir.mkdirs();
		}
		File[] sFiles = sDir.listFiles();
		
		if(logLevel > 1)
			log.info(String.valueOf(sFiles.length) + " total entities.");
		
		for(File f : sFiles) {
			
			SpawnableEntity e = loadEntity(f);
			CustomSpawners.entities.put(e.getId(), e);
			
		}

		if(logLevel > 0)
			log.info("Load Complete!");
	}

	//Reload entities from file
	public void reloadEntities() {
		saveEntities();
		loadEntities();
	}

	//Save entities to file
	public void saveEntities() {
		if(logLevel > 0)
			log.info("Saving entities...");
		
		if(logLevel > 1)
			log.info(String.valueOf(CustomSpawners.entities.size()) + " entities to save.");
		
		Iterator<SpawnableEntity> entityItr = CustomSpawners.entities.values().iterator();

		while(entityItr.hasNext()) {
			SpawnableEntity e = entityItr.next();

			if(logLevel > 1)
				log.info("Saving entity " + String.valueOf(e.getId()) + " to " + ENTITY_PATH + ch + String.valueOf(e.getId()) + ".yml");
			
			File saveFile = new File(ENTITY_PATH + ch + String.valueOf(e.getId()) + ".yml");
			saveEntity(e, saveFile);
			
		}

		clearEntities();
		
		if(logLevel > 0)
			log.info("Save complete!");	
	}

	//Removes a spawner or entity's data file
	public void removeDataFile(int id, boolean isSpawner) {
		File file = null;

		if(isSpawner) {
			file = new File(SPAWNER_PATH + ch + id + ".yml");
			if(!file.exists()) {
				plugin.printDebugMessage("Spawner File Does Not Exist. Path => " + SPAWNER_PATH + ch + id + ".yml");
				return;
			}
			file.delete();
		} else {
			file = new File(ENTITY_PATH + ch + id + ".yml");
			if(!file.exists()) {
				plugin.printDebugMessage("Entity File Does Not Exist. Path => " + ENTITY_PATH + ch + id + ".yml");
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
			plugin.getServer().broadcastMessage(ChatColor.GOLD + config.getString("data.broadcastMessage", ""));
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
			plugin.getServer().broadcastMessage(ChatColor.GREEN + config.getString("data.broadcastMessageEnd", ""));
		}

	}

	//Autosaves a spawner
	public synchronized void autosave(Spawner s) {

		File saveFile = new File(SPAWNER_PATH + ch + String.valueOf(s.getId()) + ".yml");
		saveSpawner(s, saveFile);

	}

	//Autosaves an entity
	public synchronized void autosave(SpawnableEntity e) {

		File saveFile = new File(ENTITY_PATH + ch + String.valueOf(e.getId()) + ".yml");
		saveEntity(e, saveFile);

	}
	
	//Loads a Spawner from a YAML file
	public Spawner loadSpawner(File f) {
		
		if(f.getName().endsWith(".yml")) {
			
			if(logLevel > 1) 
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
			boolean converted = yaml.getBoolean("converted", false);
			Block block = null;
			int blockID = 0;
			byte blockData = 0;

			//Make sure no values are null 
			if(Integer.valueOf(id) == null) {
				log.info("Cannot load ID from spawner! Please check that " + f.getName() + " has a valid ID.");
				return null;
			}
			if(locWorld == null) {
				log.info("Cannot load world that spawner is from file " + f.getName() + "! Using default world. " + plugin.getServer().getWorlds().get(0).getName());
				locWorld = plugin.getServer().getWorlds().get(0).getName();
			}
			if(p1World == null || p2World == null) {
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
			
			block = loc.getWorld().getBlockAt(loc);
			String blockType = yaml.getString("block", "49-0");
			int dashIndex = blockType.indexOf("-");
			
			try {
				blockID = Integer.parseInt(blockType.substring(0, dashIndex));
				blockData = Byte.parseByte(blockType.substring(dashIndex + 1, blockType.length()));
			} catch(NumberFormatException e) {
				log.info("Error loading spawner block type. Check that it is in the format <block id>-<block data>. Using obsidian as default.");
				blockID = 49;
				blockData = 0;
			}
			
			block.setTypeIdAndData(blockID, blockData, false);
			

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
			s.setBlock(block);
			s.setConverted(converted);

			return s;
		}
		
		return null;
		
	}
	
	//Loads a SpawnableEntity from a YAML file
	public SpawnableEntity loadEntity(File f) {
		
		if(f.getName().endsWith(".yml")) {
			
			if(logLevel > 1)
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
			List<?> blacklist = yaml.getList("blacklist", new ArrayList<String>());
			List<?> whitelist = yaml.getList("whitelist", new ArrayList<String>());
			List<?> itemlist = yaml.getList("itemlist", new ArrayList<ItemStack>());
			boolean useBlack = yaml.getBoolean("useBlacklist", config.getBoolean("entities.useBlacklist", true));
			boolean useWhite = yaml.getBoolean("useWhitelist", !config.getBoolean("entities.useBlacklist", true));
			List<?> dropList = yaml.getList("drops", new ArrayList<ItemStack>());
			
			boolean usingCustomDamage = yaml.getBoolean("useCustomDamage", config.getBoolean("entities.useCustomDamage", false));
			int damage = yaml.getInt("damage", config.getInt("entities.dealtDamage", 2));
			EntityPotionEffect effectType = (EntityPotionEffect) yaml.get("potionType", plugin.getPotion(config.getString("entities.potionType", "REGENERATION_1_0:0")));
			int droppedExp = yaml.getInt("droppedExp", config.getInt("entities.experienceDropped", 1));
			int fuseTicks = yaml.getInt("fuseTicks", config.getInt("entities.fuseTicks", 80));
			float yield = (float) yaml.getDouble("yield", config.getDouble("entities.yield", 5.0d));
			boolean incendiary = yaml.getBoolean("incendiary", config.getBoolean("entities.incendiary", false));
			ItemStack itemType = yaml.getItemStack("itemType", plugin.getItemStack(config.getString("itemType", "1:0")));
			boolean usingCustomDrops = yaml.getBoolean("useCustomDrops", config.getBoolean("entities.useCustomDrops", false));

			//Make sure no values are null 
			if(Integer.valueOf(id) == null) {
				log.info("Cannot load ID from entity! Please check that " + f.getName() + " has a valid ID.");
				return null;
			}
			
			if(strType == null) {
				log.info("Cannot load type of entity in file " + f.getName() + "! Using default type. ");
				strType = "PIG";
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
			
			ArrayList<ItemStack> itemlistStacks = new ArrayList<ItemStack>();
			for(Object o : itemlist) {
				if(o instanceof ItemStack) {
					itemlistStacks.add((ItemStack) o);
				}
			}
			
			ArrayList<ItemStack> dropListStacks = new ArrayList<ItemStack>();
			for(Object o : dropList) {
				if(o instanceof ItemStack) {
					dropListStacks.add((ItemStack) o);
				}
			}

			EntityType type = plugin.parseEntityType(strType);

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
			e.setItemDamageList(itemlistStacks);
			e.setUseBlacklist(useBlack);
			e.setUseWhitelist(useWhite);
			e.setDrops(dropListStacks);
			e.setUsingCustomDamage(usingCustomDamage);
			e.setDamage(damage);
			e.setPotionEffect(effectType);
			e.setDroppedExp(droppedExp);
			e.setFuseTicks(fuseTicks);
			e.setYield(yield);
			e.setIncendiary(incendiary);
			e.setItemType(itemType);
			e.setUsingCustomDrops(usingCustomDrops);

			return e;
		}
		
		return null;
		
	}
	
	//Saves a Spawner to a YAML file
	public void saveSpawner(Spawner s, File f) {
		
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(f);

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
		yaml.set("block", s.getBlock().getTypeId() + "-" + s.getBlock().getData());
		yaml.set("converted", s.isConverted());

		try {
			yaml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
			log.severe("Failed to save spawner " + String.valueOf(s.getId()) + "!");
		}
		
	}
	
	//Saves a SpawnableEntity to YAML file
	public void saveEntity(SpawnableEntity e, File f) {
		
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(f);

		yaml.options().header("DO NOT MODIFY THIS FILE!");

		yaml.set("id", e.getId());
		yaml.set("name", e.getName());
		yaml.set("type", plugin.parseEntityName(e.getType()));
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
		yaml.set("useCustomDamage", e.isUsingCustomDamage());
		yaml.set("damage", e.getDamage());
		yaml.set("potionType", e.getPotionEffect());
		yaml.set("droppedExp", e.getDroppedExp());
		yaml.set("fuseTicks", e.getFuseTicks());
		yaml.set("yield", e.getYield());
		yaml.set("incendiary", e.isIncendiary());
		yaml.set("itemType", e.getItemType());
		yaml.set("useCustomDrops", e.isUsingCustomDrops());
		yaml.set("drops", e.getDrops());

		try {
			yaml.save(f);
		} catch (IOException ex) {
			ex.printStackTrace();
			log.severe("Failed to save entity " + String.valueOf(e.getId()) + "!");
		}
		
	}
	
}
