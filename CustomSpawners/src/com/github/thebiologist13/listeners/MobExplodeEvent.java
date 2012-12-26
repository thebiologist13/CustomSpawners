package com.github.thebiologist13.listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

@SuppressWarnings("unused")
public class MobExplodeEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobExplodeEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent ev) {
		//Entity
		Entity e = ev.getEntity();
		
		//SpawnableEntity
		SpawnableEntity s = plugin.getEntityFromSpawner(e);
		
		if(s != null) {
			
			ev.setCancelled(true);
			
			if(!wgAllows(e)) 
				return;
			
			Location explosionLoc = ev.getLocation();
			//TODO soon... by that I mean, probably never.
			/*ArrayList<LivingEntity> entities = getNearbyEntities(e, s.getYield() * 2);
			
			for(LivingEntity le : entities) {
				
				if(s.isUsingCustomDamage()) {
					
					le.damage(s.getDamage());
					
				} else {
					
					DamageController.explodingEntities.put(le.getEntityId(), le);
					
				}
				
			}*/
			
			explosionLoc.getWorld().createExplosion(ev.getLocation(), s.getYield(), s.isIncendiary());
			
		}
		
	}
	
	//Check if players are nearby
	private ArrayList<LivingEntity> getNearbyEntities(Entity source, double max) {
		ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>();
		for(Entity e : source.getNearbyEntities(max, max, max)) {
			//Finds distance between spawner and player is 3D space.
			double distance = source.getLocation().distance(e.getLocation());
			if(distance <= max) {
				if(e instanceof LivingEntity) {
					entities.add((LivingEntity) e);
				}
			}
		}
		return entities;
	}
	
	//WorldGuard explosives 
	private boolean wgAllows(Entity e) {
		
		WorldGuardPlugin wg = plugin.getWG();
		
		if(wg == null)
			return true;
		
		ApplicableRegionSet set = wg.getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
		
		if(e.getType().equals(EntityType.PRIMED_TNT) && !set.allows(DefaultFlag.TNT))
			return false;
		
		if(e.getType().equals(EntityType.FIREBALL) && !set.allows(DefaultFlag.GHAST_FIREBALL))
			return false;
		
		if(e.getType().equals(EntityType.SMALL_FIREBALL) && !set.allows(DefaultFlag.GHAST_FIREBALL))
			return false;
		
		if(e.getType().equals(EntityType.CREEPER) && !set.allows(DefaultFlag.CREEPER_EXPLOSION))
			return false;
		
		return true;
	}
	
}
