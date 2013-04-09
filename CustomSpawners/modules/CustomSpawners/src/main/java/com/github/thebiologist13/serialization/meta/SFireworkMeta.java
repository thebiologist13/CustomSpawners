package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.thebiologist13.serialization.SItemMeta;


public class SFireworkMeta extends SItemMeta implements Serializable {

	private static final long serialVersionUID = -7486031304716984714L;
	private List<SFireworkEffect> effects = new ArrayList<SFireworkEffect>();
	private int power = 1;
	
	public SFireworkMeta() {
		super();
	}

	public SFireworkMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof FireworkMeta) {
				FireworkMeta effectMeta = (FireworkMeta) meta;
				for(FireworkEffect e : effectMeta.getEffects()) {
					effects.add(new SFireworkEffect(e));
				}
				power = effectMeta.getPower();
			}
		}
	}

	public List<SFireworkEffect> getEffects() {
		return effects;
	}

	public int getPower() {
		return power;
	}

	public void setEffects(List<SFireworkEffect> effects) {
		this.effects = effects;
	}

	public void setPower(int power) {
		this.power = power;
	}

}
