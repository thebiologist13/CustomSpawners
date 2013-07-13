package com.github.thebiologist13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.astesana.javaluator.DoubleEvaluator;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.github.thebiologist13.api.IConverter;
import com.github.thebiologist13.api.IObject;
import com.github.thebiologist13.api.ISItemStack;
import com.github.thebiologist13.api.ISpawnManager;
import com.github.thebiologist13.api.ISpawnableEntity;
import com.github.thebiologist13.api.ISpawner;
import com.github.thebiologist13.attributelib.VanillaAttribute;
import com.github.thebiologist13.listeners.*;
import com.github.thebiologist13.serialization.SPotionEffect;
import com.github.thebiologist13.v1_6_R2.Converter;
import com.herocraftonline.heroes.Heroes;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * CustomSpawners is a plugin for making customizable spawners for Bukkit
 * servers.
 * 
 * Licensed under GNU-GPLv3
 * 
 * @author thebiologist13
 * @version 0.5
 */
//TODO FEATURE: Synchronize Spawners command
public class CustomSpawners extends JavaPlugin {

	// Selected entity by console.
	public static int consoleEntity = -1;

	// Selected group by console.
	public static int consoleGroup = -1;

	// Selected spawner by console.
	public static int consoleSpawner = -1;
	
	// Debug
	public static boolean debug = false;

	// Default Entity to use.
	public static SpawnableEntity defaultEntity = null;

	//Donor List
	public static String donors; 

	// All of the entity types on the server.
	public static ConcurrentHashMap<Integer, SpawnableEntity> entities = new ConcurrentHashMap<Integer, SpawnableEntity>();

	// Selected entities for players.
	public static ConcurrentHashMap<Player, Integer> entitySelection = new ConcurrentHashMap<Player, Integer>();

	// All the groups in the server.
	public static ConcurrentHashMap<Integer, Group> groups = new ConcurrentHashMap<Integer, Group>();

	//Selected groups for players
	public static ConcurrentHashMap<Player, Integer> groupSelection = new ConcurrentHashMap<Player, Integer>();

	//Entities that should be removed when out of range. The value is the spawner ID.
	public static ConcurrentHashMap<UUID, Integer> rangeEntities = new ConcurrentHashMap<UUID, Integer>();
	
	//Spawners that need to be listened on for redstone.
	public static ConcurrentHashMap<Location, Integer> redstoneSpawners = new ConcurrentHashMap<Location, Integer>();

	//The entities that have a removal time.
	public static ConcurrentHashMap<UUID, Long> timedEntities = new ConcurrentHashMap<UUID, Long>();
	
	// Player selection area Point 1.
	public static ConcurrentHashMap<Player, Location> selectionPointOne = new ConcurrentHashMap<Player, Location>();

	// Player selection area Point 2.
	public static ConcurrentHashMap<Player, Location> selectionPointTwo = new ConcurrentHashMap<Player, Location>();

	// Players not using selections.
	public static ConcurrentHashMap<Player, Boolean> selectMode = new ConcurrentHashMap<Player, Boolean>();

	//The server's spawner. Used to manage individual spawns.
	public static Spawner serverSpawner = null;

	// All the spawners in the server.
	public static ConcurrentHashMap<Integer, Spawner> spawners = new ConcurrentHashMap<Integer, Spawner>();

	// Selected spawners for players.
	public static ConcurrentHashMap<Player, Integer> spawnerSelection = new ConcurrentHashMap<Player, Integer>();

	// Transparent Blocks to go through when getting the target location for a
	// spawner.
	public static HashSet<Byte> transparent = new HashSet<Byte>();
	
	//Players waiting for confirmation on command.
	public static ConcurrentHashMap<Player, Integer> warn = new ConcurrentHashMap<Player, Integer>();
	
	//Citizens
	private static Citizens citizens = null;
	
	//Heroes
	private static Heroes heroes = null;

	// WorldGuard
	private static WorldGuardPlugin worldGuard = null;

	// Autosave Task ID
	public int autosaveId;

	// Logger
	public Logger log = Logger.getLogger("Minecraft");

	// YAML variable
	private FileConfiguration config;

	// YAML file variable
	private File configFile;

	// FileManager
	private FileManager fileManager = null;

	// LogLevel
	private byte logLevel;

	// Save interval
	private long saveInterval;

	public static double evaluate(String expr) throws IllegalArgumentException {
		DoubleEvaluator eval = new DoubleEvaluator();
		try {
			return eval.evaluate(expr);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	public static Citizens getCitizens() {
		
		if(citizens != null)
			return citizens;
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Citizens");

		if(plugin == null && !(plugin instanceof Citizens))
			return null;

		if(!plugin.isEnabled())
			return null;
		
		return (Citizens) plugin;
	}
	
	public static IConverter getConverter() {
		return new Converter();
	}
	
	// Gets an entity
	public static SpawnableEntity getEntity(int ref) {
		return getEntity(String.valueOf(ref));
	}

	// Gets an entity
	public static SpawnableEntity getEntity(String ref) {

		ref = ChatColor.stripColor(ref);

		if (ref.isEmpty())
			return null;

		ref = ref.toLowerCase();

		ref = ref.replaceAll("__", " ");

		if (isInteger(ref)) {
			int id = Integer.parseInt(ref);

			if (id == -2)
				return defaultEntity;

			Iterator<Integer> entityItr = entities.keySet().iterator();
			while (entityItr.hasNext()) {
				int currentId = entityItr.next();

				if (currentId == id) {
					return entities.get(id);
				}
			}

		} else {

			if (ref.equals("default"))
				return defaultEntity;

			Iterator<Integer> entityItr = entities.keySet().iterator();
			while (entityItr.hasNext()) {
				Integer id = entityItr.next();
				SpawnableEntity s = entities.get(id);
				String name = ChatColor.stripColor(s.getName());

				if (name == null) {
					return null;
				}

				if (name.equalsIgnoreCase(ref)) {
					return s;
				}
			}
		}

		return null;
	}

	// Gets a spawner
	public static Group getGroup(int ref) {
		return getGroup(String.valueOf(ref));
	}

	// Gets a group
	public static Group getGroup(String ref) {

		ref = ChatColor.stripColor(ref);

		if (ref.isEmpty())
			return null;

		ref = ref.toLowerCase();

		ref = ref.replaceAll("__", " ");

		if (isInteger(ref)) {
			int id = Integer.parseInt(ref);
			Iterator<Integer> groupItr = groups.keySet().iterator();

			while (groupItr.hasNext()) {
				int currentId = groupItr.next().intValue();

				if (currentId == id) {
					return groups.get(id);
				}
			}
		} else {
			Iterator<Integer> groupItr = groups.keySet().iterator();

			while (groupItr.hasNext()) {
				Integer id = groupItr.next().intValue();
				Group g = groups.get(id);
				String name = ChatColor.stripColor(g.getName());

				if (name == null) {
					return null;
				}

				if (name.equalsIgnoreCase(ref)) {
					return g;
				}
			}
		}

		return null;
	}

	//Gets Heroes
	public static Heroes getHeroes() {

		if(heroes != null)
			return heroes;

		Plugin plugin = Bukkit.getPluginManager().getPlugin("Heroes");

		if(plugin == null && !(plugin instanceof Heroes))
			return null;
		
		if(!plugin.isEnabled())
			return null;

		return (Heroes) plugin;
	}

	// Check if players are nearby
	public static ArrayList<Player> getNearbyPlayers(Location source, double max) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(!p.getWorld().equals(source.getWorld()))
				continue;
			double distanceSq = p.getLocation().distanceSquared(source);
			if (distanceSq <= Math.pow(max, 2)) {
				players.add(p);
			}
		}
		return players;
	}

	// Gets the next available ID number in a list
	public static int getNextID(List<Integer> set) {
		int returnID = 0;
		boolean taken = true;

		while (taken) {

			if (set.size() == 0) {
				return 0;
			}

			for (Integer i : set) {
				if (returnID == i) {
					taken = true;
					break;
				} else {
					taken = false;
				}
			}

			if (taken) {
				returnID++;
			}
		}

		return returnID;
	}

	// Gets a spawner
	public static Spawner getSpawner(int ref) {
		return getSpawner(String.valueOf(ref));
	}

	// Gets a spawner
	public static Spawner getSpawner(String ref) {

		ref = ChatColor.stripColor(ref);

		if (ref.isEmpty())
			return null;

		ref = ref.toLowerCase();

		ref = ref.replaceAll("__", " ");

		if (isInteger(ref)) {
			int id = Integer.parseInt(ref);
			Iterator<Integer> spawnerItr = spawners.keySet().iterator();

			while (spawnerItr.hasNext()) {
				int currentId = spawnerItr.next();

				if (currentId == id) {
					return spawners.get(id);
				}
			}
		} else {
			Iterator<Integer> spawnerItr = spawners.keySet().iterator();

			while (spawnerItr.hasNext()) {
				Integer id = spawnerItr.next();
				Spawner s = spawners.get(id);
				String name = ChatColor.stripColor(s.getName());

				if (name == null) {
					return null;
				}

				if (name.equalsIgnoreCase(ref)) {
					return s;
				}
			}
		}

		return null;
	}

	// Gets a spawner from a location
	public static Spawner getSpawnerAt(Location loc) {
		Iterator<Spawner> spItr = CustomSpawners.spawners.values().iterator();

		while (spItr.hasNext()) {
			Spawner s = spItr.next();

			if (s.getBlock().getLocation().equals(loc.getBlock().getLocation())) {
				return s;
			}

		}

		return null;

	}

	// Sets up WorldGuard
	public static WorldGuardPlugin getWG() {
		if(worldGuard != null)
			return worldGuard;

		Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");

		if (wg == null || !(wg instanceof WorldGuardPlugin))
			return null;
		
		if(!wg.isEnabled())
			return null;

		return (WorldGuardPlugin) wg;
	}

	// Convenience method for accurately testing if a string can be parsed to an
	// double.
	public static boolean isDouble(String what) {
		try {
			Double.parseDouble(what);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Convenience method for accurately testing if a string can be parsed to an
	// double.
	public static boolean isFloat(String what) {
		try {
			Float.parseFloat(what);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Convenience method for accurately testing if a string can be parsed to an
	// integer.
	public static boolean isInteger(String what) {
		try {
			Integer.parseInt(what);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isLong(String what) {
		try {
			Long.parseLong(what);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public boolean allowedEntity(EntityType type) {
		String name = type.toString();

		List<?> notAllowed = config.getList("mobs.blacklist");

		if (notAllowed.contains(name)) {
			return false;
		}

		return true;
	}

	public SpawnableEntity cloneWithNewId(SpawnableEntity entity) {
		SpawnableEntity entity1 = createEntity(entity.getType());
		entity1.setData(entity.getData());
		entity1.setName("");
		entity1.setWhitelist(entity.getWhitelist());
		entity1.setBlacklist(entity.getBlacklist());
		entity1.setDropsSItemStack(entity.getSItemStackDrops());
		entity1.setItemDamage(entity.getItemDamage());
		entity1.setEffects(entity.getEffects());
		entity1.setModifiers(entity.getModifiers());
		return entity1;
	}

	public Spawner cloneWithNewId(Spawner spawner) {
		Spawner spawner1 = createSpawner(spawner.getMainEntity(),
				spawner.getLoc());
		spawner1.setTypeData(spawner.getTypeData());
		spawner1.setMobs(new ConcurrentHashMap<UUID, SpawnableEntity>());
		spawner1.setModifiers(spawner.getModifiers());
		spawner1.setSpawnTimes(spawner.getSpawnTimes());
		spawner1.setData(spawner.getData());
		spawner1.setActive(false);
		return spawner1;
	}

	// Converts ticks to MM:SS
	public String convertTicksToTime(int ticks) {
		int minutes = 0;
		float seconds = 0;
		float floatTick = ticks;

		if (floatTick >= 1200) {

			if ((floatTick % 1200) == 0) {
				minutes = Math.round(floatTick / 1200);
			} else {
				seconds = (floatTick % 1200) / 20;
				minutes = Math.round((floatTick - (floatTick % 1200)) / 1200);
			}

		} else {
			seconds = floatTick / 20;
		}

		String strSec = "";

		if (seconds < 10) {
			strSec = "0" + String.valueOf(seconds);
		} else {
			strSec = String.valueOf(seconds);
		}

		return String.valueOf(minutes) + ":" + strSec;
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

	public SpawnableEntity createEntity(EntityType type) {

		SpawnableEntity newEntity = new SpawnableEntity(type, getNextEntityId());

		CustomSpawners.entities.put(newEntity.getId(), newEntity);

		if(config.getBoolean("data.autosave") && config.getBoolean("data.saveOnCreate")) {
			fileManager.autosave(newEntity);
		}

		return entities.get(newEntity.getId());
	}

	public SpawnableEntity createEntity(String type, boolean hasOverride) throws IllegalArgumentException {

		SpawnableEntity newEntity = new SpawnableEntity(EntityType.PIG, getNextEntityId());

		parseEntityType(type, newEntity, hasOverride); //Throws an error if invalid

		CustomSpawners.entities.put(newEntity.getId(), newEntity);

		if(config.getBoolean("data.autosave") && config.getBoolean("data.saveOnCreate")) {
			fileManager.autosave(newEntity);
		}

		return entities.get(newEntity.getId());
	}

	public Spawner createSpawner(ISpawnableEntity e, Location loc) {
		Spawner newSpawner = new Spawner((SpawnableEntity) e, loc, getNextSpawnerId());

		newSpawner.setRadius(config.getDouble("spawners.radius", 8));
		newSpawner.setRedstoneTriggered(config.getBoolean("spawners.redstoneTriggered", false));
		newSpawner.setMaxPlayerDistance(config.getInt("spawners.maxPlayerDistance", 16));
		newSpawner.setMinPlayerDistance(config.getInt("spawners.minPlayerDistance", 0));
		newSpawner.setActive(config.getBoolean("spawners.active", false));
		newSpawner.setMaxLightLevel((byte) config.getInt("spawners.maxLightLevel", 7));
		newSpawner.setMinLightLevel((byte) config.getInt("spawners.minLightLevel", 0));
		newSpawner.setHidden(config.getBoolean("spawners.hidden", false));
		newSpawner.setRate(config.getInt("spawners.rate", 120));
		newSpawner.setMobsPerSpawn(config.getInt("spawners.mobsPerSpawn", 2));
		newSpawner.setMaxMobs(config.getInt("spawners.maxMobs", 12));
		newSpawner.setCapped(config.getBoolean("spawners.capped", false));

		spawners.put(newSpawner.getId(), newSpawner);

		if (config.getBoolean("data.autosave")
				&& config.getBoolean("data.saveOnCreate")) {
			getFileManager().autosave(newSpawner);
		}

		return spawners.get(newSpawner.getId());
	}

	public List<Group> findObjectInGroups(IObject obj) {
		List<Group> inside = new ArrayList<Group>();
		Iterator<Group> itr = CustomSpawners.groups.values().iterator();
		while(itr.hasNext()) {
			Group g = itr.next();

			if(obj instanceof Spawner && !g.getType().equals(Group.Type.SPAWNER)) {
				continue;
			} else if(obj instanceof SpawnableEntity && !g.getType().equals(Group.Type.ENTITY)) {
				continue;
			}
			
			for(IObject obj0 : g.getGroup().keySet()) {
				if(obj0.equals(obj)) {
					inside.add(g);
					break;
				}
			}
		}
		return inside;
	}

	public FileConfiguration getCustomConfig() {
		if (config == null) {
			reloadCustomConfig();
		}
		return config;
	}
	
	public String getDamageCause(String in) {

		in.toLowerCase();

		String type = "";

		if (in.equals("blockexplosion")) {
			type = "BLOCK_EXPLOSION";
		} else if (in.equals("entityexplosion") || in.equals("creeper")) {
			type = "ENTITY_EXPLOSION";
		} else if (in.equals("firetick") || in.equals("burning")) {
			type = "FIRE_TICK";
		} else if (in.equals("attack") || in.equals("entityattack")) {
			type = "ENTITY_ATTACK";
		} else if (in.equals("item") || in.equals("itemdamage")) {
			type = "ITEM";
		} else if (in.equals("spawnerfire") || in.equals("spawnerfireticks")
				|| in.equals("spawner_fire")) {
			type = "SPAWNER_FIRE_TICKS";
		} else {
			for (DamageCause c : DamageCause.values()) {
				if (c.toString().equalsIgnoreCase(in)) {
					type = in;
					break;
				}
			}
		}

		return type.toUpperCase();
	}

	public SpawnableEntity getEntityFromSpawner(Entity entity) {

		if (entity == null) {
			return null;
		}

		UUID entityId = entity.getUniqueId();

		return getEntityFromSpawner(entityId);

	}

	public SpawnableEntity getEntityFromSpawner(UUID id) {

		Iterator<Spawner> spawnerItr = spawners.values().iterator();

		while (spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			Iterator<UUID> mobItr = s.getMobs().keySet().iterator();

			while (mobItr.hasNext()) {
				UUID currentMob = mobItr.next();

				if (currentMob.equals(id)) {
					return s.getMobs().get(currentMob);
				}

			}

			Iterator<UUID> itr = s.getSecondaryMobs().keySet().iterator();

			while (itr.hasNext()) {
				UUID currentMob = itr.next();

				if (currentMob.equals(id)) {
					return s.getMobs()
							.get(s.getSecondaryMobs().get(currentMob));
				}

			}

		}

		return null;

	}

	public Entity getEntityFromAnyWorld(UUID id) {
		for(World w : getServer().getWorlds()) {
			Entity e = getEntityFromWorld(id, w);
			if(e != null)
				return e;
		}
		return null;
	}
	
	public Entity getEntityFromWorld(UUID id, World w) {

		Iterator<Entity> entitiesInWorld = w.getEntities().iterator();
		while (entitiesInWorld.hasNext()) {
			Entity e = entitiesInWorld.next();

			if (e.getUniqueId().equals(id)) {
				return e;
			}

		}

		return null;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	// Gets a string to represent the name of the group (String version of ID
	// or name)
	public String getFriendlyName(Group g) {

		if (g == null)
			return "";

		String returnMe = "(" + g.getType() + ") ";
		
		if (g.getName().isEmpty()) {
			return returnMe + String.valueOf(g.getId());
		} else {
			return returnMe + g.getName();
		}
	}

	// Gets a string to represent the name of the entity (String version of ID
	// or name)
	public String getFriendlyName(ISpawnableEntity e) {

		if (e == null)
			return "";

		if (e.getName().isEmpty()) {
			return String.valueOf(e.getId());
		} else {
			return e.getName();
		}
	}

	// Gets a string to represent the name of the spawner (String version of ID
	// or name)
	public String getFriendlyName(ISpawner s) {

		if (s == null)
			return "";

		if (s.getName().isEmpty()) {
			return String.valueOf(s.getId());
		} else {
			return s.getName();
		}
	}

	// Gets a potion from an alias
	public PotionEffectType getInputEffect(String effect) {

		PotionEffectType type = null;
		effect.toLowerCase();

		if (effect.equals("damageresistance")
				|| effect.equals("damage_resistance")
				|| effect.equals("resistance")) {
			type = PotionEffectType.DAMAGE_RESISTANCE;
		} else if (effect.equals("instanthealth")
				|| effect.equals("instant_health") || effect.equals("health")
				|| effect.equals("hp")) {
			type = PotionEffectType.HEAL;
		} else if (effect.equals("instant_damage")
				|| effect.equals("instantdamage")) {
			type = PotionEffectType.HARM;
		} else if (effect.equals("haste") || effect.equals("mining_haste")
				|| effect.equals("mininghaste")) {
			type = PotionEffectType.FAST_DIGGING;
		} else if (effect.equals("fireresistance")) {
			type = PotionEffectType.FIRE_RESISTANCE;
		} else if (effect.equals("strength")) {
			type = PotionEffectType.INCREASE_DAMAGE;
		} else if (effect.equals("fatigue") || effect.equals("miningfatigue")
				|| effect.equals("mining_fatigue")) {
			type = PotionEffectType.SLOW_DIGGING;
		} else if (effect.equals("slowness") || effect.equals("slow")) {
			type = PotionEffectType.SLOW;
		} else if (effect.equals("nightvision")) {
			type = PotionEffectType.NIGHT_VISION;
		} else if (effect.equals("waterbreathing")) {
			type = PotionEffectType.WATER_BREATHING;
		} else if (effect.equals("regen")) {
			type = PotionEffectType.REGENERATION;
		} else {
			type = PotionEffectType.getByName(effect);
		}

		return type;

	}

	public ItemStack getItem(String item, int count) {
		ItemStack stack = getItemStack(item);

		if (stack == null)
			return null;

		stack.setAmount(count);

		return stack;
	}

	public String getItemName(ISItemStack item0) {
		String name = "";

		if (item0 == null) {
			return "AIR (0)";
		}

		ItemStack item = item0.toItemStack();

		if (item.getType() != null) {
			name += item.getType().toString() + " (" + item.getTypeId() + ")";
		} else {
			name += item.getTypeId();
		}

		if (item.getDurability() != 0) {
			name += ":" + item.getDurability();
		}

		if(item0.getDropChance() != 0.0F) {
			name += " " + item0.getDropChance() + "% drop chance"; 
		}

		return name;
	}

	// Gets the proper name of an ItemStack
	public String getItemName(ItemStack item) {
		String name = "";

		if (item == null) {
			return "AIR (0)";
		}

		if (item.getType() != null) {
			name += item.getType().toString() + " (" + item.getTypeId() + ")";
		} else {
			name += item.getTypeId();
		}

		if (item.getDurability() != 0) {
			name += ":" + item.getDurability();
		}

		return name;
	}

	// Gets a ItemStack from string with id and damage value
	public ItemStack getItemStack(String value) {
		// Format should be either <data value:damage value> or <data value>
		int id = 0;
		short damage = 0;

		// Version 0.0.5b - Tweaked this so it would register right
		int index = value.indexOf(":");

		if (index == -1) {
			index = value.indexOf("-");
		}

		if (index == -1) {

			String itemId = value.substring(0, value.length());

			if (!isInteger(itemId)) {
				Material mat = Material.getMaterial(itemId.toUpperCase());

				if (mat == null)
					return null;

				id = mat.getId();
			} else {
				id = Integer.parseInt(itemId);

				if (Material.getMaterial(id) == null)
					return null;
			}

		} else {
			String itemId = value.substring(0, index);
			String itemDamage = value.substring(index + 1, value.length());

			if (!isInteger(itemId)) {
				Material mat = Material.getMaterial(itemId.toUpperCase());

				if (mat == null)
					return null;

				id = mat.getId();
			} else {
				id = Integer.parseInt(itemId);

				if (Material.getMaterial(id) == null)
					return null;
			}

			if (!isInteger(itemDamage))
				return null;

			damage = (short) Integer.parseInt(itemDamage);
		}

		return new ItemStack(id, 1, damage);
	}

	// Gets the log level
	public byte getLogLevel() {
		return this.logLevel;
	}

	// Next available entity id
	public int getNextEntityId() {
		List<Integer> entityIDs = new ArrayList<Integer>();

		entityIDs.addAll(entities.keySet());

		return getNextID(entityIDs);
	}

	// Next available group ID
	public int getNextGroupId() {
		List<Integer> groupIDs = new ArrayList<Integer>();

		groupIDs.addAll(groups.keySet());

		return getNextID(groupIDs);
	}

	// Next available spawner ID
	public int getNextSpawnerId() {
		List<Integer> spawnerIDs = new ArrayList<Integer>();

		spawnerIDs.addAll(spawners.keySet());

		return getNextID(spawnerIDs);
	}

	// Gets an EntityPotionEffect from format
	// <PotionEffectType>_<level>_<minutes>:<seconds>
	public SPotionEffect getPotion(String value) {
		int index1 = value.indexOf("_");
		int index2 = value.indexOf("_", index1 + 1);
		int index3 = value.indexOf(":");
		if (index1 == -1 || index2 == -1 || index3 == -1) {
			value = "REGENERATION_1_0:0";
			index1 = value.indexOf("_");
			index2 = value.indexOf("_", index1 + 1);
			index3 = value.indexOf(":");
		}

		PotionEffectType effectType = PotionEffectType.getByName(value
				.substring(0, index1));
		int effectLevel = Integer.parseInt(value.substring(index1 + 1, index2));
		int minutes = Integer.parseInt(value.substring(index2 + 1, index3));
		int seconds = Integer.parseInt(value.substring(index3 + 1,
				value.length()));
		int effectDuration = (minutes * 1200) + (seconds * 20);

		return new SPotionEffect(effectType, effectDuration, effectLevel);
	}

	public Spawner getSpawnerWithEntity(Entity entity) {
		UUID entityId = entity.getUniqueId();

		return getSpawnerWithEntity(entityId);

	}

	public Spawner getSpawnerWithEntity(UUID id) {

		Iterator<Spawner> spawnerItr = spawners.values().iterator();

		while (spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			Iterator<UUID> mobItr = s.getMobs().keySet().iterator();

			while (mobItr.hasNext()) {
				UUID currentMob = mobItr.next();

				if (currentMob.equals(id)) {
					return s;
				}

			}

			Iterator<UUID> itr = s.getSecondaryMobs().keySet().iterator();

			while (itr.hasNext()) {
				UUID currentMob = itr.next();

				if (currentMob.equals(id)) {
					return s;
				}

			}

		}

		return null;

	}

	@Override
	public void onDisable() {

		if (fileManager != null) {
			// Saving Entities
			fileManager.saveEntities();
			// Saving spawners
			fileManager.saveSpawners();
			// Saving groups
			fileManager.saveGroups();
		}

		// Stop Tasks
		getServer().getScheduler().cancelTasks(this);

		// Disable message
		log.info("CustomSpawners by thebiologist13 has been disabled!");
	}

	@Override
	public void onEnable() {

		//OK to put config stuff up here because it doesn't use CraftBukkit or NMS
		// Config
		config = getCustomConfig();

		// Debug
		debug = config.getBoolean("data.debug", false);

		// LogLevel
		logLevel = (byte) config.getInt("data.logLevel", 2);

		// Compat
		if (!setupCompat()) {
			this.getLogger().severe("CustomSpawners " + this.getDescription().getVersion()
					+ " does not support this version of Bukkit.");
			this.getLogger().severe("Please see http://dev.bukkit.org/server-mods/customspawners for supported versions.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		//Donors
		if(logLevel > 1)
			log.info("[CustomSpawners] Getting the list of donors...");
		donors = getDonors();

		// Transparent Blocks
		transparent.add((byte) 0);
		transparent.add((byte) 8);
		transparent.add((byte) 9);
		transparent.add((byte) 10);
		transparent.add((byte) 11);

		// Default Entity
		defaultEntity = new SpawnableEntity(EntityType.fromName(config.getString("entities.type", "Pig")), -2);
		defaultEntity.setName("Default");

		// FileManager assignment
		fileManager = new FileManager(this);

		// Interval
		saveInterval = (config.getLong("data.interval", 10) * 1200);

		if(config.getBoolean("spawners.ignoreWorldGuard", false)) {
			if(logLevel > 0) {
				log.info("[CustomSpawners] Ignoring WorldGuard connection.");
			}
		} else {
			// Setup WG
			worldGuard = getWG();

			if (worldGuard == null) {

				if (logLevel > 0) {
					log.info("[CustomSpawners] Cannot hook into WorldGuard.");
				}

			} else {

				if (logLevel > 0) {
					log.info("[CustomSpawners] Hooked into WorldGuard.");
				}

			}
		}

		if(config.getBoolean("spawners.ignoreHeroes", false)) {
			if(logLevel > 0) {
				log.info("[CustomSpawners] Ignoring Heroes connection.");
			}
		} else {
			// Setup WG
			heroes = getHeroes();

			if (heroes == null) {

				if (logLevel > 0) {
					log.info("[CustomSpawners] Cannot hook into Heroes.");
				}

			} else {

				if (logLevel > 0) {
					log.info("[CustomSpawners] Hooked into Heroes.");
				}

			}
		}
		
		if(config.getBoolean("spawners.ignoreCitizens", false)) {
			if(logLevel > 0) {
				log.info("[CustomSpawners] Ignoring Citizens connection.");
			}
		} else {
			// Setup WG
			citizens = getCitizens();

			if (citizens == null) {

				if (logLevel > 0) {
					log.info("[CustomSpawners] Cannot hook into Citizens.");
				}

			} else {

				if (logLevel > 0) {
					log.info("[CustomSpawners] Hooked into Citizens.");
				}

			}
		}

		serverSpawner = new Spawner(defaultEntity, new Location(getServer().getWorlds().get(0), 0, 0, 0), -1);
		serverSpawner.setHidden(true);
		serverSpawner.setActive(false);
		serverSpawner.setName("&cSOMEHOW YOU FOUND ME! DON'T CHANGE ME! I AM THE PROPERTY OF THE SERVER!");

		try {
			// Commands
			SpawnerExecutor se = new SpawnerExecutor(this);
			CustomSpawnersExecutor cse = new CustomSpawnersExecutor(this);
			EntitiesExecutor ee = new EntitiesExecutor(this);
			GroupsExecutor ge = new GroupsExecutor(this);
			getCommand("customspawners").setExecutor(cse);
			getCommand("spawners").setExecutor(se);
			getCommand("entities").setExecutor(ee);
			getCommand("groups").setExecutor(ge);

			// Listeners
			PluginManager pm = getServer().getPluginManager();
			pm.registerEvents(new PlayerLogoutEvent(), this);
			pm.registerEvents(new MobDamageEvent(this), this);
			pm.registerEvents(new MobCombustEvent(), this);
			pm.registerEvents(new PlayerTargetEvent(this), this);
			pm.registerEvents(new MobDeathEvent(this), this);
			pm.registerEvents(new InteractEvent(this), this);
			pm.registerEvents(new ExpBottleHitEvent(this), this);
			pm.registerEvents(new MobExplodeEvent(this), this);
			pm.registerEvents(new PotionHitEvent(this), this);
			pm.registerEvents(new ProjectileFireEvent(this), this);
			pm.registerEvents(new BreakEvent(this), this);
			pm.registerEvents(new SpawnerPowerEvent(), this);
			pm.registerEvents(new ReloadEvent(this), this);

			//Just a debug listener for entity ID + UUID
			//getServer().getPluginManager().registerEvents(new PlayerClickEntityEvent(this), this); 
		} catch(Exception e) {
			fileManager.saveCrash(getClass(), e);
			log.severe("Failed to load CustomSpawners: Error initializing commands and listeners."
					+ "Crash saved to " + getDataFolder().getPath() + File.separator + "CustomSpawners"
					+ File.separator + "Crashes");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		try {
			// Load entities from file
			fileManager.loadEntities();

			// Load spawners from files
			fileManager.loadSpawners();

			//Load Groups from files
			fileManager.loadGroups();
		} catch (Exception e) {
			fileManager.saveCrash(getClass(), e);
			log.severe("Failed to load all Spawners and Entities from file. Are they all in the new .dat file format? "
					+ "Crash saved to " + getDataFolder().getPath() + File.separator + "CustomSpawners"
					+ File.separator + "Crashes");
		}

		/*
		 * Main Thread
		 */
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {

				Iterator<Spawner> spawnerItr = spawners.values().iterator();

				while (spawnerItr.hasNext()) {
					Spawner s = spawnerItr.next();

					if (!s.getLoc().getChunk().isLoaded()) {
						continue;
					}

					s.tick();

					//Removes capped spawners
					if(s.isCapped() && s.getMobsIds().size() == s.getMaxMobs()) {
						removeSpawner(s);
						fileManager.removeSpawnerDataFile(s.getId());
					}

				}
				
				Iterator<UUID> rangeItr = rangeEntities.keySet().iterator();
				while(rangeItr.hasNext()) {
					UUID id = rangeItr.next();
					for(World w : getServer().getWorlds()) {
						Entity e = getEntityFromWorld(id, w);
						if(e == null)
							continue;
						Spawner sp =  getSpawner(rangeEntities.get(id));
						Location spLoc = sp.getLoc();
						if(e.getLocation().distanceSquared(spLoc) > Math.pow(sp.getKillRange(), 2)) {
							rangeItr.remove();
							if(!removeNPC(e))
								e.remove();
							break;
						}
					}
				}
				
				Iterator<UUID> timeItr = timedEntities.keySet().iterator();
				while(timeItr.hasNext()) {
					UUID id = timeItr.next();
					long curTime = timedEntities.get(id);
					
					if(curTime == 1) {
						timeItr.remove();
						for(World w : getServer().getWorlds()) {
							Entity e = getEntityFromWorld(id, w);
							if(e == null)
								continue;
							if(!removeNPC(e))
								e.remove();
						}
					}
					
					timedEntities.put(id, curTime - 1);
				}

			}

		}, 20, 1);

		/*
		 * Clearing thread. Manages non-existent entity removal and removes timed-out commands.
		 */
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {

				Iterator<Spawner> sp = spawners.values().iterator();
				while (sp.hasNext()) {
					Spawner s = sp.next();
					Iterator<UUID> spMobs = s.getMobs().keySet().iterator();
					while (spMobs.hasNext()) {

						UUID spId = spMobs.next();

						Entity e = getEntityFromWorld(spId, s.getLoc().getWorld());

						if (e == null) {
							s.removeMob(spId);
							continue;
						}

						//Shouldn't cause problems if this isn't here.
						/*if (e.getLocation().distance(s.getLoc()) > 192) {
								s.removeMob(spId);
								e.remove();
						}*/

						//This will untrack mobs if they are too far from the spawner.
						if(s.isTrackNearby()) {
							if(e.getLocation().distanceSquared(s.getLoc()) >= Math.pow(s.getMaxPlayerDistance(), 2)) {
								s.removeMob(spId);
							}
						}

					}

					Iterator<UUID> secMobs = s.getSecondaryMobs().keySet().iterator();
					while (secMobs.hasNext()) {
						UUID id = secMobs.next();

						Entity e = getEntityFromWorld(id, s.getLoc().getWorld());

						if (e == null) {
							s.removeSecondaryMob(id);
							continue;
						}

						//Shouldn't cause problems if this isn't here.
						/*if (e.getLocation().distance(s.getLoc()) > 192) {
							  s.removeSecondaryMob(id);
							  e.remove();
						}*/

						//This will untrack mobs if they are too far from the spawner.
						if(s.isTrackNearby()) {
							if(e.getLocation().distanceSquared(s.getLoc()) >= Math.pow(s.getMaxPlayerDistance(), 2)) {
								s.removeSecondaryMob(id);
							}
						}

					}

				}

				Iterator<Player> timeItr = warn.keySet().iterator();
				while(timeItr.hasNext()) {
					Player p = timeItr.next();

					warn.replace(p, warn.get(p) - 1);
					if(warn.get(p).intValue() == 0)
						warn.remove(p);
				}

			}

		}, 20, 20);

		/*
		 * Autosave Thread This thread manages autosaving
		 */
		if (config.getBoolean("data.autosave")
				&& config.getBoolean("data.saveOnClock")) {

			autosaveId = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

				@Override
				public void run() {
					fileManager.autosaveAll();
				}

			}, 20, saveInterval);
		}

		// Enable message
		log.info("[CustomSpawners] CustomSpawners by thebiologist13 has been enabled!");
	}

	//Parse an attribute
	public VanillaAttribute parseAttribute(String name) {
		
		VanillaAttribute att = VanillaAttribute.fromName(name);
		
		if(att != null)
			return att;
		
		name = name.toLowerCase();
		
		if(name.equals("maxhealth") || name.equals("health") || name.equals("hp")) {
			return VanillaAttribute.MAX_HEALTH;
		} else if(name.equals("followrange") || name.equals("follow") || name.equals("range")) {
			return VanillaAttribute.FOLLOW_RANGE;
		} else if(name.equals("knockbackresistance") || name.equals("knockback") || name.equals("knock")) {
			return VanillaAttribute.KNOCKBACK_RESISTANCE;
		} else if(name.equals("movementspeed") || name.equals("movement") || name.equals("move") 
				|| name.equals("speed") || name.equals("movespeed")) {
			return VanillaAttribute.MOVEMENT_SPEED;
		} else if(name.equals("attackdamage") || name.equals("attack") || name.equals("damage") 
				|| name.equals("atk") || name.equals("dmg")) {
			return VanillaAttribute.ATTACK_DAMAGE;
		} else if(name.equals("jumpstrength") || name.equals("jump")) {
			return VanillaAttribute.JUMP_STRENGTH;
		} else if(name.equals("spawnreinforcements") || name.equals("spawn") || name.equals("reinforcements")) {
			return VanillaAttribute.SPAWN_REINFORCEMENTS;
		} else {
			return null;
		}
		
	}
	
	// Parses the entity name
	public String parseEntityName(EntityType type) {
		String nameOfType = type.getName();

		if (nameOfType == null) {
			return type.toString();
		} else {
			return nameOfType;
		}

	}

	// Parses the entity type from it's name
	@SuppressWarnings("deprecation")
	public void parseEntityType(String entityType, SpawnableEntity applyTo,
			boolean hasOverride) throws IllegalArgumentException {
		EntityType type = null;
		String name = "";
		List<?> notAllowed = config.getList("mobs.blacklist");
		entityType = entityType.toLowerCase();

		if (entityType.equalsIgnoreCase("irongolem")) {

			type = EntityType.IRON_GOLEM;

		} else if (entityType.equalsIgnoreCase("mooshroom")) {

			type = EntityType.MUSHROOM_COW;

		} else if (entityType.equalsIgnoreCase("zombiepigman")) {

			type = EntityType.PIG_ZOMBIE;

		} else if (entityType.equalsIgnoreCase("magmacube")
				|| entityType.equalsIgnoreCase("fireslime")
				|| entityType.equalsIgnoreCase("firecube")) {

			type = EntityType.MAGMA_CUBE;

		} else if (entityType.equalsIgnoreCase("snowman")
				|| entityType.equalsIgnoreCase("snowgolem")) {

			type = EntityType.SNOWMAN;

		} else if (entityType.equalsIgnoreCase("ocelot")
				|| entityType.equalsIgnoreCase("ozelot")) {

			type = EntityType.OCELOT;

		} else if (entityType.equalsIgnoreCase("arrow")) {

			type = EntityType.ARROW;

		} else if (entityType.equalsIgnoreCase("snowball")) {

			type = EntityType.SNOWBALL;

		} else if (entityType.equalsIgnoreCase("falling_block")
				|| entityType.equalsIgnoreCase("fallingblock")
				|| entityType.equalsIgnoreCase("sand")
				|| entityType.equalsIgnoreCase("gravel")) {

			type = EntityType.FALLING_BLOCK;

		} else if (entityType.equalsIgnoreCase("tnt")
				|| entityType.equalsIgnoreCase("primed_tnt")
				|| entityType.equalsIgnoreCase("primed_tnt")) {

			type = EntityType.PRIMED_TNT;

		} else if (entityType.equalsIgnoreCase("firecharge")
				|| entityType.equalsIgnoreCase("smallfireball")
				|| entityType.equalsIgnoreCase("fire_charge")
				|| entityType.equalsIgnoreCase("small_fireball")) {

			type = EntityType.SMALL_FIREBALL;

		} else if (entityType.equalsIgnoreCase("fireball")
				|| entityType.equalsIgnoreCase("ghastball")
				|| entityType.equalsIgnoreCase("fire_ball")
				|| entityType.equalsIgnoreCase("ghast_ball")) {

			type = EntityType.FIREBALL;

		} else if (entityType.equalsIgnoreCase("potion")
				|| entityType.equalsIgnoreCase("splashpotion")
				|| entityType.equalsIgnoreCase("splash_potion")) {

			type = EntityType.SPLASH_POTION;

		} else if (entityType.equalsIgnoreCase("experience_bottle")
				|| entityType.equalsIgnoreCase("experiencebottle")
				|| entityType.equalsIgnoreCase("xpbottle")
				|| entityType.equalsIgnoreCase("xp_bottle")
				|| entityType.equalsIgnoreCase("expbottle")
				|| entityType.equalsIgnoreCase("exp_bottle")) {

			type = EntityType.THROWN_EXP_BOTTLE;

		} else if (entityType.equalsIgnoreCase("item")
				|| entityType.equalsIgnoreCase("drop")) {

			type = EntityType.DROPPED_ITEM;

		} else if (entityType.equalsIgnoreCase("enderpearl")
				|| entityType.equalsIgnoreCase("ender_pearl")
				|| entityType.equalsIgnoreCase("enderball")
				|| entityType.equalsIgnoreCase("ender_ball")) {

			type = EntityType.ENDER_PEARL;

		} else if (entityType.equalsIgnoreCase("endercrystal")
				|| entityType.equalsIgnoreCase("ender_crystal")
				|| entityType.equalsIgnoreCase("enderdragoncrystal")
				|| entityType.equalsIgnoreCase("enderdragon_crystal")) {

			type = EntityType.ENDER_CRYSTAL;

		} else if (entityType.equalsIgnoreCase("egg")) {

			type = EntityType.EGG;

		} else if (entityType.equalsIgnoreCase("wither")
				|| entityType.equalsIgnoreCase("witherboss")
				|| entityType.equalsIgnoreCase("wither_boss")) {

			type = EntityType.WITHER;

		} else if (entityType.equalsIgnoreCase("minecart")
				|| entityType.equalsIgnoreCase("mine_cart")) {

			type = EntityType.MINECART;

		} else if (entityType.equalsIgnoreCase("firework")
				|| entityType.equalsIgnoreCase("firework_rocket")) {

			type = EntityType.FIREWORK;

		} else if (entityType.equalsIgnoreCase("player") 
				|| entityType.equalsIgnoreCase("human")
				|| entityType.equalsIgnoreCase("npc")
				|| entityType.equalsIgnoreCase("citizen")) {
			
			type = EntityType.PLAYER;
			
		} else if (entityType.equalsIgnoreCase("horse") 
				|| entityType.equalsIgnoreCase("mare")
				|| entityType.equalsIgnoreCase("colt")
				|| entityType.equalsIgnoreCase("stallion")
				|| entityType.equalsIgnoreCase("pony")) {
			
			type = EntityType.HORSE;
			
		} else {
			// Try to parse an entity type from input. Null if invalid.
			type = EntityType.fromName(entityType);
		}

		if (type == null) {
			if (entityType.equals("spiderjockey")
					|| entityType.equals("spider_jockey")
					|| entityType.equals("skeletonjockey")
					|| entityType.equals("skeleton_jockey")
					|| entityType.equals("jockey")) {
				if ((notAllowed.contains("spider_jockey") || notAllowed
						.contains("skeleton_jockey")) && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.SPIDER);
				applyTo.setJockey(true);
			} else if (entityType.equals("witherskeleton")
					|| entityType.equals("wither_skeleton")) {
				if (notAllowed.contains("wither_skeleton") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.SKELETON);
				applyTo.setProp("wither", true);
			} else if (entityType.equals("crepp")) {
				if (notAllowed.contains("creeper") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.CREEPER);
				applyTo.setCharged(true);
				applyTo.setYield(0);
			} else if (entityType.equals("chargedcreeper")
					|| entityType.equals("charged_creeper")
					|| entityType.equals("powercreeper")
					|| entityType.equals("power_creeper")) {
				if (notAllowed.contains("creeper") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.CREEPER);
				applyTo.setCharged(true);
			} else if (entityType.equals("chest_minecart")
					|| entityType.equals("chestminecart")) {
				if (notAllowed.contains("minecart") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.MINECART);
				applyTo.setItemType(new ItemStack(Material.CHEST));
			} else if (entityType.equals("furnace_minecart")
					|| entityType.equals("furnaceminecart")
					|| entityType.equals("oven_minecart")
					|| entityType.equals("ovenminecart")) {
				if (notAllowed.contains("minecart") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.MINECART);
				applyTo.setItemType(new ItemStack(Material.FURNACE));
			} else if (entityType.equals("tnt_minecart")
					|| entityType.equals("tntminecart")
					|| entityType.equals("explosive_minecart")
					|| entityType.equals("explosiveminecart")) {
				if (notAllowed.contains("minecart") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.MINECART);
				applyTo.setItemType(new ItemStack(Material.TNT));
			} else if (entityType.equals("spawner_minecart")
					|| entityType.equals("spawnerminecart")) {
				if (notAllowed.contains("minecart") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				applyTo.setType(EntityType.MINECART);
				applyTo.setItemType(new ItemStack(Material.MOB_SPAWNER));
			} else if (entityType.equals("filly")
					|| entityType.equals("mylittlepony")) {
				if (notAllowed.contains("horse") && !hasOverride)
					throw new IllegalArgumentException("Not allowed entity.");
				
				String[] ponies = {"&4R&6a&i&2n&1b&9o&5w__&cDash", "&9Twilight__&5Sparkle",
						"&eFluttershy", "&6Applejack", "&5Rarity", "&dPinkie__Pie", "&7Derpy__&eHooves",
						"&1Princess__Luna", "&ePrincess__Celestia", "&6Apple__&4Bloom", "&fSweetie__&dBelle",
						"&6Scootaloo", "&4Big__&2Mac", "&bThe__Great__and__Powerful__&9TRIXIE!"};
				
				applyTo.setType(EntityType.HORSE);
				int rand = (int) Math.round(Math.random() * ponies.length);
				applyTo.setName(ponies[rand]);
			} else {
				throw new IllegalArgumentException("Invalid entity type.");
			}
		} else {
			name = type.getName();

			if (notAllowed.contains(name) && !hasOverride)
				throw new IllegalArgumentException("Not allowed entity.");
			applyTo.setType(type);
		}

	}

	public void printDebugMessage(String message) {
		printDebugMessage(new String[] {message} );
	}

	public void printDebugMessage(String message, Class<?> clazz) {
		printDebugMessage(new String[] {message} , clazz);
	}

	public void printDebugMessage(String[] message) {
		if (debug) {
			for (String s : message) {
				log.info("[CS_DEBUG] " + s);
			}
		}
	}

	public void printDebugMessage(String[] message, Class<?> clazz) {
		if (debug) {
			for (String s : message) {
				if (clazz != null) {
					log.info("[CS_DEBUG] " + clazz.getName() + ": " + s);
				} else {
					log.info("[CS_DEBUG] " + s);
				}
			}
		}
	}

	public void printDebugTrace(Exception e) {
		if (debug) {
			log.severe("[CS_DEBUG] " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Config stuff
	public void reloadCustomConfig() {
		if (configFile == null) {
			configFile = new File(getDataFolder(), "config.yml");

			if (!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				copy(getResource("config.yml"), configFile);
			}

		}

		config = YamlConfiguration.loadConfiguration(configFile);

		// Look for defaults in the jar
		InputStream defConfigStream = this.getResource("config.yml");
		if (defConfigStream != null) {
			FileConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
			config.options().copyDefaults(true);
		}

	}

	// Remove an entity
	public void removeEntity(SpawnableEntity e) {
		List<Group> groups = findObjectInGroups(e);
		for(Group g : groups) {
			g.removeItem(e);
		}
		
		if (entities.containsValue(e)) {
			resetEntitySelections(e.getId());
			entities.remove(e.getId());
			for (Spawner s : spawners.values()) {
				s.removeTypeData(e);
			}
		}
	}

	// Remove a spawner
	public void removeGroup(Group g) {
		if (groups.containsValue(g)) {
			resetGroupSelections(g.getId());
			groups.remove(g.getId());
		}
	}

	// Removes a spawner from a mob list when it dies
	// Called when an entity dies. e is the dead entity.
	public synchronized void removeMob(final Entity e) { 

		if (e == null)
			return;

		UUID entityId = e.getUniqueId();
		Iterator<Spawner> itr = CustomSpawners.spawners.values().iterator();

		while (itr.hasNext()) {
			Spawner s = itr.next();

			Iterator<UUID> mobs = s.getMobs().keySet().iterator();
			while (mobs.hasNext()) {
				UUID id = mobs.next();

				if (id.equals(entityId)) {
					mobs.remove();
				}

			}

			Iterator<UUID> secMobs = s.getSecondaryMobs().keySet().iterator();
			while (secMobs.hasNext()) {
				UUID id = secMobs.next();

				if (id.equals(entityId)) {
					secMobs.remove();
				}

			}
			
			rangeEntities.remove(entityId);
			timedEntities.remove(entityId);

		}

	}

	// Removes mobs spawned by a certain spawner
	public synchronized void removeMobs(final Spawner s) { // Called in the removemobs command

		Iterator<UUID> mobs = s.getMobs().keySet().iterator();
		while (mobs.hasNext()) {
			UUID id = mobs.next();
			Entity spawnerMob = getEntityFromWorld(id, s.getLoc().getWorld());

			if (spawnerMob == null) {
				continue;
			}

			spawnerMob.remove();
			mobs.remove();

			rangeEntities.remove(id);
			timedEntities.remove(id);
			
		}

		Iterator<UUID> secMobs = s.getSecondaryMobs().keySet().iterator();
		while (secMobs.hasNext()) {
			UUID id = secMobs.next();
			Entity spawnerMob = getEntityFromWorld(id, s.getLoc().getWorld());

			if (spawnerMob == null) {
				continue;
			}

			spawnerMob.remove();
			secMobs.remove();

			rangeEntities.remove(id);
			timedEntities.remove(id);
			
		}

		s.getMobs().clear();
		s.getSecondaryMobs().clear();

	}

	// Remove a spawner
	public void removeSpawner(Spawner s) {
		
		Location loc = s.getLoc();
		if(redstoneSpawners.containsKey(loc))
			redstoneSpawners.remove(loc);
		
		Iterator<UUID> itr = rangeEntities.keySet().iterator();
		while(itr.hasNext()) {
			UUID id = itr.next();
			if(rangeEntities.get(id).equals(s))
				itr.remove();
		}
		
		List<Group> groups = findObjectInGroups(s);
		for(Group g : groups) {
			g.removeItem(s);
		}
		
		if (spawners.containsValue(s)) {
			resetSpawnerSelections(s.getId());
			spawners.remove(s.getId());
		}
	}

	// Resets selections if a SpawnableEntity has been removed
	public void resetEntitySelections(int id) {
		Iterator<Player> pItr = entitySelection.keySet().iterator();

		while (pItr.hasNext()) {
			Player p = pItr.next();

			if (entitySelection.get(p) == id) {
				p.sendMessage(ChatColor.RED
						+ "Your selected entity has been removed.");
				entitySelection.remove(p);
			}
		}

	}

	//Resets selections if a group is removed
	public void resetGroupSelections(int id) {
		Iterator<Player> pItr = groupSelection.keySet().iterator();

		while (pItr.hasNext()) {
			Player p = pItr.next();

			if (groupSelection.get(p) == id) {
				p.sendMessage(ChatColor.RED
						+ "Your selected group has been removed.");
				groupSelection.remove(p);
			}
		}

	}

	// Resets selections if a spawner is removed
	public void resetSpawnerSelections(int id) {
		Iterator<Player> pItr = spawnerSelection.keySet().iterator();

		while (pItr.hasNext()) {
			Player p = pItr.next();

			if (spawnerSelection.get(p) == id) {
				p.sendMessage(ChatColor.RED
						+ "Your selected spawner has been removed.");
				spawnerSelection.remove(p);
			}
		}

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

	// This saves a Spawner to the world folder. Kind of "cheating" to make it
	// so custom spawners can be recovered from the world.
	public void saveCustomSpawnerToWorld(Spawner data) {
		World w = data.getLoc().getWorld();

		String ch = File.separator;
		String worldDir = w.getWorldFolder() + ch + "cs_data" + ch;
		String entityDir = worldDir + "entity";
		String spawnerDir = worldDir + "spawner";

		File spawnerFilesDir = new File(spawnerDir);
		File entityFilesDir = new File(entityDir);

		if(!spawnerFilesDir.exists())
			spawnerFilesDir.mkdirs();

		if(!entityFilesDir.exists())
			entityFilesDir.mkdirs();

		String spawnerPath = spawnerDir + ch + data.getId() + ".dat";

		File spawnerFile = new File(spawnerPath);

		List<Integer> types = data.getTypeData();

		File[] entityFilesList = entityFilesDir.listFiles();
		ArrayList<String> entityFiles = new ArrayList<String>();
		if (entityFilesList != null) {
			for (File f : entityFilesList) {
				entityFiles.add(f.getPath());
			}
		}

		Iterator<Integer> tItr = types.iterator();
		while (tItr.hasNext()) {
			int i = tItr.next();

			printDebugMessage("Checking if entity files exist");

			String fileName = entityDir + ch + i + ".dat";

			printDebugMessage("File to check: " + fileName);

			if (!entityFiles.contains(fileName)) {
				printDebugMessage("Doesn't contain file. Creating...");
				fileManager.saveEntity(getEntity(String.valueOf(i)), new File(fileName));
			}
		}

		printDebugMessage("World Folder: " + worldDir);

		fileManager.saveSpawner(data, spawnerFile);
	}

	public void sendMessage(CommandSender sender, String message) {
		sendMessage(sender, new String[] {message} );
	}

	public void sendMessage(CommandSender sender, String[] message) {

		if (sender == null)
			return;

		Player p = null;

		if (sender instanceof Player)
			p = (Player) sender;

		if (p == null) {

			for (String s : message) {
				s = "[CustomSpawners] " + ChatColor.stripColor(s);
				log.info(s);
			}

		} else {
			p.sendMessage(message);
		}

	}

	private String getDonors() {

		String donors = "";

		try {
			URL url = new URL("http://dev.bukkit.org/server-mods/customspawners/pages/donors/");
			URLConnection con = url.openConnection();
			con.setReadTimeout(10000);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String l;
			boolean reachedContent = false;
			while ((l=in.readLine())!=null) {

				if(l.contains("<div class=\"content-box\"><div class=\"content-box-inner\">" +
						"<p>These are the current donors to CustomSpawners:<br>")) {
					reachedContent = true;
					continue;
				}

				if(!reachedContent)
					continue;

				if(l.contains("</div></div>") && reachedContent)
					break;

				l = l.replaceAll("\"", "");
				l = l.replaceAll("<br>", "");
				l = l.replaceAll("<p>", "");
				l = l.replaceAll("</p>", "");

				l = ChatColor.translateAlternateColorCodes('&', l); //Allows me to color code

				if(donors.isEmpty()) {
					donors += l;
				} else {
					donors += ", " + l;
				}

			}
		} catch (Exception e) {
			printDebugTrace(e);
			log.info("[CustomSpawners] Could not load donor list.");
		}

		return donors;

	}

	private boolean removeNPC(Entity entity) {
		if(citizens != null) {
			NPCRegistry reg = CitizensAPI.getNPCRegistry();
			if(reg.isNPC(entity)) {
				NPC npc = reg.getNPC(entity);
				npc.despawn();
				reg.deregister(npc);
				return true;
			}
		}
		return false;
	}
	
	private boolean setupCompat() {
		String packageName = this.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);
		if (version.equals("craftbukkit")) {
			version = "pre";
		}

		try {

			String converter = "com.github.thebiologist13." + version + ".Converter";
			String spawnManager = "com.github.thebiologist13." + version + ".SpawnManager";

			printDebugMessage("Looking for Converter in: " + converter);
			printDebugMessage("Looking for SpawnManager in: " + spawnManager);

			final Class<?> clazz = Class.forName(converter);
			final Class<?> clazz0 = Class.forName(spawnManager);

			if (clazz == null || clazz0 == null) {
				return false;
			}

			if (!IConverter.class.isAssignableFrom(clazz)
					|| !ISpawnManager.class.isAssignableFrom(clazz0)) {
				return false;
			}
			return true;
		} catch (final Exception e) {
			return false;
		}

	}

}
