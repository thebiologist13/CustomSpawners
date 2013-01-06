package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_4_6.Material;

import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;

public class SInventory implements Serializable {

	private static final long serialVersionUID = 7492076966612850960L;
	private SItemStack[] armor = new SItemStack[4];
	private HashMap<Integer, SItemStack> content;
	private SItemStack hand = new SItemStack(0);
	
	public SInventory() {
		this.content = new HashMap<Integer, SItemStack>();
		armor[0] = new SItemStack(0);
		armor[1] = new SItemStack(0);
		armor[2] = new SItemStack(0);
		armor[3] = new SItemStack(0);
	}
	
	public SInventory(HashMap<Integer, ItemStack> content) {
		
		HashMap<Integer, SItemStack> content1 = new HashMap<Integer, SItemStack>();
		for(Integer i : content.keySet()) {
			content1.put(i, new SItemStack(content.get(i)));
		}
		
		this.content = content1;
	}
	
	public void addItem(int slot, ItemStack item) {
		content.put(slot, new SItemStack(item));
	}
	
	public void addItem(ItemStack item) {
		List<Integer> conIDs = new ArrayList<Integer>();
		Iterator<Integer> conItr = content.keySet().iterator();
		while(conItr.hasNext()) {
			conIDs.add(conItr.next());
		}
		
		content.put(CustomSpawners.getNextID(conIDs), new SItemStack(item));
	}
	
	public void clearArmor() {
		armor = new SItemStack[4];
	}
	
	public void clearSlot(int slot) {
		if(hasSlot(slot)) {
			content.remove(slot);
		}
	}
	
	public void empty() {
		content = new HashMap<Integer, SItemStack>();
		armor = new SItemStack[4];
		hand = new SItemStack(0);
	}
	
	public ItemStack[] getArmor() {
		
		ItemStack[] armor1 = new ItemStack[4];
		
		for(int i = 0; i < armor.length; i++) {
			armor1[i] = armor[i].toItemStack();
		}
		
		return armor1;
	}
	
	public Map<Integer, SItemStack> getContent() {
		return content;
	}
	
	public ItemStack getHand() {
		return hand.toItemStack();
	}
	
	public ArrayList<ItemStack> getMainInventory() {
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		array.add(0, hand.toItemStack());
		array.add(1, armor[0].toItemStack());
		array.add(2, armor[1].toItemStack());
		array.add(3, armor[2].toItemStack());
		array.add(4, armor[3].toItemStack());
		return array;
	}

	public ItemStack getSlot(int slot) {
		if(hasSlot(slot)) {
			return content.get(slot).toItemStack();
		}
		
		return null;
	}

	public ArrayList<Integer> getSlots(ItemStack item) {
		ArrayList<Integer> slots = new ArrayList<Integer>();
		
		for(Integer i : content.keySet()) {
			if(content.get(i).equals(item)) {
				slots.add(i);
			}
		}
		
		return slots;
		
	}
	
	public boolean hasItem(ItemStack item) {
		return content.values().contains(item);
	}
	
	public boolean hasSlot(int slot) {
		return content.keySet().contains(slot);
	}
	
	public boolean isEmpty() {
		
		boolean mainInventoryEmpty = true;
		List<ItemStack> main = getMainInventory();
		for(ItemStack i : main) {
			if(!i.getType().equals(Material.AIR)) {
				mainInventoryEmpty = false;
			}
		}
		
		return (mainInventoryEmpty && this.content.isEmpty()) ? true : false;
	}
	
	public void removeBoot() {
		armor[0] = new SItemStack(0);
	}
	
	public void removeChest() {
		armor[3] = new SItemStack(0);
	}
	
	public void removeHelmet() {
		armor[3] = new SItemStack(0);
	}
	
	public void removeLeg() {
		armor[1] = new SItemStack(0);
	}
	
	public void setArmor(ItemStack[] armor) {
		
		SItemStack[] armor1 = new SItemStack[4];
		
		for(int i = 0; i < armor.length; i++) {
			armor1[i] = new SItemStack(armor[i]);
		}
		
		this.armor = armor1;
	}
	
	public void setBoot(ItemStack boot) {
		armor[0] = new SItemStack(boot);
	}
	
	public void setChest(ItemStack chest) {
		armor[2] = new SItemStack(chest);
	}
	
	public void setHand(ItemStack hand) {
		this.hand = new SItemStack(hand);
	}
	
	public void setHelmet(ItemStack helmet) {
		armor[3] = new SItemStack(helmet);
	}
	
	public void setLeg(ItemStack leg) {
		armor[1] = new SItemStack(leg);
	}

}
