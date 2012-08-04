package com.github.thebiologist13.listeners;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	private ArrayList<Integer> preCheckedMobs = new ArrayList<Integer>();
	
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
		for(Integer i : preCheckedMobs) {
			if(entityId == i) {
				match = true;
				break;
			}
		}
		
		//If there isn't a match or return yet, check through all spawners for a match (large list)
		if(!match) {
			//The following "for" loops are just to find out if the mob trying to target was from a spawner.
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
				
				while(passiveMobItr.hasNext()) {
					int currentMob = passiveMobItr.next();
					
					if(currentMob == entityId) {
						match = true;
						preCheckedMobs.add(currentMob);
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
	
}
