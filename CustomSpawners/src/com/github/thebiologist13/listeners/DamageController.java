package com.github.thebiologist13.listeners;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.github.thebiologist13.SpawnableEntity;

public class DamageController {
	
	//The key is an entity ID. The value is ticks left. If an entity ID is in this list, it has caught fire from somewhere else (other than fire ticks prop.) and should take fire damage.
	public static ConcurrentHashMap<Integer, Integer> negatedFireImmunity = new ConcurrentHashMap<Integer, Integer>();
		
	//Hashmap for extra health
	public static ConcurrentHashMap<Integer, Integer> extraHealthEntities = new ConcurrentHashMap<Integer, Integer>();
		
	//HashMap for extra damage entities
	//TODO any spawned entity with a damage mod MUST be added to this.
	public static ConcurrentHashMap<Integer, Integer> damageModEntities = new ConcurrentHashMap<Integer, Integer>();
	
	/*
	 * Before it takes damage, it must be an allowed type of damage. Then
	 * it has to be checked if it has a a damage mod. Finally detract normal
	 * health unless it has extra health.
	 */
	
	/*
	 * Assumes that the entity is already from a spawner.
	 */
	public void doDamage(EntityDamageEvent ev, SpawnableEntity e) {
		
		DamageCause cause = ev.getCause();
		Entity entity = ev.getEntity();
		int mobId = entity.getEntityId();
		int damage = ev.getDamage();
		
		if(e != null) {
			//If it doesn't return from these statements, the damage is valid
			if(e.isUsingBlacklist()) {
				ArrayList<String> black = e.getDamageBlacklist();
				
				if(black.contains(cause.name())) {
					
					ev.setCancelled(true);
					return;
					
				} else if(black.contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
					
					int id = entity.getEntityId();
					
					if(negatedFireImmunity.containsKey(id) && !e.getDamageBlacklist().contains(DamageCause.FIRE_TICK.name())) {
						int newTicks = negatedFireImmunity.get(id) - 1;
						negatedFireImmunity.replace(id, newTicks);
					}
					
					ev.setCancelled(true);
					return;
					
				} else if(black.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent)) {
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					
					if(eve.getDamager() instanceof Player) {
						Player p = (Player) eve.getDamager();
						if(e.getItemDamageList().contains(p.getItemInHand())) {
							ev.setCancelled(true);
							return;
						}
						
					}
					
				}
				
			} else if(e.isUsingWhitelist()) {
				
				ArrayList<String> white = e.getDamageBlacklist();
				
				if(!white.contains(cause.name())) {
					
					ev.setCancelled(true);
					return;
					
				} else if(!(white.contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK))) {
					
					int id = entity.getEntityId();
					
					if(negatedFireImmunity.containsKey(id) && !e.getDamageBlacklist().contains(DamageCause.FIRE_TICK.name())) {
						int newTicks = negatedFireImmunity.get(id) - 1;
						negatedFireImmunity.replace(id, newTicks);
					}
					
					ev.setCancelled(true);
					return;
					
				} else if(!(white.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent))) {
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					
					if(eve.getDamager() instanceof Player) {
						Player p = (Player) eve.getDamager();
						if(e.getItemDamageList().contains(p.getItemInHand())) {
							ev.setCancelled(true);
							return;
						}
						
					}
					
				}
				
			}
			
		}
		
		/*
		 * Here we check if it was damaged by an SpawnableEntity. If the entity was from a spawner,
		 * apply the damage mod.
		 */
		
		if(e.isUsingCustomDamage()) {
			if(ev instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
				int damagerId = eve.getDamager().getEntityId();
				
				if(damageModEntities.containsKey(damagerId)) {
					damage = damageModEntities.get(damagerId);
				}

			}
			
		}
		
		/*
		 * Finally apply the damage to extra health and health accordingly.
		 */
		if(DamageController.extraHealthEntities.containsKey(mobId)) {
			//Get the remaining extra health after "damage"
			int newExtraHealth = DamageController.extraHealthEntities.get(mobId) - damage;
			//If the new extra health <= 0, deal actual damage to entity
			if(newExtraHealth <= 0) {
				ev.setDamage(Math.abs(newExtraHealth));
				DamageController.extraHealthEntities.remove(mobId);
			} else {
				ev.setDamage(0);
				DamageController.extraHealthEntities.replace(mobId, newExtraHealth);
			}
		} else {
			ev.setDamage(damage);
		}
		
	}
	
}
