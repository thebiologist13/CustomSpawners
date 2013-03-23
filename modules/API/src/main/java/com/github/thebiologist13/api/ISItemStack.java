package com.github.thebiologist13.api;

import org.bukkit.inventory.ItemStack;

public interface ISItemStack {
	
	public int getCount();

	public short getData();

	public float getDropChance();

	public int getId();

	public ISItemMeta getMeta();

	public void setCount(int count);
	
	public void setData(short data);

	public void setDropChance(float dropChance);

	public void setId(int id);
	
	public void setMeta(ISItemMeta meta);

	public ItemStack toItemStack();
	
}
