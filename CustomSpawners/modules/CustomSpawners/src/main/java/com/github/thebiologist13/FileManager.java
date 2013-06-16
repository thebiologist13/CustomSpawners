package com.github.thebiologist13;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


/**
 * Manages file I/O for CustomSpawners.
 * 
 * @author thebioLOGist13
 */
public class FileManager {

	private final String ch = File.separator;

	private final FileConfiguration CONFIG;

	private final String CRASH_PATH;

	private final String ENTITY_PATH;

	private final String GROUP_PATH;

	private final Logger LOG;

	private final byte LOG_LEVEL;

	private final String NOT_DAT = "CustomSpawners has switched to using .dat files for saving. Sorry for any inconvenience.";

	private final CustomSpawners PLUGIN;

	private final String SPAWNER_PATH;

	private final String SWITCHED_FORMAT = "CustomSpawners has switched to save in .dat files and cannot load .yml files.";

	public FileManager(CustomSpawners plugin) {
		this.PLUGIN = plugin;
		this.LOG = plugin.log;
		this.CONFIG = plugin.getCustomConfig();
		this.LOG_LEVEL = plugin.getLogLevel();
		this.SPAWNER_PATH = plugin.getDataFolder() + File.separator + "Spawners";
		this.ENTITY_PATH = plugin.getDataFolder() + File.separator + "Entities";
		this.GROUP_PATH = plugin.getDataFolder() + File.separator + "Groups";
		this.CRASH_PATH = plugin.getDataFolder() + File.separator + "Crashes";
	}

	//Autosaves an entity
	public synchronized void autosave(SpawnableEntity e) {

		String path = ENTITY_PATH + ch + e.getId() + ".dat";
		File file = new File(path);

		saveEntity(e, file);

	}

	//Autosaves a spawner
	public synchronized void autosave(Spawner s) {

		String path = SPAWNER_PATH + ch + s.getId() + ".dat";
		File file = new File(path);

		saveSpawner(s, file);

	}

	//Autosaves a spawner
	public synchronized void autosave(Group g) {

		String path = GROUP_PATH + ch + g.getId() + ".dat";
		File file = new File(path);

		saveGroup(g, file);

	}

	//Autosaves everything
	public synchronized void autosaveAll() {

		if(CONFIG.getBoolean("data.broadcastAutosave")) {
			if(CONFIG.getBoolean("data.broadcastOnlyOp")) {
				for(OfflinePlayer p : PLUGIN.getServer().getOperators()) {
					if(p instanceof Player) {
						Player p1 = (Player) p;
						PLUGIN.sendMessage(p1, ChatColor.GOLD + CONFIG.getString("data.broadcastMessage", ""));
					}
				}
			} else {
				PLUGIN.getServer().broadcastMessage(ChatColor.GOLD + CONFIG.getString("data.broadcastMessage", ""));
			}
		}

		Iterator<Integer> spawnerItr = CustomSpawners.spawners.keySet().iterator();
		Iterator<Integer> entityItr = CustomSpawners.entities.keySet().iterator();
		Iterator<Integer> groupItr = CustomSpawners.groups.keySet().iterator();

		while(spawnerItr.hasNext()) {
			int id = spawnerItr.next();

			autosave(CustomSpawners.getSpawner(id));
		}

		while(entityItr.hasNext()) {
			int id = entityItr.next();

			autosave(CustomSpawners.getEntity(id));
		}

		while(groupItr.hasNext()) {
			int id = groupItr.next();

			autosave(CustomSpawners.getGroup(id));
		}

		if(CONFIG.getBoolean("data.broadcastAutosave")) {
			if(CONFIG.getBoolean("data.broadcastOnlyOp")) {
				for(OfflinePlayer p : PLUGIN.getServer().getOperators()) {
					if(p instanceof Player) {
						Player p1 = (Player) p;
						PLUGIN.sendMessage(p1, ChatColor.GREEN + CONFIG.getString("data.broadcastMessageEnd", ""));
					}
				}
			} else {
				PLUGIN.getServer().broadcastMessage(ChatColor.GREEN + CONFIG.getString("data.broadcastMessageEnd", ""));
			}
		}

	}

	//Clears the entities list
	public void clearEntities() {
		synchronized(this) {
			CustomSpawners.entities.clear();
		}
	}

	//Clears the spawners list
	public void clearSpawners() {
		synchronized(this) {
			CustomSpawners.spawners.clear();
		}
	}

	//Clears the spawners list
	public void clearGroups() {
		synchronized(this) {
			CustomSpawners.groups.clear();
		}
	}

	public boolean isDat(File f) {
		return f.getName().endsWith(".dat"); 
	}

	public boolean isYaml(File f) {
		return f.getName().endsWith(".yml");
	}

	//Load entities from file
	public void loadEntities() {
		if(LOG_LEVEL > 0)
			LOG.info("Loading entities from directory " + ENTITY_PATH);

		File sDir = new File(ENTITY_PATH);
		if(!sDir.exists())
			sDir.mkdirs();
		File[] sFiles = sDir.listFiles();

		if(LOG_LEVEL > 1)
			LOG.info(String.valueOf(sFiles.length) + " total entities.");

		for(File f : sFiles) {

			SpawnableEntity e = loadEntity(f);

			if(e == null) {
				LOG.info("Failed to load from " + f.getPath());
				continue;
			}

			if(e.getId() == CustomSpawners.defaultEntity.getId())
				continue;

			if(CustomSpawners.entities.containsKey(e.getId())) {
				PLUGIN.cloneWithNewId(e);
				continue;
			}

			CustomSpawners.entities.put(e.getId(), e);

		}

		if(LOG_LEVEL > 0)
			LOG.info("Load Complete!");
	}

	//Loads a SpawnableEntity from a dat file
	public SpawnableEntity loadEntity(File f) {

		if(isDat(f)) {

			try {
				FileInputStream fIn = new FileInputStream(f);
				ObjectInputStream oIn = new ObjectInputStream(fIn);

				SpawnableEntity e = (SpawnableEntity) oIn.readObject();

				oIn.close();
				fIn.close();

				if(e.getModifiers() == null) {
					e.setModifiers(new HashMap<String, String>());
				}

				return e;

			} catch (Exception e) {
				PLUGIN.printDebugTrace(e);
				saveCrash(this.getClass(), e);
				LOG.severe("Failed to load entity from" + f.getPath() + "!");
			}

		} else if(isYaml(f)) {

			LOG.info(SWITCHED_FORMAT);
			f.delete();

		}

		return null;

	}

	//Loads a Group from a DAT file
	public Group loadGroup(File f) {

		if(isDat(f)) {

			try {
				FileInputStream fIn = new FileInputStream(f);
				ObjectInputStream oIn = new ObjectInputStream(fIn);

				Group g = (Group) oIn.readObject();

				oIn.close();
				fIn.close();
				
				return g;

			} catch (Exception e) {
				PLUGIN.printDebugTrace(e);
				saveCrash(this.getClass(), e);
				LOG.severe("Failed to load spawner from" + f.getPath() + "!");
			}

		} else if(isYaml(f)) {

			LOG.info(SWITCHED_FORMAT);
			f.delete();

		}

		return null;

	}

	//Loads groups from file
	public void loadGroups() {

		if(LOG_LEVEL > 0)
			LOG.info("Loading groups from directory " + GROUP_PATH);

		File gDir = new File(GROUP_PATH);
		if(!gDir.exists())
			gDir.mkdirs();
		File[] gFiles = gDir.listFiles();

		if(LOG_LEVEL > 1) 
			LOG.info(String.valueOf(gFiles.length) + " total groups.");

		for(File f : gFiles) {

			Group g = loadGroup(f);

			if(g == null) {
				LOG.info("Failed to load from " + f.getPath());
				continue;
			}

			if(CustomSpawners.groups.containsKey(g.getId())) {
				LOG.info("Skipping " + f.getPath() + ": Duplicate group.");
				continue;
			}

			CustomSpawners.groups.put(g.getId(), g);

		}

		if(LOG_LEVEL > 0)
			LOG.info("Load Complete!");
	}

	//Loads a Spawner from a YAML file
	public Spawner loadSpawner(File f) {

		if(isDat(f)) {

			try {
				FileInputStream fIn = new FileInputStream(f);
				ObjectInputStream oIn = new ObjectInputStream(fIn);

				Spawner s = (Spawner) oIn.readObject();

				oIn.close();
				fIn.close();
				
				if(s.getMobs() == null) {
					s.setMobs(new ConcurrentHashMap<UUID, SpawnableEntity>());
				}

				if(s.getModifiers() == null) {
					s.setModifiers(new HashMap<String, String>());
				}

				if(s.getSecondaryMobs() == null) {
					s.setSecondaryMobs(new ConcurrentHashMap<UUID, UUID>());
				}

				if(s.getSpawnTimes() == null) {
					s.setSpawnTimes(new ArrayList<Integer>());
				}

				if(s.getTypeData() == null) {
					s.setTypeData(new ArrayList<Integer>());
					s.addTypeData(CustomSpawners.defaultEntity);
				}

				return s;

			} catch (Exception e) {
				PLUGIN.printDebugTrace(e);
				saveCrash(this.getClass(), e);
				LOG.severe("Failed to load spawner from " + f.getPath() + "!");
			}

		} else if(isYaml(f)) {

			LOG.info(SWITCHED_FORMAT);
			f.delete();

		}

		return null;

	}

	//Loads spawners from file
	public void loadSpawners() {

		if(LOG_LEVEL > 0)
			LOG.info("Loading spawners from directory " + SPAWNER_PATH);

		File sDir = new File(SPAWNER_PATH);
		if(!sDir.exists())
			sDir.mkdirs();
		File[] sFiles = sDir.listFiles();

		if(LOG_LEVEL > 1) 
			LOG.info(String.valueOf(sFiles.length) + " total spawners.");

		for(File f : sFiles) {

			Spawner s = loadSpawner(f);

			if(s == null) {
				LOG.info("Failed to load from " + f.getPath());
				continue;
			}
			
			if(CustomSpawners.spawners.containsKey(s.getId())) {
				PLUGIN.cloneWithNewId(s);
			}

			CustomSpawners.spawners.put(s.getId(), s);

		}

		if(LOG_LEVEL > 0)
			LOG.info("Load Complete!");
	}

	//Reloads
	public void reloadData() throws Exception {
		reloadEntities();
		reloadSpawners();
		reloadGroups();
	}

	//Reload entities from file
	public void reloadEntities() {
		saveEntities();
		loadEntities();
	}

	//Saves then loads spawners from file
	public void reloadSpawners() {
		saveSpawners();
		loadSpawners();
	}

	//Saves then loads groups from file
	public void reloadGroups() {
		saveGroups();
		loadGroups();
	}

	//Removes a data file
	public void removeEntityDataFile(int id) {

		File file = null;

		String path = ENTITY_PATH + ch + id + ".dat";
		file = new File(path);

		if(!file.exists()) {
			PLUGIN.printDebugMessage("Entity File Does Not Exist. Path => " + path);
			return;
		}

		file.delete();

		for(World w : PLUGIN.getServer().getWorlds()) {
			File entity = new File(w.getWorldFolder() + ch + "cs_data" + ch + "entites" + ch + id + ".dat");

			if(!entity.exists())
				return;

			entity.delete();

		}

	}

	public void removeSpawnerDataFile(int id) {

		File file = null;

		String path = SPAWNER_PATH + ch + id + ".dat";
		file = new File(path);

		if(!file.exists()) {
			PLUGIN.printDebugMessage("Spawner File Does Not Exist. Path => " + path);
			return;
		}

		file.delete();

		for(World w : PLUGIN.getServer().getWorlds()) {
			File spawner = new File(w.getWorldFolder() + ch + "cs_data" + ch + "spawners" + ch + id + ".dat");

			if(!spawner.exists())
				return;

			spawner.delete();

		}

	}

	public void removeGroupDataFile(int id) {

		File file = null;

		String path = GROUP_PATH + ch + id + ".dat";
		file = new File(path);

		if(!file.exists()) {
			PLUGIN.printDebugMessage("Group File Does Not Exist. Path => " + path);
			return;
		}

		file.delete();

		for(World w : PLUGIN.getServer().getWorlds()) {
			File spawner = new File(w.getWorldFolder() + ch + "cs_data" + ch + "groups" + ch + id + ".dat");

			if(!spawner.exists())
				return;

			spawner.delete();

		}

	}

	//Saves a crash report to file
	public String saveCrash(Class<?> clazz, Exception e) {
		Calendar c = Calendar.getInstance();
		String path = CRASH_PATH + ch + "crash-" + String.valueOf(c.get(Calendar.DATE)) + "-" +
				String.valueOf((c.get(Calendar.MONTH) + 1)) + "-" + 
				String.valueOf(c.get(Calendar.YEAR)) + "-" + 
				String.valueOf(System.currentTimeMillis()) + ".txt";
		int minInt = c.get(Calendar.MINUTE);
		String minute = (minInt < 10) ? "0" + minInt : "" + minInt; 

		try {

			File f = new File(path);

			if(!f.exists()) {
				if(f.getParentFile() != null)
					f.getParentFile().mkdirs();
			}

			PrintWriter out = new PrintWriter(new FileOutputStream(f));

			write("CustomSpawners Error on " + c.get(Calendar.DATE) + "/" +
					(c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR), out);
			write("Time of Error: " + c.get(Calendar.HOUR_OF_DAY) + ":" + 
					minute + ":" + c.get(Calendar.SECOND), out);
			write("", out);
			write("I wish I had something funny to say here, but I don't, so, potato. :P", out);
			write("", out);
			write("Please report this error to thebiologist13 via a PM on BukkitDev or an email (thebiologist13@gmail.com).", out);
			write("* * * * * SEND THE CONTENTS OF THIS WHOLE FILE * * * * *", out);
			write("", out);
			write("* * * Java Info * * *", out);
			write("Java Verison: " + System.getProperty("java.version", "Unknown"), out);
			write("", out);
			write("* * * Server Info * * *", out);
			write("", out);
			write("Bukkit Build: " + PLUGIN.getServer().getBukkitVersion(), out);
			write("CustomSpawners Build: " + PLUGIN.getDescription().getVersion(), out);
			write("", out);
			write("* * * Begin Report * * *", out);
			write("Class Error Occurred In: " + clazz.getName(), out);
			write("Error Message: " + e.getMessage(), out);
			write("Stack Trace: ", out);
			for(StackTraceElement el : e.getStackTrace()) {
				writeTabify(el.toString(), out);
			}
			write("", out);
			write("Plugins: ", out);
			for(Plugin p : PLUGIN.getServer().getPluginManager().getPlugins()) {
				writeTabify(p.getName(), out);
			}
			write("", out);
			write("* * * End Report * * *", out);

			out.flush();
			out.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return path;
	}

	//Save entities to file
	public void saveEntities() {

		if(LOG_LEVEL > 0)
			LOG.info("Saving entities...");

		if(LOG_LEVEL > 1)
			LOG.info(String.valueOf(CustomSpawners.entities.size()) + " entities to save.");

		Iterator<SpawnableEntity> entityItr = CustomSpawners.entities.values().iterator();

		while(entityItr.hasNext()) {
			SpawnableEntity e = entityItr.next();

			if(e.getId() == CustomSpawners.defaultEntity.getId())
				continue;

			String path = ENTITY_PATH + ch + String.valueOf(e.getId()) + ".dat";

			if(LOG_LEVEL > 1)
				LOG.info("Saving entity " + String.valueOf(e.getId()) + " to " + path);

			File saveFile = new File(path);
			saveEntity(e, saveFile);

		}

		clearEntities();

		if(LOG_LEVEL > 0)
			LOG.info("Save complete!");	
	}

	//Saves a SpawnableEntity to YAML file
	public void saveEntity(SpawnableEntity s, File f) {

		if(isDat(f)) {

			try {
				if(!f.exists())
					f.createNewFile();

				FileOutputStream fOut = new FileOutputStream(f);
				ObjectOutputStream oOut = new ObjectOutputStream(fOut);

				s.removeNulls();

				oOut.writeObject(s);

				oOut.close();
				fOut.close();

			} catch (Exception e) {
				PLUGIN.printDebugTrace(e);
				saveCrash(this.getClass(), e);
				LOG.severe("Failed to save entity " + String.valueOf(s.getId()) + "!");
			}

			return;
		}

	}

	//Save entities to file
	public void saveGroups() {

		if(LOG_LEVEL > 0)
			LOG.info("Saving groups...");

		if(LOG_LEVEL > 1)
			LOG.info(String.valueOf(CustomSpawners.groups.size()) + " groups to save.");

		Iterator<Group> groupItr = CustomSpawners.groups.values().iterator();

		while(groupItr.hasNext()) {
			Group g = groupItr.next();

			String path = GROUP_PATH + ch + String.valueOf(g.getId()) + ".dat";

			if(LOG_LEVEL > 1)
				LOG.info("Saving group " + String.valueOf(g.getId()) + " to " + path);

			File saveFile = new File(path);
			saveGroup(g, saveFile);

		}

		clearEntities();

		if(LOG_LEVEL > 0)
			LOG.info("Save complete!");	
	}

	//Saves a Group to DAT file
	public void saveGroup(Group g, File f) {

		if(isDat(f)) {

			try {
				if(!f.exists())
					f.createNewFile();

				FileOutputStream fOut = new FileOutputStream(f);
				ObjectOutputStream oOut = new ObjectOutputStream(fOut);

				oOut.writeObject(g);

				oOut.close();
				fOut.close();

			} catch (Exception e) {
				PLUGIN.printDebugTrace(e);
				saveCrash(this.getClass(), e);
				LOG.severe("Failed to save entity " + String.valueOf(g.getId()) + "!");
			}

			return;
		}

	}

	//Saves a Spawner to a YAML file
	public void saveSpawner(Spawner s, File f) {

		if(isDat(f)) {

			try {
				if(!f.exists())
					f.createNewFile();

				FileOutputStream fOut = new FileOutputStream(f);
				ObjectOutputStream oOut = new ObjectOutputStream(fOut);

				s.removeNulls();

				oOut.writeObject(s);

				oOut.close();
				fOut.close();

			} catch (Exception e) {
				PLUGIN.printDebugTrace(e);
				saveCrash(this.getClass(), e);
				LOG.severe("Failed to save spawner " + String.valueOf(s.getId()) + "!");
			}

			return;
		} else {
			LOG.info(NOT_DAT);
		}

	}

	//Saves spawners to file
	public void saveSpawners() {
		if(LOG_LEVEL > 0)
			LOG.info("Saving spawners...");

		if(LOG_LEVEL > 1)
			LOG.info(String.valueOf(CustomSpawners.spawners.size()) + " spawners to save.");

		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();

		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			boolean killOnReload = CONFIG.getBoolean("spawners.killOnReload", false);

			String path = SPAWNER_PATH + ch + String.valueOf(s.getId()) + ".dat";

			if(LOG_LEVEL > 1)
				LOG.info("Saving spawner " + String.valueOf(s.getId()) + " to " + path);

			if(killOnReload) {
				PLUGIN.removeMobs(s);
			}

			File saveFile = new File(path);

			saveSpawner(s, saveFile);

		}

		clearSpawners();

		if(LOG_LEVEL > 0)
			LOG.info("Save complete!");	
	}

	private void write(String s, PrintWriter w) throws IOException {
		w.println(s);
	}

	private void writeTabify(String s, PrintWriter w) throws IOException {
		w.println("\t" + s);
	}

}
