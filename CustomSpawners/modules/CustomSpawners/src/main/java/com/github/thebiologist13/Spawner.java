package com.github.thebiologist13;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.github.thebiologist13.api.ISpawnManager;
import com.github.thebiologist13.api.ISpawnableEntity;
import com.github.thebiologist13.api.ISpawner;
import com.github.thebiologist13.serialization.SBlock;
import com.github.thebiologist13.serialization.SLocation;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

/**
 * Represents a spawner managed by CustomSpawners.
 * 
 * @author thebiologist13
 */
public class Spawner implements Serializable, ISpawner {
	
	private static final long serialVersionUID = -153105685313343476L;
	
	//Data
	private Map<String, Object> data;
	//Integer is mob ID. This holds the entities that have been spawned so when one dies, it can be removed from maxMobs.
	private ConcurrentHashMap<UUID, SpawnableEntity> mobs;
	//Modifiers. Key is modified property, value is expression.
	private Map<String, String> modifiers;
	//If the block was powered before
	private boolean poweredBefore;
	//If there was a player near it before.
	private boolean playersBefore;
	//Secondary Mobs (passenger, projectiles, etc.). Key is mobId, value is associated mob from "mobs".
	private ConcurrentHashMap<UUID, UUID> secondaryMobs;
	//Ticks left before next spawn
	private int ticksLeft; 
	//Times to spawn at
	private List<Integer> times;
	//Spawnable Mobs
	private List<Integer> typeData;
	
	public Spawner(SpawnableEntity type, Location loc, int id) {
		this(type, loc, "", id);
	}
	
	public Spawner(SpawnableEntity type, Location loc, String name, int id) {
		this.data = new HashMap<String, Object>();
		this.mobs = new ConcurrentHashMap<UUID, SpawnableEntity>();
		this.modifiers = new HashMap<String, String>();
		this.poweredBefore = false;
		this.playersBefore = false;
		this.secondaryMobs = new ConcurrentHashMap<UUID, UUID>();
		this.ticksLeft = -1;
		this.times = new ArrayList<Integer>();
		this.typeData = new ArrayList<Integer>();
		
		SLocation[] areaPoints = new SLocation[2];
		
		areaPoints[0] = new SLocation(loc);
		areaPoints[1] = new SLocation(loc);
		
		this.data.put("id", id);
		this.data.put("loc", new SLocation(loc));
		this.data.put("block", new SBlock(loc.getBlock()));
		this.data.put("areaPoints", areaPoints);
		this.typeData.add(type.getId());
		this.data.put("converted", false);
		this.data.put("useSpawnArea", false);
		this.data.put("name", name);
	}
	
	@Override
	public void addMob(UUID mobId, ISpawnableEntity entity) {
		mobs.put(mobId, (SpawnableEntity) entity);
	}
	
	public void addModifier(String property, String expression) {
		modifiers.put(property, expression);
	}

	@Override
	public void addSecondaryMob(UUID secId, UUID mobId) {
		secondaryMobs.put(secId, mobId);
	}
	
	public void addTime(int time) {
		times.add(time);
	}
	
	public void addTypeData(SpawnableEntity data) {
		typeData.add(data.getId());
	}
	
	public void changeAreaPoint(int index, Location value) {
		if(index != 0 || index != 1) {
			return;
		}
		
		SLocation[] ap1 = (SLocation[]) this.data.get("areaPoints");
		ap1[index] = new SLocation(value);
		
		this.data.put("areaPoints", ap1);
	}
	
	public double evaluate(String expr) {
		double maxDis = (this.data.containsKey("maxDistance")) ? Double.parseDouble(this.data.get("maxDistance").toString()) : 0;
		expr = expr.replaceAll("@mobs", "" + mobs.size());
		expr = expr.replaceAll("@mps", "" + 
				((this.data.containsKey("mobsPerSpawn")) ? (Integer) this.data.get("mobsPerSpawn") : 1));
		expr = expr.replaceAll("@maxdis", "" + maxDis);
		expr = expr.replaceAll("@mindis", "" + 
				((this.data.containsKey("minDistance")) ? Double.parseDouble(this.data.get("minDistance").toString()) : 0));
		expr = expr.replaceAll("@maxlight", "" + 
				((this.data.containsKey("maxLight")) ? (Byte) this.data.get("maxLight") : 0));
		expr = expr.replaceAll("@minlight", "" + 
				((this.data.containsKey("minLight")) ? (Byte) this.data.get("minLight") : 0));
		expr = expr.replaceAll("@radius", "" + 
				((this.data.containsKey("radius")) ? Double.parseDouble(this.data.get("radius").toString()) : 0));
		expr = expr.replaceAll("@rate", "" + 
				((this.data.containsKey("rate")) ? (Integer) this.data.get("rate") : 60));
		expr = expr.replaceAll("@players", "" + Bukkit.getServer().getOnlinePlayers().length);
		expr = expr.replaceAll("@nearplayers", "" + CustomSpawners.getNearbyPlayers(getLoc(), maxDis).size());
		expr = expr.replaceAll("@light", "" + getLight());
		
		return CustomSpawners.evaluate(expr);
	}
	
	//Spawn the mobs
	@Override
	public void forceSpawn() {
		
		ISpawnManager man = CustomSpawners.getSpawnManager(this);
		man.forceSpawn();
		
	}
	
	//Spawn a mob on the spawner.
	@Override
	public Entity forceSpawnOnLoc(ISpawnableEntity entity, Location loc) {
		
		ISpawnManager man = CustomSpawners.getSpawnManager(this);
		return man.spawnMobAt(entity, loc);
		
	}
	
	//Spawn the mobs
	@Override
	public void forceSpawnType(ISpawnableEntity entity) {

		ISpawnManager man = CustomSpawners.getSpawnManager(this);
		man.forceSpawn(entity);
		
	}

	@Override
	public Location[] getAreaPoints() {
		
		Location[] ap1 = new Location[2];
		SLocation[] ap = ((SLocation[]) this.data.get("areaPoints"));
		
		for(int i = 0; i < ap.length; i++) {
			ap1[i] = ap[i].toLocation();
		}
		
		return ap1;
	}

	@Override
	public Block getBlock() {
		return ((SBlock) this.data.get("block")).toBlock();
	}

	@Override
	public byte getBlockData() {
		return ((SBlock) this.data.get("block")).getData();
	}

	@Override
	public int getBlockId() {
		return ((SBlock) this.data.get("block")).getId();
	}

	@Override
	public Map<String, Object> getData() {
		return data;
	}

	@Override
	public int getId() {
		return (Integer) this.data.get("id");
	}

	@Override
	public Location getLoc() {
		Location loc = ((SLocation) this.data.get("loc")).toLocation();
		loc.setX(loc.getBlockX() + 0.5f);
		loc.setY(loc.getBlockY() + 0.5f);
		loc.setZ(loc.getBlockZ() + 0.5f);
		return loc;
	}

	@Override
	public ISpawnableEntity getMainEntity() {
		return CustomSpawners.getEntity(this.typeData.get(0).toString());
	}

	@Override
	public byte getMaxLightLevel() {
		byte maxLight = (this.data.containsKey("maxLight")) ? (Byte) this.data.get("maxLight") : 0;
		if(hasModifier("maxlight")) {
			String expr = getModifier("maxlight");
			try {
				int rawLight = (int) Math.abs(Math.round(evaluate(expr)));
				if(rawLight > 15)
					rawLight = 15;
				maxLight = (byte) rawLight;
			} catch(IllegalArgumentException e) {}
		}
		return maxLight;
	}

	@Override
	public int getMaxMobs() {
		int maxMobs = (this.data.containsKey("maxMobs")) ? (Integer) this.data.get("maxMobs") : 1;
		if(hasModifier("maxmobs")) {
			String expr = getModifier("maxmobs");
			try {
				maxMobs = (int) Math.abs(Math.round(evaluate(expr)));
			} catch(IllegalArgumentException e) {}
		}
		return maxMobs;
	}

	@Override
	public double getMaxPlayerDistance() {
		double value = (this.data.containsKey("maxDistance")) ? Double.parseDouble(this.data.get("maxDistance").toString()) : 0;
		if(hasModifier("maxdistance")) {
			String expr = getModifier("maxdistance");
			try {
				value = evaluate(expr);
			} catch(IllegalArgumentException e) {}
		}
		return value;
	}

	@Override
	public byte getMinLightLevel() {
		byte minLight = (this.data.containsKey("minLight")) ? (Byte) this.data.get("minLight") : 0;
		if(hasModifier("minlight")) {
			String expr = getModifier("minlight");
			try {
				int rawLight = (int) Math.abs(Math.round(evaluate(expr)));
				if(rawLight > 15)
					rawLight = 15;
				minLight = (byte) rawLight;
			} catch(IllegalArgumentException e) {}
		}
		return minLight;
	}

	@Override
	public double getMinPlayerDistance() {
		double value = (this.data.containsKey("minDistance")) ? Double.parseDouble(this.data.get("minDistance").toString()) : 0;
		if(hasModifier("mindistance")) {
			String expr = getModifier("mindistance");
			try {
				value = evaluate(expr);
			} catch(IllegalArgumentException e) {}
		}
		return value;
	}
	
	public Map<UUID, SpawnableEntity> getMobs() {
		return this.mobs;
	}
	
	@Override
	public Map<UUID, Integer> getMobsIds() {
		Map<UUID, Integer> mobsIds = new HashMap<UUID, Integer>();
		for(UUID id : mobs.keySet()) {
			mobsIds.put(id, mobs.get(id).getId());
		}
		return mobsIds;
	}

	@Override
	public int getMobsPerSpawn() {
		int value = (this.data.containsKey("mobsPerSpawn")) ? (Integer) this.data.get("mobsPerSpawn") : 1;
		if(hasModifier("mps")) {
			String expr = getModifier("mps");
			try {
				value = (int) Math.abs(Math.round(evaluate(expr)));
			} catch(IllegalArgumentException e) {}
		}
		return value;
	}

	@Override
	public String getModifier(String prop) {
		return modifiers.get(prop);
	}

	@Override
	public Map<String, String> getModifiers() {
		return modifiers;
	}

	@Override
	public String getName() {
		return (this.data.containsKey("name")) ? (String) this.data.get("name") : "";
	}

	@Override
	public Object getProp(String key) {
		return (this.data.containsKey(key)) ? this.data.get(key) : null;
	}

	@Override
	public double getRadius() {
		double value = (this.data.containsKey("radius")) ? Double.parseDouble(this.data.get("radius").toString()) : 0;
		if(hasModifier("radius")) {
			String expr = getModifier("radius");
			try {
				value = evaluate(expr);
			} catch(IllegalArgumentException e) {}
		}
		return value;
	}

	@Override
	public int getRate() {
		int value = (this.data.containsKey("rate")) ? (Integer) this.data.get("rate") : 60;
		if(hasModifier("rate")) {
			String expr = getModifier("rate");
			try {
				value = (int) Math.abs(Math.round(evaluate(expr)));
			} catch(IllegalArgumentException e) {}
		}
		
		return value;
	}
	
	@Override
	public Map<UUID, UUID> getSecondaryMobs() {
		
		if(this.secondaryMobs == null) {
			this.secondaryMobs = new ConcurrentHashMap<UUID, UUID>();
		}
		
		return this.secondaryMobs;
	}

	@Override
	public List<Integer> getSpawnTimes() {
		return times;
	}

	@Override
	public List<Integer> getTypeData() {
		return this.typeData;
	}
	
	@Override
	public List<ISpawnableEntity> getTypesEntities() {
		List<ISpawnableEntity> types = new ArrayList<ISpawnableEntity>();
		for(Integer i : typeData) {
			types.add(CustomSpawners.getEntity(i));
		}
		return types;
	}

	@Override
	public boolean hasModifier(String property) {
		return modifiers.containsKey(property);
	}

	@Override
	public boolean hasProp(String key) {
		return this.data.containsKey(key);
	}

	@Override
	public boolean hasSpawnTime(int time) {
		if(times.isEmpty())
			return true;
		return times.contains(time);
	}

	@Override
	public boolean isActive() {
		return (this.data.containsKey("active")) ? (Boolean) this.data.get("active") : false;
	}

	@Override
	public boolean isCapped() {
		return (this.data.containsKey("capped")) ? (Boolean) this.data.get("capped") : false;
	}
	
	@Override
	public boolean isConverted() {
		return (Boolean) this.data.get("converted");
	}

	@Override
	public boolean isHidden() {
		return (this.data.containsKey("hidden")) ? (Boolean) this.data.get("hidden") : false;
	}

	@Override
	public boolean isPoweredBefore() {
		return poweredBefore;
	}

	@Override
	public boolean isRedstoneTriggered() {
		return (this.data.containsKey("redstone")) ? (Boolean) this.data.get("redstone") : false;
	}

	@Override
	public boolean isSpawnOnEnter() {
		return (this.data.containsKey("spawnOnEnter")) ? (Boolean) this.data.get("spawnOnEnter") : false;
	}
	
	@Override
	public boolean isSpawnOnRedstone() {
		return (this.data.containsKey("spawnOnRedstone")) ? (Boolean) this.data.get("spawnOnRedstone") : false;
	}

	@Override
	public boolean isTrackNearby() {
		return (this.data.containsKey("trackNearby")) ? (Boolean) this.data.get("trackNearby") : false;
	}
	
	@Override
	public boolean isUsingSpawnArea() {
		return (this.data.containsKey("useSpawnArea")) ? (Boolean) this.data.get("useSpawnArea") : false;
	}

	@Override
	public ISpawnableEntity randType() {
		Random rand = new Random();
		List<Integer> typeData = getTypeData();
		int index = rand.nextInt(typeData.size());
		ISpawnableEntity e = CustomSpawners.getEntity(typeData.get(index).toString());
		if(e == null) {
			return CustomSpawners.defaultEntity;
		}
		return e;
	}
	
	//Remove the spawner
	public void remove() {
		/*
		 * If the ID is -1, it should be removed next tick by not 
		 * calling these functions and removing it's file.
		 * 
		 * Won't happen initially because when the spawner is initialized, it is given an ID
		 */
		data.put("id", -1);
		data.put("active", false);
	}

	public void removeMob(UUID mobId) {
		if(mobs.containsKey(mobId))
			mobs.remove(mobId);
	}
	
	public void removeNulls() {
		for(String s : data.keySet()) {
			if(data.get(s) == null) {
				data.remove(s);
			}
		}
	}

	public void removeSecondaryMob(UUID secId) {
		secondaryMobs.remove(secId);
	}

	public void removeTypeData(SpawnableEntity type) {
		if(typeData.contains(type.getId())) {
			int index = typeData.indexOf(type.getId());
			typeData.remove(index);
		}
		
		if(typeData.size() == 0) {
			typeData.add(CustomSpawners.defaultEntity.getId());
		}

	}

	@Override
	public void setActive(boolean active) {
		this.data.put("active", active);
	}
	
	public void setAreaPoints(Location[] areaPoints) {
		
		if(areaPoints.length != 2)
			return;
		
		SLocation[] ap1 = new SLocation[2];
		
		for(int i = 0; i < areaPoints.length; i++) {
			ap1[i] = new SLocation(areaPoints[i]);
		}
		
		this.data.put("areaPoints", ap1);
	}

	public void setBlock(Block block) {
		this.data.put("block", new SBlock(block));
	}

	public void setCapped(boolean capped) {
		this.data.put("capped", capped);
	}
	
	@Override
	public void setConverted(boolean converted) {
		this.data.put("converted", converted);
	}

	public void setData(Map<String, Object> data) {
		
		if(data == null)
			return;
		
		for(String s : data.keySet()) {
			
			if(s.equals("id") || s.equals("name"))
				continue;
			
			this.data.put(s, data.get(s));
		}
		
	}
	
	public void setHidden(boolean hidden) {
		this.data.put("hidden", hidden);
	}
	
	public void setLoc(Location loc) {
		this.data.put("loc", new SLocation(loc));
		setBlock(loc.getBlock());
	}
	
	public void setMaxLightLevel(byte maxLightLevel) {
		this.data.put("maxLight", maxLightLevel);
	}
	
	public void setMaxMobs(int maxMobs) {
		this.data.put("maxMobs", maxMobs);
	}

	public void setMaxPlayerDistance(double maxPlayerDistance) {
		this.data.put("maxDistance", maxPlayerDistance);
	}

	public void setMinLightLevel(byte minLightLevel) {
		this.data.put("minLight", minLightLevel);
	}
	
	public void setMinPlayerDistance(double minPlayerDistance) {
		this.data.put("minDistance", minPlayerDistance);
	}
	
	public void setMobs(ConcurrentHashMap<UUID, SpawnableEntity> mobParam) {
		this.mobs = mobParam;
	}
	
	public void setMobsPerSpawn(int mobsPerSpawn) {
		this.data.put("mobsPerSpawn", mobsPerSpawn);
	}
	
	public void setModifiers(Map<String, String> modifiers) {
		this.modifiers = modifiers;
	}

	public void setName(String name) {
		name = ChatColor.translateAlternateColorCodes('&', name);
		
		name = name.replaceAll("__", " ");
		
		this.data.put("name", name);
	}

	public void setPoweredBefore(boolean poweredBefore) {
		this.poweredBefore = poweredBefore;
	}
	
	public void setProp(String key, Object value) {
		if(key == null || value == null) {
			return;
		}
		
		this.data.put(key, value);
	}

	public void setRadius(double radius) {
		this.data.put("radius", radius);
	}
	
	public void setRate(int rate) {
		this.data.put("rate", rate);
		this.ticksLeft = rate;
	}
	
	public void setRedstoneTriggered(boolean redstoneTriggered) {
		this.data.put("redstone", redstoneTriggered);
	}
	
	public void setSecondaryMobs(Map<UUID, UUID> secondaryMobs) {
		this.secondaryMobs = (ConcurrentHashMap<UUID, UUID>) secondaryMobs;
	}
	
	public void setSpawnOnEnter(boolean value) {
		this.data.put("spawnOnEnter", value);
	}
	
	public void setSpawnOnRedstone(boolean value) {
		this.data.put("spawnOnRedstone", value);
	}
	
	public void setSpawnTimes(List<Integer> times) {
		this.times = times;
	}
	
	public void setTrackNearby(boolean value) {
		this.data.put("trackNearby", value);
	}
	
	public void setTypeData(List<Integer> typeDataParam) {
		
		if(typeDataParam == null) {
			return;
		}
		
		this.typeData = typeDataParam;
	}
	
	public void setUseSpawnArea(boolean useSpawnArea) {
		this.data.put("useSpawnArea", useSpawnArea);
	}
	
	//Spawn the mobs
	@Override
	public void spawn() {
		spawn(isActive());
	}
	
	public void spawn(boolean isActive) {
		//If the spawner is not active, return
		if(!isActive) {
			return;
		}
		
		if(!wgAllows(getLoc()))
			return;
		
		ISpawnManager man = CustomSpawners.getSpawnManager(this);
		man.spawn();
	}
	
	//Tick the spawn rate down and spawn mobs if it is time to spawn. Return the ticks left.
	public int tick() {
		
		int rate = getRate();
		int time = (int) getLoc().getWorld().getTime();
		for(Integer i : times) {
			if(i.intValue() == time) {
				ticksLeft = rate;
				spawn(true);
				return 0;
			}
		}
		
		boolean playersNow = (CustomSpawners.getNearbyPlayers(getLoc(), getMaxPlayerDistance()).size() > 0);
		
		if(isSpawnOnEnter() && playersNow && !playersBefore) {
			ticksLeft = rate;
			playersBefore = true;
			spawn(true);
			return 0;
		}
		
		if(!playersNow)
			playersBefore = false;
		
		if(!(rate <= 0)) {
			ticksLeft--;
			if(ticksLeft == 0) {
				ticksLeft = rate;
				spawn();
				return 0;
			}
		}
		
		return ticksLeft;
	}

	private byte getLight() {
		Block blk = getBlock();
		Location loc = getLoc();
		byte highest = 0;
		int x = blk.getX();
		int y = blk.getY();
		int z = blk.getZ();
		Block north = loc.getWorld().getBlockAt(x, y, z - 1);
		Block south = loc.getWorld().getBlockAt(x, y, z + 1);
		Block east = loc.getWorld().getBlockAt(x + 1, y, z);
		Block west = loc.getWorld().getBlockAt(x - 1, y, z);
		Block up = loc.getWorld().getBlockAt(x, y + 1, z);
		Block down = loc.getWorld().getBlockAt(x, y - 1, z);
		Block[] blocks = {north, south, east, west, up, down};
		for(Block b : blocks) {
			byte level = b.getLightLevel();
			if(level > highest) {
				highest = level;
			}
		}

		return highest;
	}
	
	private boolean wgAllows(Location loc) {
		WorldGuardPlugin wg = CustomSpawners.getWG();

		if(wg == null)
			return true;

		ApplicableRegionSet set = wg.getRegionManager(loc.getWorld()).getApplicableRegions(loc);

		if(!set.allows(DefaultFlag.MOB_SPAWNING))
			return false;

		return true;
	}
	
}
