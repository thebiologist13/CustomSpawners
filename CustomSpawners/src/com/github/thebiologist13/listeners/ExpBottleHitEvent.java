package com.github.thebiologist13.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class ExpBottleHitEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public ExpBottleHitEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onHit(ExpBottleEvent ev) {
		//Entity
		Entity entity = ev.getEntity();
		//SpawnableEntity
		SpawnableEntity e = plugin.getEntityFromSpawner(entity);

		if(e != null) {

			ev.setExperience(e.getDroppedExp());

		}
		
	}
	
}
