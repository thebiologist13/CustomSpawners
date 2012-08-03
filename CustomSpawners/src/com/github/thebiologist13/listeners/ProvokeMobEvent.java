package com.github.thebiologist13.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class ProvokeMobEvent implements Listener {
	
	@EventHandler
	public void onPlayerProvoke(EntityDamageByEntityEvent ev) {
		//Damaged Entity
		Entity damaged = ev.getEntity();
		//Damager
		Entity damager = ev.getDamager();
		//If a match has been found
		boolean match = false;
		//Entity ID
		int entityId = damaged.getEntityId();
		//EntityType
		EntityType type = damaged.getType();
		//Valid Spawners
		ArrayList<Spawner> validSpawners = new ArrayList<Spawner>();
		
		if(!(damager instanceof Player)) {return;}
		
		for(Spawner s : CustomSpawners.spawners) {
			if(s.isPassive()) {
				for(SpawnableEntity e : s.getTypeData().values()) {
					if(e.getType().equals(type)) {
						validSpawners.add(s);
					}
				}
			}
		}
		
		for(Spawner s : validSpawners) {
			Iterator<Integer> mobItr = s.getMobs().iterator();
			
			while(mobItr.hasNext()) {
				int currentMob = mobItr.next();
				
				if(currentMob == entityId) {
					match = true;
					break;
				}
			}
			
			if(match) {break;}
			
		}
		
		if(match) {
			CustomSpawners.angryMobs.add(entityId);
		}
		
	}
	
}
