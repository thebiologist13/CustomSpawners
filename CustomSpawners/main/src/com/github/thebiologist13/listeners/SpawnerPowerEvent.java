package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SpawnerPowerEvent implements Listener {
	
	private final CustomSpawners PLUGIN;
	
	public SpawnerPowerEvent(CustomSpawners plugin) {
		this.PLUGIN = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpawnerPower(BlockPhysicsEvent ev) {
		
		Spawner spawner = PLUGIN.getSpawnerAt(ev.getBlock().getLocation());
		
		if(spawner != null) {
			
			boolean hasPower = spawner.getBlock().isBlockPowered();
		
			if(spawner.isSpawnOnRedstone()) {
				if(hasPower && !spawner.isPoweredBefore()) {
					spawner.spawn();
					spawner.setPoweredBefore(true);
				}
				
				if(!hasPower && spawner.isPoweredBefore()) {
					spawner.setPoweredBefore(false);
				}
			}
			
		}
		
	}
	
}
