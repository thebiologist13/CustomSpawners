package com.github.thebiologist13.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class MobDeathEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobDeathEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMobDeath(EntityDeathEvent ev) {
		Entity entity = ev.getEntity();
		EntityType type = ev.getEntityType();
		ArrayList<Spawner> validSpawners = new ArrayList<Spawner>();
		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();
		SpawnableEntity e = plugin.getEntityFromSpawner(entity);
		
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			
			for(SpawnableEntity e1 : s.getTypeData().values()) {
				if(e1.getType().equals(type)) {
					validSpawners.add(s);
					break;
				}
			}
		}
		
		plugin.removeMob(entity, validSpawners);
		
		if(e != null) {
			
			//Custom Drops
			if(e.isUsingCustomDrops()) {
				ev.getDrops().clear();
				Iterator<ItemStack> itr = e.getDrops().iterator();
				while(itr.hasNext()) ev.getDrops().add(itr.next());
			}
			
			//Exp
			ev.setDroppedExp(e.getDroppedExp());
			
		}
		
	}
	
}
