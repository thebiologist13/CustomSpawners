package com.github.thebiologist13.api;

import java.util.UUID;

import org.bukkit.Location;

public interface ICustomExplosion {
	
	public void detonate();

	public int getDamage();

	public UUID getEntity();

	public Location getLocation();

	public float getPower();

	public int getTNT();

	public boolean isDestroyBlocks();

	public boolean isFire();

	public void setDamage(int damage);

	public void setDestroyBlocks(boolean destroyBlocks);

	public void setEntity(UUID entity);

	public void setFire(boolean fire);

	public void setLocation(Location location);

	public void setPower(float power);
	
}
