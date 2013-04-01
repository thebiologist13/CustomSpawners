package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.thebiologist13.serialization.SItemMeta;

public class SSkullMeta extends SItemMeta implements Serializable {
	
	private static final long serialVersionUID = -8625886247607054011L;
	private String owner;
	
	public SSkullMeta() {
		super();
	}

	public SSkullMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof SkullMeta) {
				SkullMeta skull = (SkullMeta) meta;
				setOwner(skull.getOwner());
			}
		}
	}

	public String getOwner() {
		return owner;
	}
	
	public boolean hasOwner() {
		return owner != null;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
