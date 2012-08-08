package com.github.thebiologist13.listeners;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class MobDamageEvent implements Listener {
	
	//The key is an entity ID. The value is ticks left. If an entity ID is in this list, it has caught fire from somewhere else (other than fire ticks prop.) and should take fire damage.
	public static ConcurrentHashMap<Integer, Integer> negatedFireImmunity = new ConcurrentHashMap<Integer, Integer>();
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ev) {
		//Damaged Entity
		Entity damaged = ev.getEntity();
		//Entity ID
		int entityId = damaged.getEntityId();
		//EntityType
		EntityType type = damaged.getType();
		//Match
		boolean match = false;
		//Iterator for spawners 
		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();
		
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			boolean validSpawner = false;
			
			for(SpawnableEntity e : s.getTypeData().values()) {
				if(e.getType().equals(type)) {
					validSpawner = true;
					break;
				}
			}
			
			if(validSpawner) {
				Iterator<Integer> mobItr = s.getMobs().keySet().iterator();
				Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
				
				while(mobItr.hasNext()) {
					int current = mobItr.next();
					
					if(current == entityId) {
						match = true;
						SpawnableEntity e = s.getMobs().get(current);
						
						if(!takeSpawnerFireDamage(e, ev)) {
							ev.setCancelled(true);
							break;
						}
						
						if(!takeDamage(e, ev)) {
							ev.setDamage(0);
						}
						
						break;
					}
				}
				
				while(passiveMobItr.hasNext()) {
					int current = passiveMobItr.next();
					
					if(current == entityId) {
						match = true;
						SpawnableEntity e = s.getPassiveMobs().get(current);
						
						if(!takeSpawnerFireDamage(e, ev)) {
							ev.setCancelled(true);
							break;
						}
						
						if(!takeDamage(e, ev)) {
							ev.setDamage(0);
						}
						
						break;
					}
				}
				
				if(match) {
					break;
				}
			}
		}
		
	}
	
	private boolean takeDamage(SpawnableEntity e, EntityDamageEvent ev) {
		//Damage Cause
		DamageCause cause = ev.getCause();
		
		/*
		 * Blacklist items are the only ones it DOES NOT take damage from.
		 * Whitelist items are the only ones it DOES take damage from.
		 * Check item damage in MobDamageByEntityEvent.
		 */
		if(e.isUsingBlacklist()) {
			if(e.getDamageBlacklist().contains(cause.name())) {
				return false;
			}
			
			return true;
		} else if(e.isUsingWhitelist()) {
			if(e.getDamageWhitelist().contains(cause.name())) {
				return true;
			}
			
			return false;
		}
		
		return true;
	}
	
	private boolean takeSpawnerFireDamage(SpawnableEntity e, EntityDamageEvent ev) {
		//Damage Cause
		DamageCause cause = ev.getCause();
		//ID
		int id = ev.getEntity().getEntityId();
		
		if(e.isUsingBlacklist()) {
			if(e.getDamageBlacklist().contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
				if(negatedFireImmunity.containsKey(id) && !e.getDamageBlacklist().contains(DamageCause.FIRE_TICK.name())) {
					int newTicks = negatedFireImmunity.get(id) - 1;
					negatedFireImmunity.replace(id, newTicks);
					return true;
				} else {
					return false;
				}
			}
			return true;
		} else if(e.isUsingWhitelist()) {
			if(e.getDamageWhitelist().contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
				if(negatedFireImmunity.containsKey(id) && e.getDamageWhitelist().contains(DamageCause.FIRE_TICK.name())) {
					int newTicks = negatedFireImmunity.get(id) - 1;
					negatedFireImmunity.replace(id, newTicks);
					return true;
				} else {
					return false;
				}
			}
			
			return false;
		}
		
		return true;
	}
	
}
