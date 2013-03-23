package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SFireworkEffectMeta extends SItemMeta implements Serializable {

	private static final long serialVersionUID = -7807210989027120277L;
	private SFireworkEffect effect;
	
	public SFireworkEffectMeta() {
		super();
	}

	public SFireworkEffectMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof FireworkEffectMeta) {
				FireworkEffectMeta effectMeta = (FireworkEffectMeta) meta;
				setEffect(new SFireworkEffect(effectMeta.getEffect()));
			}
		}
	}

	public SFireworkEffect getEffect() {
		return effect;
	}

	public void setEffect(SFireworkEffect effect) {
		this.effect = effect;
	}

}
