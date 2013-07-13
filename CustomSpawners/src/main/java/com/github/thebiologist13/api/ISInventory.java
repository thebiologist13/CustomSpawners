package com.github.thebiologist13.api;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public interface ISInventory {
	public void addItem(int slot, ISItemStack item);
	
	public void addItem(ISItemStack item);
	
	public void clearArmor();
	
	public void clearSlot(int slot);
	
	public void empty();
	
	public ItemStack[] getArmor();
	
	public Map<Integer, ItemStack> getContentBukkit();
	
	public ItemStack getHand();
	
	public ArrayList<ItemStack> getMainInventory();
	
	public ArrayList<ISItemStack> getMainInventoryISItemStacks(); 

	public ItemStack getSlot(int slot);

	public ArrayList<Integer> getSlots(ItemStack item);
	
	public boolean hasHand();
	
	public boolean hasItem(ISItemStack item);
	
	public boolean hasSlot(int slot);
	
	public boolean isEmpty();
	
	public void removeBoot();
	
	public void removeChest();
	
	public void removeHelmet();
	
	public void removeLeg();
	
	public void setArmor(ISItemStack[] armor);
	
	public void setBoot(ISItemStack boot);
	
	public void setChest(ISItemStack chest);
	
	public void setHand(ISItemStack hand);
	
	public void setHelmet(ISItemStack helmet);
	
	public void setLeg(ISItemStack leg);
}
