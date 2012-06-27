package com.github.thebiologist13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.thebiologist13.listeners.MobDeathEvent;
import com.github.thebiologist13.listeners.PlayerLogoutEvent;

public class CustomSpawners extends JavaPlugin {
	
	//YAML variable
	private FileConfiguration config;
	
	//YAML file variable
	private File configFile;
	
	//Logger
	public Logger log = Logger.getLogger("Minecraft");
	
	//All the spawners in the server.
	public ArrayList<Spawner> spawners = new ArrayList<Spawner>();
	
	//Selected spawners for players
	public HashMap<Player, Integer> selection = new HashMap<Player, Integer>();
	
	public void onEnable() {
		
		//Config
		config = this.getCustomConfig();
		config.options().copyDefaults(true);
		saveCustomConfig();
		
		//Commands
		SpawnerExecutor se = new SpawnerExecutor(this);
		getCommand("customspawner").setExecutor(se);
		
		//Listeners
		getServer().getPluginManager().registerEvents(new PlayerLogoutEvent(this), this);
		getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
		
		//Load spawners from files
		loadSpawners();
		
		//Spawning Task
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			public void run() {
				for(int i = 0; i < spawners.size(); i++) {
					if(spawners.get(i).id == -1) {
						if(selection.containsValue(spawners.get(i).id)) {
							resetPlayerSelections(spawners.get(i).id);
						}
						spawners.remove(i);
						continue;
					}
					spawners.get(i).tick();
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
	
	//Config stuff. Credit for this goes to respective owners. I found it here: http://wiki.bukkit.org/Introduction_to_the_New_Configuration
	public void reloadCustomConfig() {
	    if (configFile == null) {
	    	configFile = new File(getDataFolder(), "config.yml");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = getResource("config.yml");
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
	        log.severe("Could not save config!");
	    }
	}
	
	//Gets a spawner by ID number
	public Spawner getSpawnerById(int id) {
		for(Spawner s : spawners) {
			if(s.id == id) {
				return s;
			}
		}
		return null;
	}
	
	//Checks if an ID is valid
	public boolean isValidId(int id) {
		for(Spawner s : spawners) {
			if(s.id == id) {
				return true;
			}
		}
		return false;
	}
	
	//Gets all spawners as a array
	public Spawner[] getSpawners() {
		return (Spawner[]) spawners.toArray();
	}
	
	//Resets selections if a spawner is removed
	public void resetPlayerSelections(int id) {
		for(Player p : selection.keySet()) {
			if(selection.get(p) == id) {
				p.sendMessage(ChatColor.RED + "Your selected spawner has been removed.");
				selection.remove(p);
			}
		}
	}
	
	//Loads spawners from file
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
				String strType = yaml.getString("type");
				EntityType type = null;
				try {
					type = EntityType.fromName(strType);
				} catch(Exception e) {
					log.info("Invalid entity type loading " + f.getName() + ", using default.");
					type = EntityType.fromName(config.getString("spawners.type", "pig")); 
				}
				
				//Setting the spawner's properties and adding it to list.
				Spawner spawner = new Spawner(type, loc, id, this.getServer());
				spawner.radius = radius;
				spawner.redstoneTriggered = redstoneTriggered;
				spawner.maxPlayerDistance = maxPlayerDistance;
				spawner.minPlayerDistance = minPlayerDistance;
				spawner.active = active;
				spawner.maxLightLevel = maxLightLevel;
				spawner.minLightLevel = minLightLevel;
				spawner.hidden = hidden;
				spawner.mobsPerSpawn = mobsPerSpawn;
				spawner.maxMobs = maxMobs;
				spawner.rate = rate;
				
				//Mobs list
				List<LivingEntity> entitiesInWorld = getServer().getWorld(locWorld).getLivingEntities();
				for(Object o : mobIds) {
					if(o instanceof Integer) {
						for(LivingEntity e : entitiesInWorld) {
							if(e.getEntityId() == (Integer) o) {
								spawner.mobs.add(e.getEntityId());
							}
						}
					}
				}
				
				spawners.add(spawner);
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
			log.info("Saving spawner " + String.valueOf(s.id) + " to " + getDataFolder() + "\\Spawners\\" + String.valueOf(s.id) + ".yml");
			File saveFile = new File(getDataFolder() + "\\Spawners\\" + String.valueOf(s.id) + ".yml");
			FileConfiguration yaml = YamlConfiguration.loadConfiguration(saveFile);
			
			if(killOnReload) {
				for(Integer e : s.mobs) {
					List<LivingEntity> entities = getServer().getWorld(s.loc.getWorld().getName()).getLivingEntities();
					for(LivingEntity le : entities) {
						if(le.getEntityId() == e) {
							le.remove();
						}
					}
				}
				s.mobs.clear();
			}
			
			List<Integer> mobIDs = new ArrayList<Integer>();
			for(Integer e : s.mobs) {
				mobIDs.add(e);
			}
			
			yaml.set("id", s.id);
			yaml.set("type", s.type.getName());
			yaml.set("radius", s.radius);
			yaml.set("redstone", s.redstoneTriggered);
			yaml.set("maxPlayer", s.maxPlayerDistance);
			yaml.set("minPlayer", s.minPlayerDistance);
			yaml.set("active", s.active);
			yaml.set("maxLight", s.maxLightLevel);
			yaml.set("minLight", s.minLightLevel);
			yaml.set("hidden", s.hidden);
			yaml.set("mobsPerSpawn", s.mobsPerSpawn);
			yaml.set("maxMobs", s.maxMobs);
			yaml.set("rate", s.rate);
			yaml.set("location.world", s.loc.getWorld().getName());
			yaml.set("location.x", s.loc.getBlockX());
			yaml.set("location.y", s.loc.getBlockY());
			yaml.set("location.z", s.loc.getBlockZ());
			yaml.set("mobs", mobIDs.toArray());
			
			try {
				yaml.save(saveFile);
			} catch (IOException e) {
				e.printStackTrace();
				log.severe("Failed to save spawner " + String.valueOf(s.id) + "!");
			}
		}
		
		log.info("Save complete!");
	}
}
