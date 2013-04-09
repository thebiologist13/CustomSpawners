package com.github.thebiologist13.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public interface ISpawner {
	
	public void addMob(UUID id, ISpawnableEntity type);
	
	public void addSecondaryMob(UUID mainId, UUID secId);
	
	public void forceSpawn();
	
	public Entity forceSpawnOnLoc(ISpawnableEntity type, Location location);

	public void forceSpawnType(ISpawnableEntity type);

	public Location[] getAreaPoints();

	public Block getBlock();
	
	public byte getBlockData();
	
	public int getBlockId();
	
	public Map<String, Object> getData();
	
	public int getId();
	
	public Location getLoc();

	public ISpawnableEntity getMainEntity();

	public byte getMaxLightLevel();

	public int getMaxMobs();
	
	public double getMaxPlayerDistance();
	
	public byte getMinLightLevel();
	
	public double getMinPlayerDistance();
	
	public Map<UUID, Integer> getMobsIds();
	
	public int getMobsPerSpawn();

	public String getModifier(String prop);

	public Map<String, String> getModifiers();

	public String getName();

	public Object getProp(String key);

	public double getRadius();
	
	public int getRate();
	
	public Map<UUID, UUID> getSecondaryMobs();
	
	public List<Integer> getSpawnTimes();

	public List<Integer> getTypeData();

	public List<ISpawnableEntity> getTypesEntities();

	public boolean hasModifier(String property);

	public boolean hasProp(String key);

	public boolean hasSpawnTime(int time);

	public boolean isActive();

	public boolean isConverted();

	public boolean isHidden();
	
	public boolean isPoweredBefore();
	
	public boolean isRedstoneTriggered();
	
	public boolean isSpawnOnRedstone();
	
	public boolean isUsingSpawnArea();
	
	public ISpawnableEntity randType();
	
	public void spawn();
	
	public void setActive(boolean active);
	
	public void setConverted(boolean converted);
}
