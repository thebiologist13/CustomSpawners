package com.github.thebiologist13.listeners;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.EntityPotionEffect;
import com.github.thebiologist13.SpawnableEntity;

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
		SpawnableEntity e = plugin.getEntityFromSpawner(entity);
		
		if(e != null) {
			
			//This just makes it so the potion is under CustomSpawners control only.
			ev.setCancelled(true);
			
			Collection<LivingEntity> affectedEntities = ev.getAffectedEntities();
			for(LivingEntity le : affectedEntities) {
				EntityPotionEffect epe = e.getPotionEffect();
				PotionEffect effect = new PotionEffect(epe.getType(), epe.getDuration(), epe.getAmplifier());
				le.addPotionEffect(effect, true);
			}
			
		}
		
	}
	
}
