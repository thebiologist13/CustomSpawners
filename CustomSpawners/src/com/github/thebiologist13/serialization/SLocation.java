package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SLocation implements Serializable {

	private static final long serialVersionUID = 2991917100510895431L;
	private double x, y, z;
	private String worldName;
	private UUID worldUUID;
	
	public SLocation(World w, double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.worldName = w.getName();
		this.worldUUID = w.getUID();
	}
	
	public SLocation(Location l) {
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
		this.worldName = l.getWorld().getName();
		this.worldUUID = l.getWorld().getUID();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public UUID getWorldUUID() {
		return worldUUID;
	}

	public void setWorldUUID(UUID worldUUID) {
		this.worldUUID = worldUUID;
	}
	
	public Location toLocation() {
		return new Location(Bukkit.getWorld(worldUUID), x, y, z);
	}

}
