package com.github.thebiologist13.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class BreakEvent implements Listener {

	private final CustomSpawners PLUGIN;
	private final FileConfiguration CONFIG;
	
	public BreakEvent(CustomSpawners plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getCustomConfig();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent ev) {
		
		Spawner s = CustomSpawners.getSpawnerAt(ev.getBlock().getLocation());
		
		if(s == null)
			return;
		
		if(CONFIG.getBoolean("spawners.deactivateOnBreak", true))
			s.setActive(false);
		
		if(CONFIG.getBoolean("spawners.removeOnBreak", true)) { 
			if(CONFIG.getBoolean("spawners.killOnRemove", true))
				PLUGIN.removeMobs(s);
			
			int id = s.getId();
			
			PLUGIN.removeSpawner(s);
			PLUGIN.getFileManager().removeSpawnerDataFile(id);
		}
		
	}
	
}
