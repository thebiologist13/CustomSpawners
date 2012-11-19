package com.github.thebiologist13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

@SerializableAs("EntityInventory")
public class EntityInventory implements ConfigurationSerializable {

	private HashMap<Integer, ItemStack> content = null;
	private ItemStack[] armor = new ItemStack[4];
	private ItemStack hand = new ItemStack(Material.AIR);
	
	public EntityInventory() {
		this.content = new HashMap<Integer, ItemStack>();
	}
	
	public EntityInventory(HashMap<Integer, ItemStack> content) {
		this.content = content;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", content);
		map.put("boot", armor[0]);
		map.put("leg", armor[1]);
		map.put("chest", armor[2]);
		map.put("helmet", armor[3]);
		map.put("hand", hand);
		return map;
	}
	
	public static EntityInventory deserialize(Map<String, Object> map) {
		
		Map<?, ?> rawContent = (Map<?, ?>) map.get("content");
		ItemStack[] arm1 = {
				(ItemStack) map.get("helmet"),
				(ItemStack) map.get("chest"),
				(ItemStack) map.get("leg"),
				(ItemStack) map.get("boot")
		};
		HashMap<Integer, ItemStack> parsedContent = new HashMap<Integer, ItemStack>();
		for(Object o : rawContent.keySet()) {
			if(o instanceof Integer) {
				Object o1 = rawContent.get(o);
				if(o1 instanceof ItemStack) {
					parsedContent.put((Integer) o, (ItemStack) o1);
				}
			}
		}
		
		EntityInventory inv = new EntityInventory(parsedContent);
		inv.setArmor(arm1);
		inv.setHand((ItemStack) map.get("hand"));
		
		return inv;
	}
	
	public void addItem(ItemStack item) {
		
		int count = 0;
		
		while(true) {
			if(!content.containsKey(count)) {
				break;
			}
		}
		
		content.put(count, item);
	}
	
	public void addItem(int slot, ItemStack item) {
		content.put(slot, item);
	}
	
	public boolean hasItem(ItemStack item) {
		return content.values().contains(item);
	}
	
	public boolean hasSlot(int slot) {
		return content.keySet().contains(slot);
	}
	
	public void clearSlot(int slot) {
		if(hasSlot(slot)) {
			content.remove(slot);
		}
	}
	
	public void empty() {
		content = new HashMap<Integer, ItemStack>();
	}
	
	public ItemStack getSlot(int slot) {
		if(hasSlot(slot)) {
			return content.get(slot);
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

	public ItemStack getHand() {
		return hand;
	}

	public void setHand(ItemStack hand) {
		this.hand = hand;
	}
	
	public void clearArmor() {
		armor = new ItemStack[4];
	}
	
	public ItemStack[] getArmor() {
		return armor;
	}
	
	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}
	
	public void setHelmet(ItemStack helmet) {
		armor[3] = helmet;
	}
	
	public void setChest(ItemStack chest) {
		armor[2] = chest;
	}
	
	public void setLeg(ItemStack leg) {
		armor[1] = leg;
	}
	
	public void setBoot(ItemStack boot) {
		armor[0] = boot;
	}
	
	public void removeHelmet() {
		armor[3] = new ItemStack(Material.AIR);
	}
	
	public void removeChest() {
		armor[3] = new ItemStack(Material.AIR);
	}
	
	public void removeLeg() {
		armor[1] = new ItemStack(Material.AIR);
	}
	
	public void removeBoot() {
		armor[0] = new ItemStack(Material.AIR);
	}
	
	public ArrayList<ItemStack> getMainInventory() {
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		array.add(0, hand);
		array.add(1, armor[0]);
		array.add(2, armor[1]);
		array.add(3, armor[2]);
		array.add(4, armor[3]);
		return array;
	}

}
