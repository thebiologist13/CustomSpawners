package com.github.thebiologist13.api;

import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

public interface ISItemMeta {
	
	public void addEnchantment(Enchantment enchant, int level);

	public String getDisplayName();

	public Map<ICardboardEnchantment, Integer> getEnchants();

	public List<String> getLore();

	public Map<String, Object> getMiscData();
	
	public Object getProp(String key);
	
	public boolean hasProp(String key);

	public void setDisplayName(String displayName);

	public void setEnchants(Map<ICardboardEnchantment, Integer> enchants);

	public void setLore(List<String> lore);

	public void setMiscData(Map<String, Object> miscData);
	
	public void setProp(String key, Object data);
	
}
