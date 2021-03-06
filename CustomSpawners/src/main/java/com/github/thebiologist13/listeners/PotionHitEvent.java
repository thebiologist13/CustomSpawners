package com.github.thebiologist13.listeners;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.serialization.SPotionEffect;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class PotionHitEvent implements Listener {
	
	private CustomSpawners plugin = null;
	
	public PotionHitEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onHit(PotionSplashEvent ev) {
		//Entity
		Entity entity = ev.getEntity();
		//SpawnableEntity
		SpawnableEntity e = plugin.getEntityFromSpawner(entity.getUniqueId());
		
		if(e != null) {
			
			if(!wgAllows(entity))
				return;
			
			//This just makes it so the potion is under CustomSpawners control only.
			ev.setCancelled(true);
			
			Collection<LivingEntity> affectedEntities = ev.getAffectedEntities();
			
			for(LivingEntity le : affectedEntities) {
				
				SPotionEffect epe = e.getPotionEffect();
				PotionEffect effect = new PotionEffect(epe.getType(), epe.getDuration(), epe.getAmplifier());
				le.addPotionEffect(effect, true);
				
			}
			
		}
		
	}
	
	private boolean wgAllows(Entity e) {
		
		WorldGuardPlugin wg = CustomSpawners.getWG();
		
		if(wg == null)
			return true;
		
		ApplicableRegionSet set = wg.getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
		
		if(!set.allows(DefaultFlag.POTION_SPLASH))
			return false;
		
		return true;
	}
	
}
