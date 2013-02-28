package com.github.thebiologist13.listeners;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CustomExplosion {
	
	private int damage;
	private boolean destroyBlocks;
	private UUID entity;
	private boolean fire;
	private Location location;
	private float power;
	private int tnt;
	
	public CustomExplosion(Location location, float power, UUID entityId) {
		this(location, power, entityId, 0);
	}
	
	public CustomExplosion(Location location, float power, UUID entityId, int damage) {
		this(location, power, entityId, damage, false);
	}
	
	public CustomExplosion(Location location, float power, UUID entityId, int damage, boolean fire) {
		this(location, power, entityId, damage, fire, true);
	}
	
	public CustomExplosion(Location location, float power, UUID entityId, int damage, boolean fire, boolean destroyBlocks) {
		this.location = location;
		this.power = power;
		this.entity = entityId;
		this.damage = damage;
		this.fire = fire;
		this.destroyBlocks = destroyBlocks;
	}
	
	public void detonate() {
		TNTPrimed tnt = (TNTPrimed) location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
		if(destroyBlocks == false) {
			tnt.setYield(0);
		} else {
			tnt.setYield(power);
		}
		tnt.setIsIncendiary(fire);
		tnt.setFuseTicks(0);
		this.tnt = tnt.getEntityId();
	}

	public int getDamage() {
		return damage;
	}

	public UUID getEntity() {
		return entity;
	}

	public Location getLocation() {
		return location;
	}

	public float getPower() {
		return power;
	}

	public int getTNT() {
		return tnt;
	}

	public boolean isDestroyBlocks() {
		return destroyBlocks;
	}

	public boolean isFire() {
		return fire;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setDestroyBlocks(boolean destroyBlocks) {
		this.destroyBlocks = destroyBlocks;
	}

	public void setEntity(UUID entity) {
		this.entity = entity;
	}

	public void setFire(boolean fire) {
		this.fire = fire;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setPower(float power) {
		this.power = power;
	}
	
}
