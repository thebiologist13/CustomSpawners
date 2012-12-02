package com.github.thebiologist13.listeners;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class DamageController {
	
	/*
	 * The key is an entity ID. The value is ticks left. If an entity ID is in this list, 
	 * it has caught fire from somewhere else (other than fire ticks prop.) and should take fire damage.
	 */
	public static ConcurrentHashMap<Integer, Integer> negatedFireImmunity = new ConcurrentHashMap<Integer, Integer>();
		
	//Hashmap for extra health
	public static ConcurrentHashMap<Integer, Integer> extraHealthEntities = new ConcurrentHashMap<Integer, Integer>();
	
	private CustomSpawners plugin = null;
	
	public DamageController(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	//Get the damage to deal
	public int getModifiedDamage(EntityDamageEvent ev) {
		
		DamageCause cause = ev.getCause();
		Entity entity = ev.getEntity();
		int mobId = entity.getEntityId();
		int damage = ev.getDamage();
		
		if(entity instanceof Player) {
			
			if(ev instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
				Entity damager = eve.getDamager();
				
				SpawnableEntity e = plugin.getEntityFromSpawner(damager);
				
				if(e != null) {
					
					if(!wgAllows(entity)) {
						return 0;
					}
					
					return e.getDamage();
				}
				
			}
			
		} else {
			
			SpawnableEntity e = plugin.getEntityFromSpawner(entity);
			
			if(e == null)
				return damage;
			
			if(ev instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
				Entity damager = eve.getDamager();
				
				if(damager instanceof Player) {
					
					Spawner s = plugin.getSpawnerWithEntity(entity);
					
					s.removePassiveMob(mobId);
					s.addMob(mobId, e);
					
				}
				
			}
			
			if(e.isUsingBlacklist()) {
				ArrayList<String> black = e.getDamageBlacklist();
				
				if(black.contains(cause.name())) {
					
					return 0;
					
				} else if(black.contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
					
					if(negatedFireImmunity.containsKey(mobId) && !e.getDamageBlacklist().contains(DamageCause.FIRE_TICK.name())) {
						int newTicks = negatedFireImmunity.get(mobId) - 1;
						negatedFireImmunity.replace(mobId, newTicks);
					}
					
					return 0;
					
				} else if(black.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent)) {
					
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					Entity damager = eve.getDamager();
					
					if(damager instanceof Player) {
						
						Player p = (Player) damager;
						
						if(e.getItemDamageList().contains(p.getItemInHand())) {
							
							return 0;
							
						}
						
					}
					
				}
				
			} else if(e.isUsingWhitelist()) {
				
				ArrayList<String> white = e.getDamageBlacklist();
				
				if(white.contains(cause.name())) {
					
					return damage;
					
				} else if(white.contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
					
					if(negatedFireImmunity.containsKey(mobId) && !e.getDamageBlacklist().contains(DamageCause.FIRE_TICK.name())) {
						int newTicks = negatedFireImmunity.get(mobId) - 1;
						negatedFireImmunity.replace(mobId, newTicks);
					}
					
					return damage;
					
				} else if(white.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent)) {
					
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					Entity damager = eve.getDamager();
					
					if(damager instanceof Player) {
						
						Player p = (Player) damager;
						
						if(e.getItemDamageList().contains(p.getItemInHand())) {
							
							return damage;
							
						}
						
					}
					
				}
				
			}
			
		}
		
		if(extraHealthEntities.containsKey(mobId)) {
			
			int newExtraHealth = extraHealthEntities.get(mobId) - damage;

			if(newExtraHealth <= 0) {
				
				extraHealthEntities.remove(mobId);
				return Math.abs(newExtraHealth);
				
			} else {
				
				extraHealthEntities.replace(mobId, newExtraHealth);
				return 0;
				
			}
			
		} else {
			
			return damage;
			
		}
		
	}
	
	private boolean wgAllows(Entity e) {
		
		if(plugin.worldGuard == null)
			return true;
		
		ApplicableRegionSet set = plugin.worldGuard.getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
		
		if(!set.allows(DefaultFlag.MOB_DAMAGE))
			return false;
		
		return true;
	}
	
}
