package com.github.thebiologist13.serialization;

import java.io.Serializable;

import org.bukkit.util.Vector;

public class SVector implements Serializable {

	private static final long serialVersionUID = 7453455451776025972L;
	private double x, y, z;
	
	public SVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public SVector(Vector v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}
	
	public Vector toVector() {
		return new Vector(x, y, z);
	}

}
