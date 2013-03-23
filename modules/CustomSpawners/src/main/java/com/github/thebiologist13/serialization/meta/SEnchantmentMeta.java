package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.thebiologist13.serialization.CardboardEnchantment;

public class SEnchantmentMeta extends SItemMeta implements Serializable {

	private static final long serialVersionUID = 119734518456419750L;
	private Map<CardboardEnchantment, Integer> storedEnchantments = new HashMap<CardboardEnchantment, Integer>();
	
	public SEnchantmentMeta() {
		super();
	}

	public SEnchantmentMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) meta;
				for(Enchantment en : enchMeta.getStoredEnchants().keySet()) {
					CardboardEnchantment c = new CardboardEnchantment(en);
					storedEnchantments.put(c, enchMeta.getEnchantLevel(en));
				}
			}
		}
	}

	public Map<CardboardEnchantment, Integer> getStoredEnchantments() {
		return storedEnchantments;
	}

	public boolean hasEnchants() {
		return this.storedEnchantments != null;
	}
	
	public void setStoredEnchantments(Map<CardboardEnchantment, Integer> storedEnchantments) {
		this.storedEnchantments = storedEnchantments;
	}

}
