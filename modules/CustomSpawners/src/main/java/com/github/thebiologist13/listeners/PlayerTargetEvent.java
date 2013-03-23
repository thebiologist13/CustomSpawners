package com.github.thebiologist13.listeners;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class PlayerTargetEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public PlayerTargetEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerTarget(EntityTargetEvent ev) {
		//Entity
		Entity entity = ev.getEntity();
		//ID
		UUID id = entity.getUniqueId();
		//Target
		Entity target = ev.getTarget();

		if(!(target instanceof Player)) 
			return;
		
		SpawnableEntity s = plugin.getEntityFromSpawner(id);
		
		if(s == null) 
			return;
		
		if(ev.getReason().equals(TargetReason.FORGOT_TARGET)) {
			ev.setCancelled(true);
		}
		
		if(s.isPassive() && !DamageController.angryMobs.containsKey(id)) {
			ev.setCancelled(true);
		}
		
	}
	
}
