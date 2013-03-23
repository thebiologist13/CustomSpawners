package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.github.thebiologist13.CustomSpawners;

public class MobDamageEvent implements Listener {
	
	private CustomSpawners plugin;
	
	public MobDamageEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ev) {
		//DamageController
		DamageController dc = new DamageController(plugin);
		
		int damage = dc.getModifiedDamage(ev);
		if(damage > 0) {
			ev.setDamage(damage);
		} else {
			ev.setDamage(0);
		}
		
	}
	
}
