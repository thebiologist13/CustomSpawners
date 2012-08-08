package com.github.thebiologist13.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class PlayerTargetEvent implements Listener {
	
	private static ConcurrentHashMap<Integer, SpawnableEntity> preCheckedMobs = new ConcurrentHashMap<Integer, SpawnableEntity>();
	
	@EventHandler
	public void onPlayerTarget(EntityTargetEvent ev) {
		//Entity
		Entity entity = ev.getEntity();
		//EntityType
		EntityType type = entity.getType();
		//Targeted Player
		Entity target = ev.getTarget();
		//Entity ID
		int entityId = entity.getEntityId();
		//Valid Spawners
		ArrayList<Spawner> validSpawners = new ArrayList<Spawner>();
		//If a match has been found
		boolean match = false;
		
		if(target == null) {return;}
		
		if(!(target instanceof Player)) {return;}
		
		//Then see if it has been checked previously (small list)
		for(Integer i : preCheckedMobs.keySet()) {
			if(entityId == i) {
				match = true;
				break;
			}
		}
		
		//If there isn't a match or return yet, check through all spawners for a match (large list)
		if(!match) {
			
			//The following loops are just to find out if the mob trying to target was from a spawner.
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
				Iterator<Integer> passiveMobItr = s.getPassiveMobs().keySet().iterator();
				
				while(passiveMobItr.hasNext()) {
					int currentMob = passiveMobItr.next();
					
					if(currentMob == entityId) {
						match = true;
						preCheckedMobs.put(currentMob, s.getPassiveMobs().get(currentMob));
						break;
					}
				}
				
				if(match) {break;}
				
			}
			
		}
		
		//Set cancelled if there is a match
		if(match) {
			ev.setCancelled(true);
		}
		
	}

	public static ConcurrentHashMap<Integer, SpawnableEntity> getPreCheckedMobs() {
		return preCheckedMobs;
	}
	
}
