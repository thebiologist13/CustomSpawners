package com.github.thebiologist13.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SpawnerPowerEvent implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpawnerPower(BlockRedstoneEvent ev) {

		Block block = ev.getBlock();
		Location loc = block.getLocation();

		if(CustomSpawners.redstoneSpawners.containsKey(loc)) {
			Spawner spawner = CustomSpawners.getSpawner(CustomSpawners.redstoneSpawners.get(loc));

			if(spawner != null) {

				boolean hasPower = block.isBlockPowered() || block.isBlockIndirectlyPowered();

				if(spawner.isSpawnOnRedstone()) {

					if(hasPower && !spawner.isPoweredBefore()) {
						spawner.setPoweredBefore(true);
						spawner.spawn();
					}

					if(!hasPower && spawner.isPoweredBefore()) {
						spawner.setPoweredBefore(false);
					}

				}

			}
			
		}
		
	}

}
