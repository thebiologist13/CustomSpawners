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
	private SItemStack[] armor = new SItemStack[4];
	private HashMap<Integer, SItemStack> content;
	private SItemStack hand;

	public SInventory() {
		this.content = new HashMap<Integer, SItemStack>();
		armor[0] = new SItemStack(0);
		armor[1] = new SItemStack(0);
		armor[2] = new SItemStack(0);
		armor[3] = new SItemStack(0);
	}

	public SInventory(HashMap<Integer, ItemStack> content) {

		HashMap<Integer, SItemStack> content1 = new HashMap<Integer, SItemStack>();
		for (Integer i : content.keySet()) {
			content1.put(i, new SItemStack(content.get(i)));
		}

		this.content = content1;
	}

	@Override
	public void addItem(int slot, ItemStack item) {
		content.put(slot, new SItemStack(item));
	}

	@Override
	public void addItem(ItemStack item) {
		List<Integer> conIDs = new ArrayList<Integer>();
		Iterator<Integer> conItr = content.keySet().iterator();
		while (conItr.hasNext()) {
			conIDs.add(conItr.next());
		}

		content.put(CustomSpawners.getNextID(conIDs), new SItemStack(item));
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
		content = new HashMap<Integer, SItemStack>();
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

	public Map<Integer, SItemStack> getContent() {
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
		if(hasHand())
			return hand.toItemStack();
		else
			return new ItemStack(0);
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
	public boolean hasItem(ItemStack item) {
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
	public void setArmor(ItemStack[] armor) {

		SItemStack[] armor1 = new SItemStack[4];

		for (int i = 0; i < armor.length; i++) {
			armor1[i] = new SItemStack(armor[i]);
		}

		this.armor = armor1;
	}

	@Override
	public void setBoot(ItemStack boot) {
		armor[0] = new SItemStack(boot);
	}

	@Override
	public void setChest(ItemStack chest) {
		armor[2] = new SItemStack(chest);
	}

	@Override
	public void setHand(ItemStack hand) {
		this.hand = new SItemStack(hand);
	}

	@Override
	public void setHelmet(ItemStack helmet) {
		armor[3] = new SItemStack(helmet);
	}

	@Override
	public void setLeg(ItemStack leg) {
		armor[1] = new SItemStack(leg);
	}

	private SItemStack generateSItemMeta(ItemStack stack) {
		SItemStack stack0 = new SItemStack(stack);
		
	}
	
}
