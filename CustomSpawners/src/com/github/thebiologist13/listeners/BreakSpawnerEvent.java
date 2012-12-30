package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class BreakSpawnerEvent implements Listener {

	private CustomSpawners plugin = null;
	
	public BreakSpawnerEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent ev) {
		
		Spawner s = plugin.getSpawnerAt(ev.getBlock().getLocation());
		
		if(!plugin.getCustomConfig().getBoolean("deactivateOnBreak", true)) {
			return;
		}
		
		if(s != null)
			s.setActive(false);
		
	}
	
}
