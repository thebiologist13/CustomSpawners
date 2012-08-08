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

public class MobDamageByEntityEvent implements Listener {
	
	@EventHandler
	public void onPlayerProvoke(EntityDamageByEntityEvent ev) {
		//Damaged Entity
		Entity damaged = ev.getEntity();
		//Damager
		Entity damager = ev.getDamager();
		//If a match has been found
		boolean match = false;
		//From spawner in general
		boolean fromSpawner = false;
		//Entity ID
		int entityId = damaged.getEntityId();
		//EntityType
		EntityType type = damaged.getType();
		//Valid Spawners
		ArrayList<Spawner> validSpawners = new ArrayList<Spawner>();
		
		if(!(damager instanceof Player)) {return;}
		
		//If the mob was passive, move to normal mob list.
		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();
		
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			
			for(SpawnableEntity e : s.getTypeData().values()) {
				if(e.getType().equals(type)) {
					validSpawners.add(s);
					break;
				}
			}
		}
		
		for(Spawner s : validSpawners) {
			SpawnableEntity e = null;
			Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
			Iterator<Integer> mobItr = s.getMobs().keySet().iterator();
			Iterator<Integer> preChecks = PlayerTargetEvent.getPreCheckedMobs().keySet().iterator();
			
			while(preChecks.hasNext()) {
				int currentMob = preChecks.next();
				
				if(currentMob == entityId) {
					match = true;
					e = PlayerTargetEvent.getPreCheckedMobs().get(currentMob);
					preChecks.remove();
					break;
				}

			}
			
			if(!match) {
				while(passiveMobItr.hasNext()) {
					int currentMob = passiveMobItr.next();
					
					if(currentMob == entityId) {
						match = true;
						fromSpawner = true;
						e = s.getPassiveMobs().get(currentMob);
						passiveMobItr.remove();
						break;
					}

				}
				
				while(mobItr.hasNext()) {
					int currentMob = mobItr.next();
					
					if(currentMob == entityId) {
						fromSpawner = true;
						e = s.getMobs().get(currentMob);
						break;
					}

				}
			}
			
			if(e != null) {
				if(match) {
					s.addMob(entityId, e);
					break;
				}
				
				if(fromSpawner) {
					if(!takeDamage(e, (Player) damager)) {
						ev.setDamage(0);
					}
					
					break;
				}
				
			}
			
		}
		
	}
	
	private boolean takeDamage(SpawnableEntity e, Player p) {
		//Item ID
		int id = p.getItemInHand().getTypeId();
		
		/*
		 * Blacklist items are the only ones it DOES NOT take damage from.
		 * Whitelist items are the only ones it DOES take damage from.
		 */
		if(e.isUsingBlacklist()) {
			if(e.getDamageBlacklist().contains("ITEM")) {
				if(e.getItemDamageList().contains(id)) {
					return false;
				}
			}
			return true;
		} else if(e.isUsingWhitelist()) {
			if(e.getDamageWhitelist().contains("ITEM")) {
				if(e.getItemDamageList().contains(id)) {
					return true;
				}
			}
			return false;
		}
		
		return true;
	}
	
}
