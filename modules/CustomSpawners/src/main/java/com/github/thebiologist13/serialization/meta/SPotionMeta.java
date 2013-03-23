package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import com.github.thebiologist13.serialization.SPotionEffect;

public class SPotionMeta extends SItemMeta implements Serializable {
	
	private static final long serialVersionUID = 5051173464006919396L;
	private List<SPotionEffect> effects = new ArrayList<SPotionEffect>();
	
	public SPotionMeta() {
		super();
	}

	public SPotionMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof PotionMeta) {
				PotionMeta p = (PotionMeta) meta;
				for(PotionEffect e : p.getCustomEffects()) {
					effects.add(new SPotionEffect(e));
				}
			}
		}
	}

	public List<SPotionEffect> getEffects() {
		return effects;
	}

	public void setEffects(List<SPotionEffect> effects) {
		this.effects = effects;
	}

}
