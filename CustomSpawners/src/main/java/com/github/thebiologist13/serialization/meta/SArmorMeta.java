package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.github.thebiologist13.serialization.SItemMeta;


public class SArmorMeta extends SItemMeta implements Serializable {

	private static final long serialVersionUID = -279788941080279582L;
	private Color color;
	
	public SArmorMeta() {
		super();
	}

	public SArmorMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof LeatherArmorMeta) {
				LeatherArmorMeta arm = (LeatherArmorMeta) meta;
				setColor(new Color(arm.getColor()));
			}
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
