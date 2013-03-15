package com.github.thebiologist13.listeners;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;


public class MobCombustEvent implements Listener {
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent ev) {
		//Entity
		Entity combustee = ev.getEntity();
		//ID
		UUID id = combustee.getUniqueId();
		
		if(!DamageController.negatedFireImmunity.containsKey(id)) {
			DamageController.negatedFireImmunity.put(id, ev.getDuration() / 20);
		}
		
	}
	
}
