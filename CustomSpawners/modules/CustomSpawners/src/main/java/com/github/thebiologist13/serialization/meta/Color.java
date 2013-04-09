package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;

public class Color implements Serializable {
	
	private static final long serialVersionUID = -5191788743046749082L;
	private int r,g,b;
	
	public Color() {
		this(0, 0, 0);
	}
	
	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color(org.bukkit.Color color) {
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}
	
	public org.bukkit.Color getBukkitColor() {
		org.bukkit.Color base = org.bukkit.Color.BLACK;
		base.setRed(r);
		base.setGreen(g);
		base.setBlue(b);
		return base;
	}
	
}
