package com.github.thebiologist13.serialization;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;

public class SItemStack implements Serializable {

	private static final long serialVersionUID = 3510924320558955033L;
	private int id;
	private int count;
	private short data;

	public SItemStack(int id) {
		this.id = id;
		this.data = 0;
		this.count = 1;
	}
	
	public SItemStack(int id, int count) {
		this.id = id;
		this.data = 0;
		this.count = count;
	}
	
	public SItemStack(int id, short data, int count) {
		this.id = id;
		this.data = data;
		this.count = count;
	}
	
	public SItemStack(ItemStack i) {
		ItemStack newStack = i.clone();
		this.id = newStack.getTypeId();
		this.data = newStack.getDurability();
		this.count = newStack.getAmount();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getData() {
		return data;
	}

	public void setData(short data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public ItemStack toItemStack() {
		return new ItemStack(id, count, data);
	}

}
