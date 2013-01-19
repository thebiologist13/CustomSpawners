package com.github.thebiologist13.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class ProjectileFireEvent implements Listener {

	private CustomSpawners plugin = null;
	
	public ProjectileFireEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onProjectileFire(ProjectileLaunchEvent ev) {
		//Entity that fired projectile
		Entity launcher = ev.getEntity().getShooter();
		//Projectile
		Projectile pro = ev.getEntity();
		//SpawnableEntity
		SpawnableEntity e = plugin.getEntityFromSpawner(launcher);
		
		if(e != null) {
			Spawner s = plugin.getSpawnerWithEntity(launcher);
				
			if(s != null) {
				s.addSecondaryMob(pro.getEntityId(), launcher.getEntityId());
			}
			
		}
		
	}

}
