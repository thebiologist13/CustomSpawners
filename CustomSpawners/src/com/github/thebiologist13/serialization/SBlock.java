package com.github.thebiologist13.serialization;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class SBlock implements Serializable {

	private static final long serialVersionUID = -8243960161592452259L;
	private int id;
	private byte data;
	private SLocation location;
	
	public SBlock(int id) {
		this.id = id;
		this.data = 0;
		this.location = null;
	}
	
	public SBlock(int id, byte data) {
		this.id = id;
		this.data = data;
		this.location = null;
	}
	
	public SBlock(int id, byte data, Location location) {
		this.id = id;
		this.data = data;
		this.location = new SLocation(location);
	}
	
	public SBlock(int id, byte data, SLocation location) {
		this.id = id;
		this.data = data;
		this.location = location;
	}
	
	public SBlock(Block b) {
		this.id = b.getTypeId();
		this.data = b.getData();
		this.location = new SLocation(b.getLocation());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getData() {
		return data;
	}

	public void setData(byte data) {
		this.data = data;
	}
	
	public Block toBlock() {
		Block b = location.getWorld().getBlockAt(location.toLocation());
		return b;
	}
	
}
