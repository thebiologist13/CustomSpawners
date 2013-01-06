package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SItemStack implements Serializable {

	private static final long serialVersionUID = 3510924320558955033L;
	private int count;
	private short data;
	private int id;
	private SItemMeta meta;

	public SItemStack(int id) {
		this(id, (short) 0, 1);
	}
	
	public SItemStack(int id, int count) {
		this(id, (short) 0, count);
	}
	
	public SItemStack(int id, short data, int count) {
		this.id = id;
		this.data = data;
		this.count = count;
		this.setMeta(new SItemMeta());
	}
	
	public SItemStack(ItemStack i) {
		ItemStack newStack = i.clone();
		this.id = newStack.getTypeId();
		this.data = newStack.getDurability();
		this.count = newStack.getAmount();
		this.meta = new SItemMeta(i);
	}

	public int getCount() {
		return count;
	}

	public short getData() {
		return data;
	}

	public int getId() {
		return id;
	}

	public SItemMeta getMeta() {
		return meta;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setData(short data) {
		this.data = data;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setMeta(SItemMeta meta) {
		this.meta = meta;
	}

	public ItemStack toItemStack() {
		ItemStack stack = new ItemStack(id, count, data);
		ItemMeta meta = stack.getItemMeta();
		
		if(meta != null && this.meta != null) {
			meta.setDisplayName(this.meta.getDisplayName()); //TODO Test
			meta.setLore(this.meta.getLore());
			
			Map<CardboardEnchantment, Integer> metaEnchants = this.meta.getEnchants();
			for(CardboardEnchantment ce : metaEnchants.keySet()) {
				Enchantment e = ce.unbox();
				meta.addEnchant(e, metaEnchants.get(ce), true);
			}
			
			stack.setItemMeta(meta);
		}
		
		return stack;
	}

}
