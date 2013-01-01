package com.github.thebiologist13.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
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
		//ID
		int id = entity.getEntityId();
		//Target
		Entity target = ev.getTarget();

		if(!(target instanceof Player)) return;
		
		Spawner s = plugin.getSpawnerWithEntity(entity);
		
		if(s == null) return;
		
		SpawnableEntity type = null;
		
		if(s.getMobs().containsKey(id)) {
			type = s.getMobs().get(id);
		}
		
		if(type == null) {
			return;
		}
		
		if(ev.getReason().equals(TargetReason.FORGOT_TARGET)) {
				ev.setCancelled(true);
		}
		
		if(type.isPassive() && !DamageController.angryMobs.containsKey(id)) {
			ev.setCancelled(true);
		}
		
	}
	
}
