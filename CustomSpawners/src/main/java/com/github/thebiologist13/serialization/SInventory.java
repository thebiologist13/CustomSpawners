package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.api.ISInventory;
import com.github.thebiologist13.api.ISItemStack;

public class SInventory implements Serializable, ISInventory {

	private static final long serialVersionUID = 7492076966612850960L;
	private ISItemStack[] armor = new SItemStack[4];
	private HashMap<Integer, ISItemStack> content;
	private ISItemStack hand;

	public SInventory() {
		this.content = new HashMap<Integer, ISItemStack>();
		armor[0] = new SItemStack(0);
		armor[1] = new SItemStack(0);
		armor[2] = new SItemStack(0);
		armor[3] = new SItemStack(0);
	}

	public SInventory(HashMap<Integer, ItemStack> content) {

		HashMap<Integer, ISItemStack> content1 = new HashMap<Integer, ISItemStack>();
		for (Integer i : content.keySet()) {
			content1.put(i, new SItemStack(content.get(i)));
		}

		this.content = content1;
	}

	@Override
	public void addItem(int slot, ISItemStack item) {
		content.put(slot, item);
	}

	@Override
	public void addItem(ISItemStack item) {
		List<Integer> conIDs = new ArrayList<Integer>();
		Iterator<Integer> conItr = content.keySet().iterator();
		while (conItr.hasNext()) {
			conIDs.add(conItr.next());
		}

		content.put(CustomSpawners.getNextID(conIDs), item);
	}

	@Override
	public void clearArmor() {
		armor = new SItemStack[4];
	}

	@Override
	public void clearSlot(int slot) {
		if (hasSlot(slot)) {
			content.remove(slot);
		}
	}

	@Override
	public void empty() {
		content = new HashMap<Integer, ISItemStack>();
		armor = new SItemStack[4];
		hand = null;
	}

	@Override
	public ItemStack[] getArmor() {

		ItemStack[] armor1 = new ItemStack[4];

		for (int i = 0; i < armor.length; i++) {
			armor1[i] = armor[i].toItemStack();
		}

		return armor1;
	}
	
	public ISItemStack[] getArmorSItemStacks() {
		return armor;
	}

	public Map<Integer, ISItemStack> getContent() {
		return content;
	}

	@Override
	public Map<Integer, ItemStack> getContentBukkit() {
		Map<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
		for (Integer i : content.keySet()) {
			map.put(i, content.get(i).toItemStack());
		}
		return map;
	}

	@Override
	public ItemStack getHand() {
		return getHandSItemStack().toItemStack();
	}
	
	public ISItemStack getHandSItemStack() {
		if(hasHand())
			return hand;
		else
			return new SItemStack(0);
	}

	@Override
	public ArrayList<ItemStack> getMainInventory() {
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		array.add(0, (hasHand()) ? hand.toItemStack() : new ItemStack(0));
		array.add(1, armor[0].toItemStack());
		array.add(2, armor[1].toItemStack());
		array.add(3, armor[2].toItemStack());
		array.add(4, armor[3].toItemStack());
		return array;
	}
	
	@Override
	public ArrayList<ISItemStack> getMainInventoryISItemStacks() {
		ArrayList<ISItemStack> array = new ArrayList<ISItemStack>();
		array.add(0, (hasHand()) ? hand : new SItemStack(0));
		array.add(1, armor[0]);
		array.add(2, armor[1]);
		array.add(3, armor[2]);
		array.add(4, armor[3]);
		return array;
	}

	@Override
	public ItemStack getSlot(int slot) {
		if (hasSlot(slot)) {
			return content.get(slot).toItemStack();
		}

		return null;
	}

	@Override
	public ArrayList<Integer> getSlots(ItemStack item) {
		ArrayList<Integer> slots = new ArrayList<Integer>();

		for (Integer i : content.keySet()) {
			if (content.get(i).equals(item)) {
				slots.add(i);
			}
		}

		return slots;

	}
	
	@Override
	public boolean hasHand() {
		return hand != null;
	}

	@Override
	public boolean hasItem(ISItemStack item) {
		return content.values().contains(item);
	}

	@Override
	public boolean hasSlot(int slot) {
		return content.keySet().contains(slot);
	}

	@Override
	public boolean isEmpty() {

		boolean mainInventoryEmpty = true;
		List<ItemStack> main = getMainInventory();
		for (ItemStack i : main) {
			if (!i.getType().equals(Material.AIR)) {
				mainInventoryEmpty = false;
			}
		}

		return (mainInventoryEmpty && this.content.isEmpty()) ? true : false;
	}

	@Override
	public void removeBoot() {
		armor[0] = new SItemStack(0);
	}

	@Override
	public void removeChest() {
		armor[3] = new SItemStack(0);
	}

	@Override
	public void removeHelmet() {
		armor[3] = new SItemStack(0);
	}

	@Override
	public void removeLeg() {
		armor[1] = new SItemStack(0);
	}

	@Override
	public void setArmor(ISItemStack[] armor) {
		this.armor = armor;
	}

	@Override
	public void setBoot(ISItemStack boot) {
		armor[0] = boot;
	}

	@Override
	public void setChest(ISItemStack chest) {
		armor[2] = chest;
	}

	@Override
	public void setHand(ISItemStack hand) {
		this.hand = hand;
	}

	@Override
	public void setHelmet(ISItemStack helmet) {
		armor[3] = helmet;
	}

	@Override
	public void setLeg(ISItemStack leg) {
		armor[1] = leg;
	}
	
}
