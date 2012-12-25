package com.github.thebiologist13.listeners;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.serialization.SInventory;
import com.github.thebiologist13.serialization.SItemStack;

public class MobDeathEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobDeathEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMobDeath(EntityDeathEvent ev) {
		Entity entity = ev.getEntity();
		SpawnableEntity e = plugin.getEntityFromSpawner(entity);
		
		plugin.removeMob(entity);
		
		DamageController.angryMobs.remove(entity.getEntityId());
		
		if(e != null) {
			
			//Custom Drops
			if(e.isUsingCustomDrops()) {
				ev.getDrops().clear();
				Iterator<ItemStack> itr = e.getDrops().iterator();
				while(itr.hasNext()) ev.getDrops().add(itr.next());
			} else if(!e.getInventory().isEmpty()) {
				ev.getDrops().clear();
				SInventory inv = e.getInventory();
				Collection<SItemStack> items = inv.getContent().values();
				
				for(SItemStack s : items) {
					ItemStack i = s.toItemStack();
					ev.getDrops().add(i);
				}
				
				for(ItemStack i : inv.getArmor()) {
					ev.getDrops().add(i);
				}

				ev.getDrops().add(inv.getHand());
				
			}
			
			//Exp
			ev.setDroppedExp(e.getDroppedExp());
			
		}
		
	}
	
}
