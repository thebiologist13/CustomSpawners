package com.github.thebiologist13;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.thebiologist13.listeners.MobDeathEvent;
import com.github.thebiologist13.listeners.PlayerLogoutEvent;

public class CustomSpawners extends JavaPlugin {
	
	//Logger
	public Logger log = Logger.getLogger("Minecraft");
	
	//All the spawners in the server.
	public static ArrayList<Spawner> spawners = new ArrayList<Spawner>();
	
	//All of the entity types on the server
	public static ArrayList<SpawnableEntity> entities = new ArrayList<SpawnableEntity>();
	
	//Selected spawners for players
	public static HashMap<Player, Integer> spawnerSelection = new HashMap<Player, Integer>();
	
	//Selected entities for players
	public static HashMap<Player, Integer> entitySelection = new HashMap<Player, Integer>();
	
	//YAML variable
	private FileConfiguration config;
	
	//YAML file variable
	private File configFile;
	
	
	
	public void onEnable() {
		
		//Config
		config = getCustomConfig();
		
		//Commands
		SpawnerExecutor se = new SpawnerExecutor(this);
		getCommand("customspawner").setExecutor(se);
		
		//Listeners
		getServer().getPluginManager().registerEvents(new PlayerLogoutEvent(), this);
		getServer().getPluginManager().registerEvents(new MobDeathEvent(), this);
		
		//Load spawners from files
		loadSpawners();
		
		//Spawning Task
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			public void run() {
				for(int i = 0; i < spawners.size(); i++) {
					if(spawners.get(i).getId() == -1) {
						if(spawnerSelection.containsValue(spawners.get(i).getId())) {
							resetSpawnerSelections(spawners.get(i).getId());
						}
						spawners.remove(i);
						continue;
					}
					spawners.get(i).tick();
				}
				
				for(int i = 0; i < entities.size(); i++) {
					if(entities.get(i).getId() == -1) {
						if(entitySelection.containsValue(entities.get(i).getId())) {
							resetEntitySelections(entities.get(i).getId());
						}
						spawners.remove(i);
						continue;
					}
				}
			}
			
		}, 20, 1);
		
		//Enable message
		log.info("CustomSpawners by thebiologist13 has been enabled!");
	}
	
	public void onDisable() {
		
		//Saving spawners
		saveSpawners();
		
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
	
	//Gets a spawner by ID number
	public Spawner getSpawnerById(int id) {
		for(Spawner s : spawners) {
			if(s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	//Checks if an spawner ID is valid
	public boolean isValidSpawner(int id) {
		for(Spawner s : spawners) {
			if(s.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	//Gets all spawners as a array
	public Spawner[] getSpawners() {
		return (Spawner[]) spawners.toArray();
	}
	
	//Gets a SpawnableEntity by ID number
	public SpawnableEntity getEntityById(int id) {
		for(SpawnableEntity e : entities) {
			if(e.getId() == id) {
				return e;
			}
		}
		return null;
	}
	
	//Checks if an spawner ID is valid
	public boolean isValidEntity(int id) {
		for(SpawnableEntity e : entities) {
			if(e.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	//Gets all spawners as a array
	public SpawnableEntity[] getEntities() {
		return (SpawnableEntity[]) entities.toArray();
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
	@SuppressWarnings("unused")
	public void loadSpawners() {
		log.info("Loading spawners from directory " + getDataFolder().getPath() + "\\Spawners");
		File sDir = new File(getDataFolder() + "\\Spawners");
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
				int maxPlayerDistance = yaml.getInt("maxPlayer");
				int minPlayerDistance = yaml.getInt("minPlayer");
				boolean active = yaml.getBoolean("active");
				byte maxLightLevel = (byte) yaml.getInt("maxLight");
				byte minLightLevel = (byte) yaml.getInt("minLight");
				boolean hidden = yaml.getBoolean("hidden");
				int mobsPerSpawn = yaml.getInt("mobsPerSpawn"); 
				int maxMobs = yaml.getInt("maxMobs"); 
				List<?> mobIds = yaml.getList("mobs"); 
				int rate = yaml.getInt("rate");
				
				//Location
				String locWorld = yaml.getString("location.world");
				int locX = yaml.getInt("location.x");
				int locY = yaml.getInt("location.y");
				int locZ = yaml.getInt("location.z");
				Location loc = new Location(getServer().getWorld(locWorld), locX, locY, locZ);
				
				//Mob type
				List<?> listType = yaml.getList("type");
				
				//TODO Set Props Here
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
		
		for(Spawner s : spawners) {
			log.info("Saving spawner " + String.valueOf(s.getId()) + " to " + getDataFolder() + "\\Spawners\\" + String.valueOf(s.getId()) + ".yml");
			File saveFile = new File(getDataFolder() + "\\Spawners\\" + String.valueOf(s.getId()) + ".yml");
			FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);
			
			if(killOnReload) {
				for(Integer e : s.getMobs()) {
					List<LivingEntity> entities = getServer().getWorld(s.getLoc().getWorld().getName()).getLivingEntities();
					for(LivingEntity le : entities) {
						if(le.getEntityId() == e) {
							le.remove();
						}
					}
				}
				s.getMobs().clear();
			}
			
			List<Integer> mobIDs = new ArrayList<Integer>();
			for(Integer e : s.getMobs()) {
				mobIDs.add(e);
			}
			
			List<Integer> spawnableEntityIDs = new ArrayList<Integer>();
			for(Integer i : s.getTypeData().keySet()) {
				spawnableEntityIDs.add(i);
			}
			
			Location[] areaPoints = s.getAreaPoints();
			
			yaml.options().header("DO NOT MODIFY THIS FILE!");
			
			yaml.set("id", s.getId());
			yaml.set("name", s.getName());
			yaml.set("spawnableEntities", spawnableEntityIDs);
			yaml.set("active", s.isActive());
			yaml.set("hidden", s.isHidden());
			yaml.set("radius", s.getRadius());
			yaml.set("useSpawnArea", s.isUsingSpawnArea());
			yaml.set("redstoneTriggered", s.isRedstoneTriggered());
			yaml.set("maxDistance", s.getMaxPlayerDistance());
			yaml.set("minDistance", s.getMinPlayerDistance());
			yaml.set("maxLight", s.getMaxLightLevel());
			yaml.set("minLight", s.getMinLightLevel());
			yaml.set("mobsPerSpawn", s.getMobsPerSpawn());
			yaml.set("maxMobs", s.getMaxMobs());
			yaml.set("spawnerX", s.getLoc().getBlockX());
			yaml.set("spawnerY", s.getLoc().getBlockY());
			yaml.set("spawnerZ", s.getLoc().getBlockZ());
			yaml.set("p1x", areaPoints[0].getBlockX());
			yaml.set("p1y", areaPoints[0].getBlockY());
			yaml.set("p1z", areaPoints[0].getBlockZ());
			yaml.set("p2x", areaPoints[1].getBlockX());
			yaml.set("p2y", areaPoints[1].getBlockY());
			yaml.set("p2z", areaPoints[1].getBlockZ());
			yaml.set("rate", s.getRate());
			yaml.set("mobs", mobIDs);
			
			try {
				yaml.save(saveFile);
			} catch (IOException e) {
				e.printStackTrace();
				log.severe("Failed to save spawner " + String.valueOf(s.getId()) + "!");
			}
		}
		
		log.info("Save complete!");
	}
	
	//Load entities from file
	@SuppressWarnings("unused")
	public void loadEntities() {
		log.info("Loading entities from directory " + getDataFolder().getPath() + "\\Entities");
		File sDir = new File(getDataFolder() + "\\Entities");
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
				String type = yaml.getString("type"); //TODO remember spider jockey's special case
				List<?> effects = yaml.getList("effects"); //TODO also special
				double xVelocity = yaml.getDouble("xVelocity"); //TODO velocities are ALSO special
				double yVelocity = yaml.getDouble("yVelocity");
				double zVelocity = yaml.getDouble("zVelocity");
				int age = yaml.getInt("age");
				int health = yaml.getInt("health");
				int air = yaml.getInt("air");
				String profession = yaml.getString("profession"); //TODO Another special case
				String endermanBlock = yaml.getString("endermanBlock"); //TODO Another special case
				boolean isSaddled = yaml.getBoolean("saddled");
				boolean isCharged = yaml.getBoolean("charged");
				boolean isJockey = yaml.getBoolean("jockey");
				boolean isTamed = yaml.getBoolean("tame");
				boolean angryWolf = yaml.getBoolean("angry");
				boolean isSitting = yaml.getBoolean("sitting"); 
				String catType = yaml.getString("catType"); //TODO Another special case
				int slimeSize = yaml.getInt("slimeSize");
				String color = yaml.getString("color"); //TODO Another special case
				
				//TODO Set Props Here
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
		
		for(SpawnableEntity e : entities) {
			log.info("Saving entity " + String.valueOf(e.getId()) + " to " + getDataFolder() + "\\Entities\\" + String.valueOf(e.getId()) + ".yml");
			File saveFile = new File(getDataFolder() + "\\Spawners\\" + String.valueOf(e.getId()) + ".yml");
			FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);
			
			yaml.options().header("DO NOT MODIFY THIS FILE!");
			
			yaml.set("id", e.getId());
			yaml.set("name", e.getName());
			yaml.set("type", e.getType());
			yaml.set("effects", e.getEffects());
			yaml.set("xVelocity", e.getXVelocity());
			yaml.set("yVelocity", e.getYVelocity());
			yaml.set("zVelocity", e.getZVelocity());
			yaml.set("age", e.getAge());
			yaml.set("health", e.getHealth());
			yaml.set("air", e.getAir());
			yaml.set("profession", e.getProfession().toString());
			yaml.set("endermanBlock", e.getEndermanBlock().toString());
			yaml.set("saddled", e.isSaddled());
			yaml.set("charged", e.isCharged());
			yaml.set("jockey", e.isJockey());
			yaml.set("tame", e.isTamed());
			yaml.set("angry", e.isAngryWolf());
			yaml.set("sitting", e.isSitting());
			yaml.set("catType", e.getCatType());
			yaml.set("slimeSize", e.getSlimeSize());
			yaml.set("color", e.getColor());
			
			try {
				yaml.save(saveFile);
			} catch (IOException ex) {
				ex.printStackTrace();
				log.severe("Failed to save entity " + String.valueOf(e.getId()) + "!");
			}
		}
		
		log.info("Save complete!");
	}
}
