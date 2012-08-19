package com.github.thebiologist13.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class MobRegenEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobRegenEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMobRegen(EntityRegainHealthEvent ev) {
		//Entity
		Entity entity = ev.getEntity();
		//EntityID
		int entityId = entity.getEntityId();
		//Amount
		int regainAmount = ev.getAmount();
		//SpawnableEntity
		SpawnableEntity e = plugin.getEntityFromSpawner(entity);
		
		if(!(e instanceof LivingEntity)) 
			return;
		
		LivingEntity le = (LivingEntity) e;

		if(e != null) {
			int hp = 0;
			if((le.getHealth() + regainAmount) > le.getMaxHealth())
				hp = regainAmount - (le.getMaxHealth() - le.getHealth());
			
			if(DamageController.extraHealthEntities.containsKey(entityId)) {
				DamageController.extraHealthEntities.replace(entityId, DamageController.extraHealthEntities.get(entityId) + hp); 
			} else { 
				DamageController.extraHealthEntities.put(entityId, hp);
			}
			
		}
		
	}
	
}
