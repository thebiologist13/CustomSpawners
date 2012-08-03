package com.github.thebiologist13.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class MobDeathEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobDeathEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMobDeath(EntityDeathEvent ev) {
		LivingEntity entity = ev.getEntity();
		EntityType type = ev.getEntityType();
		ArrayList<Spawner> validSpawners = new ArrayList<Spawner>();
		
		for(Spawner s : CustomSpawners.spawners) {
			for(SpawnableEntity e : s.getTypeData().values()) {
				if(e.getType().equals(type)) {
					validSpawners.add(s);
					break;
				}
			}
		}
		
		plugin.removeMob(entity, validSpawners);
		
		Iterator<Integer> angryMobItr = CustomSpawners.angryMobs.iterator();
		while(angryMobItr.hasNext()) {
			int angryMob = angryMobItr.next();
			
			if(angryMob == entity.getEntityId()) {
				angryMobItr.remove();
				break;
			}
		}
	}
	
}
