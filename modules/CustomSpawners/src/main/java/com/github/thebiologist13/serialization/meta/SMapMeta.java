package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;

import com.github.thebiologist13.serialization.SItemMeta;

public class SMapMeta extends SItemMeta implements Serializable {
	
	private static final long serialVersionUID = 4001310400289443660L;
	private boolean scaling = false;
	
	public SMapMeta() {
		super();
	}

	public SMapMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof MapMeta) {
				MapMeta map = (MapMeta) meta;
				scaling = map.isScaling();
			}
		}
	}

	public boolean isScaling() {
		return scaling;
	}

	public void setScaling(boolean scaling) {
		this.scaling = scaling;
	}

}
