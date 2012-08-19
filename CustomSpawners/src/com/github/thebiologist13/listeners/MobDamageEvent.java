package com.github.thebiologist13.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class MobDamageEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobDamageEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ev) {
		//DamageController
		DamageController dc = new DamageController();
		//Damaged Entity
		Entity damaged = ev.getEntity();
		//SpawnableEntity
		SpawnableEntity e = plugin.getEntityFromSpawner(damaged);
		
		if(ev.getCause().equals(DamageCause.ENTITY_ATTACK)) 
			return;
		
		if(e != null) {
			dc.doDamage(ev, e);
		}
		
	}
	
}
