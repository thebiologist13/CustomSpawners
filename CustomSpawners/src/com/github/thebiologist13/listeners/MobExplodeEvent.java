package com.github.thebiologist13.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class MobExplodeEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public MobExplodeEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent ev) {
		//Entity
		Entity e = ev.getEntity();
		
		if(e == null) 
			return;
		
		int id = e.getEntityId();
		
		//SpawnableEntity
		SpawnableEntity s = plugin.getEntityFromSpawner(id);
		
		if(s != null) {
			
			ev.setCancelled(true);
			
			if(!wgAllows(e)) 
				return;
			
			Location norm = ev.getLocation();
			Location plusOne = new Location(norm.getWorld(), norm.getX(), norm.getY() + 1, norm.getZ() + 1);
			CustomExplosion ex;
			if(s.isUsingCustomDamage()) {
				ex = new CustomExplosion(plusOne, s.getYield(), id, s.getDamage(e), s.isIncendiary());
			} else {
				ex = new CustomExplosion(plusOne, s.getYield(), id, 0, s.isIncendiary());
			}
			ex.detonate();
			
			DamageController.explode.put(ex.getTNT(), ex);
			
		}
		
	}
	
	//WorldGuard explosives 
	private boolean wgAllows(Entity e) {
		
		WorldGuardPlugin wg = CustomSpawners.getWG();
		
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
