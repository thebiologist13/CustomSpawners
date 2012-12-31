package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SpawnerPowerEvent implements Listener {
	
	private final CustomSpawners PLUGIN;
	
	public SpawnerPowerEvent(CustomSpawners plugin) {
		this.PLUGIN = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpawnerPower(BlockRedstoneEvent ev) {
		
		Spawner spawner = PLUGIN.getSpawnerAt(ev.getBlock().getLocation());
		
		if(spawner != null) {
			
			if(spawner.isSpawnOnRedstone()) {
				spawner.spawn();
			}
			
		}
		
	}
	
}
