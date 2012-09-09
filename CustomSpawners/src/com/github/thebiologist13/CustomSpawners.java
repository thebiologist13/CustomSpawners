package com.github.thebiologist13;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.listeners.DamageController;
import com.github.thebiologist13.listeners.ExpBottleHitEvent;
import com.github.thebiologist13.listeners.InteractEvent;
import com.github.thebiologist13.listeners.MobCombustEvent;
import com.github.thebiologist13.listeners.MobDamageEvent;
import com.github.thebiologist13.listeners.MobDeathEvent;
import com.github.thebiologist13.listeners.MobExplodeEvent;
import com.github.thebiologist13.listeners.MobRegenEvent;
import com.github.thebiologist13.listeners.PlayerLogoutEvent;
import com.github.thebiologist13.listeners.PlayerTargetEvent;
import com.github.thebiologist13.listeners.PotionHitEvent;
import com.github.thebiologist13.listeners.ProjectileFireEvent;

public class CustomSpawners extends JavaPlugin {
	
	/*
	 * THE GREAT TODO LIST OF THINGS TODO!
	 * * Make more console commands
	 * * Fix explosive radius and velocity
	 * * Spawn cycles
	 * * Test new features
	 */
	
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
	
	//Selected spawner by console
	public static int consoleSpawner = -1;
	
	//Selected entity by console
	public static int consoleEntity = -1;
	
	//Player selection area Point 1
	public static ConcurrentHashMap<Player, Location> selectionPointOne = new ConcurrentHashMap<Player, Location>();
	
	//Player selection area Point 2
	public static ConcurrentHashMap<Player, Location> selectionPointTwo = new ConcurrentHashMap<Player, Location>();
	
	//Default Entity to use
	public static SpawnableEntity defaultEntity = null;
	
	//FileManager
	private FileManager fileManager = null;
	
	//YAML variable
	private FileConfiguration config;
	
	//YAML file variable
	private File configFile;
	
	//Debug
	public static boolean debug = false;
	
	public void onEnable() {
		
		//Serialization
		ConfigurationSerialization.registerClass(EntityPotionEffect.class, "Effect");
		
		//Config
		config = getCustomConfig();
		
		//Default Entity
		defaultEntity = new SpawnableEntity(EntityType.fromName(config.getString("entities.type")), -2);
		defaultEntity.setName("Default");
		
		//FileManager assignment
		fileManager = new FileManager(this);
		
		//Debug
		debug = config.getBoolean("data.debug", false);
		
		//Commands
		//TODO Note: to check if a creature is valid, use instanceof Creature or EntityType.isSpawnable()
		//TODO Add other entities (tnt, fireball, etc.)
		SpawnerExecutor se = new SpawnerExecutor(this);
		CustomSpawnersExecutor cse = new CustomSpawnersExecutor(this);
		EntitiesExecutor ee = new EntitiesExecutor(this);
		getCommand("customspawners").setExecutor(cse);
		getCommand("spawners").setExecutor(se);
		getCommand("entities").setExecutor(ee);
		
		//Listeners
		getServer().getPluginManager().registerEvents(new PlayerLogoutEvent(), this);
		getServer().getPluginManager().registerEvents(new MobDamageEvent(this), this);
		getServer().getPluginManager().registerEvents(new MobCombustEvent(), this);
		//getServer().getPluginManager().registerEvents(new MobDamageByEntityEvent(this), this);
		getServer().getPluginManager().registerEvents(new PlayerTargetEvent(this), this);
		getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
		getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
		getServer().getPluginManager().registerEvents(new ExpBottleHitEvent(this), this);
		getServer().getPluginManager().registerEvents(new MobExplodeEvent(this), this);
		getServer().getPluginManager().registerEvents(new MobRegenEvent(this), this);
		getServer().getPluginManager().registerEvents(new PotionHitEvent(this), this);
		getServer().getPluginManager().registerEvents(new ProjectileFireEvent(this), this);
		
		//Load entities from file
		fileManager.loadEntities();
		
		//Load spawners from files
		fileManager.loadSpawners();
		
		/*
		 * Spawning Thread
		 */
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

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
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

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
			
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

				@Override
				public void run() {
					
					fileManager.autosaveAll();
					
				}
				
			}, 20, interval);
		}
		
		//Enable message
		log.info("CustomSpawners by thebiologist13 has been enabled!");
	}
	
	public void onDisable() {
		
		//Saving Entities
		fileManager.saveEntities();
		//Saving spawners
		fileManager.saveSpawners();
		
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
	
	//Converts ticks to MM:SS
	public String convertTicksToTime(int ticks) {
		int minutes = 0;
		float seconds = 0;
		
		if(ticks >= 1200) {
			
			if((ticks % 1200) == 0) {
				minutes = ticks / 1200;
			} else {
				seconds = (ticks % 1200) / 20;
				minutes = (ticks - (ticks % 1200)) / 1200;
			}
			
		} else {
			seconds = ticks / 20;
		}
		
		return minutes + ":" + seconds;
	}
	
	//Gets a ItemStack from string with id and damage value
	public ItemStack getItemStack(String value) {
		int id = 0;
		short damage = 0;
		
		int index = value.indexOf(":") + 1;
		
		if(index == -1) {
			String itemId = value.substring(0, index);
			
			if(!isInteger(itemId)) 
				return null;
			
			id = Integer.parseInt(itemId);
			
			if(Material.getMaterial(id) == null)
				return null;
			
		} else {
			String itemId = value.substring(0, index);
			String itemDamage = value.substring(index + 1, value.length());
			
			if(!isInteger(itemId) || !isInteger(itemDamage)) 
				return null;
			
			id = Integer.parseInt(itemId);
			damage = (short) Integer.parseInt(itemDamage);
			
			if(Material.getMaterial(id) == null)
				return null;
		}
		
		return new ItemStack(id, 1, damage);
	}
	
	//Gets the proper name of an ItemStack
	public String getItemName(ItemStack item) {
		String name = "";
		
		if(item == null) {
			return "AIR (0)";
		}
		
		if(item.getType() != null) {
			name += item.getType().toString() + " (" + item.getTypeId() + ")";
		} else {
			name += item.getTypeId();
		}
		
		if(item.getDurability() != 0) {
			name += ":" + item.getDurability();
		}
		
		return name;
	}
	
	//Gets an EntityPotionEffect from format <PotionEffectType>_<level>_<minutes>:<seconds>
	public EntityPotionEffect getPotion(String value) {
		int index1 = value.indexOf("_");
		int index2 = value.indexOf("_", index1 + 1);
		int index3 = value.indexOf(":");
		if(index1 == -1 || index2 == -1 || index3 == -1) {
			value = "REGENERATION_1_0:0";
			index1 = value.indexOf("_");
			index2 = value.indexOf("_", index1 + 1);
			index3 = value.indexOf(":");
		}
		
		PotionEffectType effectType = PotionEffectType.getByName(value.substring(0, index1));
		int effectLevel = Integer.parseInt(value.substring(index1 + 1, index2));
		int minutes = Integer.parseInt(value.substring(index2 + 1, index3));
		int seconds = Integer.parseInt(value.substring(index3 + 1, value.length()));
		int effectDuration = (minutes * 1200) + (seconds * 20);
		
		return new EntityPotionEffect(effectType, effectLevel, effectDuration);
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
					if(DamageController.extraHealthEntities.containsKey(spawnerMobId)) 
						DamageController.extraHealthEntities.remove(spawnerMobId);
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
					if(DamageController.extraHealthEntities.containsKey(spawnerMobId)) 
						DamageController.extraHealthEntities.remove(spawnerMobId);
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
					if(DamageController.extraHealthEntities.containsKey(spawnerMobId)) 
						DamageController.extraHealthEntities.remove(spawnerMobId);
				}

			}
			
			while(passiveMobs.hasNext()) {
				int spawnerMobId = passiveMobs.next();
				
				if(spawnerMobId == entityId) {
					passiveMobs.remove();
					if(DamageController.extraHealthEntities.containsKey(spawnerMobId)) 
						DamageController.extraHealthEntities.remove(spawnerMobId);
				}

			}
			
		}
		
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}
	
	public SpawnableEntity getEntityFromSpawner(Entity entity) {
		
		if(entity == null) {
			return null;
		}
		
		int entityId = entity.getEntityId();
		Iterator<Spawner> spawnerItr = spawners.values().iterator();
		
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
			Iterator<Integer> mobItr = s.getMobs().keySet().iterator();

			while(passiveMobItr.hasNext()) {
				int currentMob = passiveMobItr.next();

				if(currentMob == entityId) {
					return s.getPassiveMobs().get(currentMob);
				}
				
			}

			while(mobItr.hasNext()) {
				int currentMob = mobItr.next();
				
				if(currentMob == entityId) {
					return s.getMobs().get(currentMob);
				}
				
			}
			
		}
		
		return null;
		
	}
	
	public Spawner getSpawnerWithEntity(Entity entity) {
		EntityType type = entity.getType();
		int entityId = entity.getEntityId();
		ArrayList<Spawner> validSpawners = new ArrayList<Spawner>();
		Iterator<Spawner> spawnerItr = spawners.values().iterator();
		
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			
			for(SpawnableEntity e : s.getTypeData().values()) {
				if(e.getType().equals(type)) {
					validSpawners.add(s);
					break;
				}
			}
		}
		
		for(Spawner s : validSpawners) {
			Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
			Iterator<Integer> mobItr = s.getMobs().keySet().iterator();

			while(passiveMobItr.hasNext()) {
				int currentMob = passiveMobItr.next();

				if(currentMob == entityId) {
					return s;
				}
				
			}

			while(mobItr.hasNext()) {
				int currentMob = mobItr.next();
				
				if(currentMob == entityId) {
					return s;
				}
				
			}
			
		}
		
		return null;
		
	}
	
	public void printDebugMessage(String message) {
		if(debug) {
			log.info("[CS_DEBUG] " + message);
		}
		
	}
	
	public void printDebugMessage(String message, Class<?> clazz) {
		if(debug) {
			if(clazz != null) {
				log.info("[CS_DEBUG] " + clazz.getName() + ": " + message);
			} else {
				log.info("[CS_DEBUG] " + message);
			}
			
		}
		
	}
	
	public void sendMessage(CommandSender sender, String message) {
		
		if(sender == null) 
			return;
		
		Player p = null;
		
		if(sender instanceof Player)
			p = (Player) sender;
		
		if(p == null) {
			message = ChatColor.stripColor(message);
			log.info(message);
		} else {
			p.sendMessage(message);
		}
		
	}
	
	public void sendMessage(CommandSender sender, String[] message) {
		
		if(sender == null) 
			return;
		
		Player p = null;
		
		if(sender instanceof Player)
			p = (Player) sender;
		
		if(p == null) {
			
			for(String s : message) {
				s = ChatColor.stripColor(s);
				log.info(s);
			}
			
		} else {
			p.sendMessage(message);
		}
		
	}
	
}
