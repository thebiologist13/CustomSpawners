package com.github.thebiologist13.listeners;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
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
	
	//Map for aggroed mobs (entity id, spawnableEntity)
	public static ConcurrentHashMap<Integer, Integer> angryMobs = new ConcurrentHashMap<Integer, Integer>();
	
	public static ConcurrentHashMap<Integer, CustomExplosion> explode = new ConcurrentHashMap<Integer, CustomExplosion>();
	
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
				int id = damager.getEntityId();
				
				SpawnableEntity e = plugin.getEntityFromSpawner(id);
				
				if(e != null) {
					
					if(cause.equals(DamageCause.ENTITY_EXPLOSION)) {
						ev.setCancelled(true);
						return 0;
					}
					
					return e.getDamage();
				} else {
					
					if(explode.containsKey(id)) {
						damage = explode.get(id).getDamage();
						if(damage == -1) {
							damage = ev.getDamage();
						}
						
					}
					
				}
				
			}
			
		} else {
			
			SpawnableEntity e = plugin.getEntityFromSpawner(entity.getEntityId());
			
			if(e == null) {
				return damage;
			}
			
			if(!wgAllows(entity)) {
				ev.setCancelled(true);
				return 0;
			}
			
			if(ev instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
				Entity damager = eve.getDamager();
				int id = damager.getEntityId();
				
				if(damager instanceof Player) {
					angryMobs.put(mobId, e.getId());
				}
				
				SpawnableEntity e1 = plugin.getEntityFromSpawner(id);
				
				if(e1 != null && cause.equals(DamageCause.ENTITY_EXPLOSION)) {
					ev.setCancelled(true);
					return 0;
				}
				
				if(explode.containsKey(id)) {
					damage = explode.get(id).getDamage();
					if(damage == -1) {
						damage = ev.getDamage();
					}
					
				}
				
			}
			
			if(e.isInvulnerable()) {
				ev.setCancelled(true);
				return 0;
			}
			
			if(e.isUsingBlacklist()) { //Things to not take damage from
				List<String> black = e.getDamageBlacklist();
				
				if(black.contains(cause.name().toUpperCase())) {
					
					return 0;
					
				} else if(black.contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
					
					if(negatedFireImmunity.containsKey(mobId)) {
						int newTicks = negatedFireImmunity.get(mobId) - 1;
						
						if(newTicks == 0) {
							negatedFireImmunity.remove(mobId);
						}
						
						negatedFireImmunity.replace(mobId, newTicks);
					} else {
						negatedFireImmunity.put(mobId, e.getFireTicks());
					}
					
					return 0;
					
				} else if(black.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent)) {
					
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					Entity damager = eve.getDamager();
					
					if(damager instanceof Player) {
						
						Player p = (Player) damager;
						
						for(ItemStack i : e.getItemDamageList()) {
							ItemStack hand = p.getItemInHand();
							if(hand.getTypeId() == i.getTypeId() && hand.getData().getData() == i.getData().getData()) {
								return 0;
							}
						}
						
					}
					
				}
				
			} else if(e.isUsingWhitelist()) {
				
				List<String> white = e.getDamageBlacklist();
				
				if(!white.contains(cause.name().toUpperCase())) {
					
					return 0;
					
				} else if(!white.contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
					
					if(negatedFireImmunity.containsKey(mobId)) {
						int newTicks = negatedFireImmunity.get(mobId) - 1;
						
						if(newTicks == 0) {
							negatedFireImmunity.remove(mobId);
						}
						
						negatedFireImmunity.replace(mobId, newTicks);
					} else {
						negatedFireImmunity.put(mobId, e.getFireTicks());
					}
					
					return 0;
					
				} else if(!white.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent)) {
					
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					Entity damager = eve.getDamager();
					
					if(damager instanceof Player) {
						
						Player p = (Player) damager;
						
						for(ItemStack i : e.getItemDamageList()) {
							ItemStack hand = p.getItemInHand();
							if(hand.getTypeId() == i.getTypeId() && hand.getData().getData() == i.getData().getData()) {
								return 0;
							}
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
		
		WorldGuardPlugin wg = plugin.getWG();
		
		if(wg == null)
			return true;
		
		ApplicableRegionSet set = wg.getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
		
		if(!set.allows(DefaultFlag.MOB_DAMAGE))
			return false;
		
		return true;
	}
	
}
