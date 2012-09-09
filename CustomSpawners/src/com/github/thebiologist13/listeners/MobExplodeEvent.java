package com.github.thebiologist13.listeners;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

@SuppressWarnings("unused")
public class MobExplodeEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobExplodeEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent ev) {
		//Entity
		Entity e = ev.getEntity();
		
		plugin.printDebugMessage(this.getClass().getName() + ": Registered explosion.");
		
		//SpawnableEntity
		SpawnableEntity s = plugin.getEntityFromSpawner(e);
		
		if(s != null) {
			
			ev.setCancelled(true);
			plugin.printDebugMessage(this.getClass().getName() + ": " + "Cancelled base explosion.");
			plugin.printDebugMessage(this.getClass().getName() + ": " + "Generating Explosion with power of " + s.getYield());
			ev.getLocation().getWorld().createExplosion(ev.getLocation(), s.getYield(), s.isIncendiary());
			DamageController.customExplosives.put(ev.getLocation(), s);
			
		}
		
	}
	
}
