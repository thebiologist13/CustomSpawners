package com.github.thebiologist13.api;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public interface ISInventory {
	public void addItem(int slot, ItemStack item);
	
	public void addItem(ItemStack item);
	
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
	
	public boolean hasItem(ItemStack item);
	
	public boolean hasSlot(int slot);
	
	public boolean isEmpty();
	
	public void removeBoot();
	
	public void removeChest();
	
	public void removeHelmet();
	
	public void removeLeg();
	
	public void setArmor(ItemStack[] armor);
	
	public void setBoot(ItemStack boot);
	
	public void setChest(ItemStack chest);
	
	public void setHand(ItemStack hand);
	
	public void setHelmet(ItemStack helmet);
	
	public void setLeg(ItemStack leg);
}
