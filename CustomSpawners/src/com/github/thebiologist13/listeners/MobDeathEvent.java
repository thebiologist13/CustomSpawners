package com.github.thebiologist13.listeners;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class MobDeathEvent implements Listener {
	
	@EventHandler
	public void onMobDeath(EntityDeathEvent ev) {
		LivingEntity entity = ev.getEntity();
		EntityType type = ev.getEntityType();
		ArrayList<Spawner> validSpawners = new ArrayList<Spawner>();
		
		for(Spawner s : CustomSpawners.spawners) {
			if(s.getTypeData().equals(type)) { //TODO Modify this
				validSpawners.add(s);
			}
		}
		
		for(Spawner s : validSpawners) {
			boolean match = s.onMobDeath(entity);
			if(match) {break;}
		}
	}
}
