package com.github.thebiologist13.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class PlayerTargetEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public PlayerTargetEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerTarget(EntityTargetEvent ev) {
		//Entity
		Entity entity = ev.getEntity();
		//Target
		Entity target = ev.getTarget();

		if(target == null) return; //TODO Not sure if this works

		if(!(target instanceof Player)) return;
		
		Spawner s = plugin.getSpawnerWithEntity(entity);
		
		if(s == null) return;
		
		if(ev.getReason().equals(TargetReason.FORGOT_TARGET)) {
			
			if(s.getMobs().containsKey(entity.getEntityId())) {
				ev.setCancelled(true);
			}
			
		}
		
		if(DamageController.angryMobs.containsKey(entity.getEntityId())) {
			
			ev.setCancelled(true);
			
		}
		
	}
	
}
