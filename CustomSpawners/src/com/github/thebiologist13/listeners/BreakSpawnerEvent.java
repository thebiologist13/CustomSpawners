package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class BreakSpawnerEvent implements Listener {

	private final CustomSpawners PLUGIN;
	
	public BreakSpawnerEvent(CustomSpawners plugin) {
		this.PLUGIN = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent ev) {
		
		Spawner s = PLUGIN.getSpawnerAt(ev.getBlock().getLocation());
		
		if(!PLUGIN.getCustomConfig().getBoolean("spawners.deactivateOnBreak", true)) {
			return;
		}
		
		if(s != null) {
			s.setActive(false);
		}
		
	}
	
}
