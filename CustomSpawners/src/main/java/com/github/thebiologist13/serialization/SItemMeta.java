package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.thebiologist13.api.ICardboardEnchantment;
import com.github.thebiologist13.api.ISItemMeta;

public class SItemMeta implements Serializable, ISItemMeta {

	private static final long serialVersionUID = -8130035779033299778L;

	private String displayName;
	private Map<ICardboardEnchantment, Integer> enchants;
	private List<String> lore;
	private Map<String, Object> miscData;

	public SItemMeta() {
		displayName = "";
		lore = new ArrayList<String>();
		setEnchants(new HashMap<ICardboardEnchantment, Integer>());
		miscData = new HashMap<String, Object>();
	}

	public SItemMeta(ItemStack stack) {
		if (stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			displayName = meta.getDisplayName();
			lore = meta.getLore();
			enchants = new HashMap<ICardboardEnchantment, Integer>();

			Map<Enchantment, Integer> eFromMeta = meta.getEnchants();
			for (Enchantment e : eFromMeta.keySet()) {
				enchants.put(new CardboardEnchantment(e), eFromMeta.get(e));
			}
			
		} else {
			displayName = "";
			lore = new ArrayList<String>();
			enchants = new HashMap<ICardboardEnchantment, Integer>();
		}

	}

	@Override
	public void addEnchantment(Enchantment enchant, int level) {
		enchants.put(new CardboardEnchantment(enchant), level);
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Map<ICardboardEnchantment, Integer> getEnchants() {
		return enchants;
	}

	@Override
	public List<String> getLore() {
		return lore;
	}

	@Override
	public Map<String, Object> getMiscData() {
		return miscData;
	}

	@Override
	public Object getProp(String key) {
		return miscData.get(key);
	}

	@Override
	public boolean hasProp(String key) {
		return miscData.containsKey(key);
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public void setEnchants(Map<ICardboardEnchantment, Integer> enchants) {
		this.enchants = enchants;
	}

	@Override
	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	@Override
	public void setMiscData(Map<String, Object> miscData) {
		this.miscData = miscData;
	}

	@Override
	public void setProp(String key, Object data) {
		this.miscData.put(key, data);
	}

}
