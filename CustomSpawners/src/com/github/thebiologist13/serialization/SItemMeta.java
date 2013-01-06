package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SItemMeta implements Serializable {
	
	private static final long serialVersionUID = -8130035779033299778L;
	
	private String displayName;
	private Map<CardboardEnchantment, Integer> enchants;
	private List<String> lore;
	private Map<String, Object> miscData;
	
	public SItemMeta() {
		displayName = "";
		lore = new ArrayList<String>();
		setEnchants(new HashMap<CardboardEnchantment, Integer>());
		miscData = new HashMap<String, Object>();
	}
	
	public SItemMeta(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		displayName = meta.getDisplayName();
		lore = meta.getLore();
		enchants = new HashMap<CardboardEnchantment, Integer>();
		
		Map<Enchantment, Integer> eFromMeta = meta.getEnchants();
		for(Enchantment e : eFromMeta.keySet()) {
			enchants.put(new CardboardEnchantment(e), eFromMeta.get(e));
		}
		
	}
	
	public void addEnchantment(Enchantment enchant, int level) {
		enchants.put(new CardboardEnchantment(enchant), level);
	}

	public String getDisplayName() {
		return displayName;
	}

	public Map<CardboardEnchantment, Integer> getEnchants() {
		return enchants;
	}

	public List<String> getLore() {
		return lore;
	}

	public Map<String, Object> getMiscData() {
		return miscData;
	}
	
	public Object getProp(String key) {
		return miscData.get(key);
	}
	
	public boolean hasProp(String key) {
		return miscData.containsKey(key);
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEnchants(Map<CardboardEnchantment, Integer> enchants) {
		this.enchants = enchants;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void setMiscData(Map<String, Object> miscData) {
		this.miscData = miscData;
	}
	
	public void setProp(String key, Object data) {
		this.miscData.put(key, data);
	}

}
