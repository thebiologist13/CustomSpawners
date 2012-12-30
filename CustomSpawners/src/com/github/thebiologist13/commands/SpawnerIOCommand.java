package com.github.thebiologist13.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;


/**
 * This command saves and loads spawner data to the world folder.
 * 
 * @author thebiologist13
 */
public class SpawnerIOCommand extends CustomSpawnersCommand {

	public SpawnerIOCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public SpawnerIOCommand(CustomSpawners plugin, String perm) {
		super(plugin, perm);
	}
	
	@Override
	public void run(CommandSender sender, String subCommand, String[] args) {
		if(subCommand.equals("import")) {

			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Loading CustomSpawners data from worlds...");
			
			int amountLoaded = loadData().size();
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Loaded " + ChatColor.GOLD + amountLoaded + ChatColor.GREEN + " spawners from worlds in server.");

		} else if(subCommand.equals("export")) {

			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Exporting CustomSpawners data to worlds...");
			
			Iterator<Spawner> sp = CustomSpawners.spawners.values().iterator();
			int amountLoaded = 0;
			while(sp.hasNext()) {
				PLUGIN.saveCustomSpawnerToWorld(sp.next());
				amountLoaded++;
			}

			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Exported " + ChatColor.GOLD + amountLoaded + ChatColor.GREEN + " spawners to worlds.");
			
		}
	}
	
	//Load spawners and entities from world files
	public List<Spawner> loadData() {

		Iterator<World> worldItr = PLUGIN.getServer().getWorlds().iterator();
		List<Spawner> loaded = new ArrayList<Spawner>();
		
		while(worldItr.hasNext()) {
			World w = worldItr.next();

			Iterator<SpawnableEntity> entitiesFromWorld = loadAllEntitiesFromWorld(w).iterator();
			while(entitiesFromWorld.hasNext()) {
				SpawnableEntity e = entitiesFromWorld.next();
				int nextId = PLUGIN.getNextEntityId();
				if(CustomSpawners.entities.containsKey(e.getId())) {
					e = e.cloneWithNewId(nextId);
				}
				CustomSpawners.entities.put(nextId, e);
			}

			Iterator<Spawner> spawnersFromWorld = loadAllSpawnersFromWorld(w).iterator();
			while(spawnersFromWorld.hasNext()) {
				Spawner s = spawnersFromWorld.next();
				boolean sameSpawner = false;

				for(Spawner s1 : CustomSpawners.spawners.values()) {
					if(s1.getLoc().equals(s.getLoc())) {
						sameSpawner = true;
					}
				}

				if(sameSpawner) {
					continue;
				} else {
					int nextId = PLUGIN.getNextSpawnerId();
					Spawner s1 = s.cloneWithNewId(nextId);
					CustomSpawners.spawners.put(nextId, s1);
					loaded.add(s1);
				}
			}
		}
		
		return loaded;
		
	}
	
	public List<Spawner> loadAllSpawnersFromWorld(World w) {
		List<Spawner> list = new ArrayList<Spawner>();
		
		String ch = File.separator;
		String worldDir = w.getWorldFolder() + ch + "cs_data" + ch;
		String entityDir = worldDir + ch + "entity";
		String spawnerDir = worldDir + ch + "spawner";
		
		File spawnerFiles = new File(spawnerDir);
		File entityFiles = new File(entityDir);
		
		if(!spawnerFiles.exists())
			spawnerFiles.mkdirs();
		
		if(!entityFiles.exists())
			entityFiles.mkdirs();
		
		for(File spawnerFile : spawnerFiles.listFiles()) {
			
			Spawner s = PLUGIN.getFileManager().loadSpawner(spawnerFile);
			List<Integer> sEntsAsIDs = s.getTypeData();
			List<SpawnableEntity> sEnts = new ArrayList<SpawnableEntity>();
			ArrayList<SpawnableEntity> containedEntities = new ArrayList<SpawnableEntity>();
			
			for(Integer i : sEntsAsIDs) {
				sEnts.add(CustomSpawners.getEntity(i.toString()));
			}
			
			for(File f : entityFiles.listFiles()) {
				containedEntities.add(PLUGIN.getFileManager().loadEntity(f));
			}
			
			if(containedEntities.containsAll(sEnts))
				list.add(s);
			
		}
		
		return list;
	}
	
	public List<SpawnableEntity> loadAllEntitiesFromWorld(World w) {
		List<SpawnableEntity> list = new ArrayList<SpawnableEntity>();
		
		String ch = File.separator;
		String worldDir = w.getWorldFolder() + ch + "cs_data" + ch;
		String entityDir = worldDir + ch + "entity";
		
		File entityFiles = new File(entityDir);
		
		if(!entityFiles.exists())
			entityFiles.mkdirs();
		
		for(File f : entityFiles.listFiles()) {
			list.add(PLUGIN.getFileManager().loadEntity(f));
		}
		
		return list;
	}

}
