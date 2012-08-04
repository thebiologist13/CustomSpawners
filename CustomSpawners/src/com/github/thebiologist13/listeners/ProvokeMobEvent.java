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
		
		//If the mob was passive, move to normal mob list.
		
		for(Spawner s : CustomSpawners.spawners) {
			for(SpawnableEntity e : s.getTypeData().values()) {
				if(e.getType().equals(type)) {
					validSpawners.add(s);
					break;
				}
			}
		}
		
		for(Spawner s : validSpawners) {
			Iterator<Integer> passiveMobItr = s.getPassiveMobs().iterator();
			Iterator<Integer> preChecks = PlayerTargetEvent.getPreCheckedMobs().iterator();
			
			while(passiveMobItr.hasNext()) {
				int currentMob = passiveMobItr.next();
				
				if(currentMob == entityId) {
					match = true;
					passiveMobItr.remove();
					break;
				}

			}
			
			while(preChecks.hasNext()) {
				int currentMob = preChecks.next();
				
				if(currentMob == entityId) {
					match = true;
					preChecks.remove();
					break;
				}

			}
			
			if(match) {
				s.addMob(entityId);
				break;
			}
			
		}
		
	}
	
}
