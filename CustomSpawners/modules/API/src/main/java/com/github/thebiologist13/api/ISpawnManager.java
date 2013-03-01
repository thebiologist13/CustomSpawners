package com.github.thebiologist13.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface ISpawnManager {
	
	public void spawn();
	
	public void forceSpawn();
	
	public void forceSpawn(ISpawnableEntity entity);
	
	public Entity spawnMobAt(ISpawnableEntity entity, Location location);
	
}
