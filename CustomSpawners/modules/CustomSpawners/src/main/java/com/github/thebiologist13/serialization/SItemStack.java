package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.thebiologist13.api.ICardboardEnchantment;
import com.github.thebiologist13.api.ISItemMeta;
import com.github.thebiologist13.api.ISItemStack;

public class SItemStack implements Serializable, ISItemStack {

	private static final long serialVersionUID = 3510924320558955033L;
	private int count;
	private short data;
	private int id;
	private ISItemMeta meta;

	// A drop chance. 100 is guaranteed.
	private float dropChance;

	public SItemStack(int id) {
		this(id, (short) 0, 1);
	}

	public SItemStack(int id, int count) {
		this(id, (short) 0, count);
	}

	public SItemStack(int id, short data, int count) {
		this.id = id;
		this.data = data;
		this.count = count;
		this.setMeta(new SItemMeta());
	}

	public SItemStack(ItemStack i) {
		ItemStack newStack = i.clone();
		this.id = newStack.getTypeId();
		this.data = newStack.getDurability();
		this.count = newStack.getAmount();
		this.meta = new SItemMeta(i);
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public short getData() {
		return data;
	}

	@Override
	public float getDropChance() {
		return dropChance;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public ISItemMeta getMeta() {
		return meta;
	}

	@Override
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void setData(short data) {
		this.data = data;
	}

	@Override
	public void setDropChance(float dropChance) {
		this.dropChance = dropChance;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void setMeta(ISItemMeta meta) {
		this.meta = meta;
	}

	@Override
	public ItemStack toItemStack() {
		ItemStack stack = new ItemStack(id, count, data);
		ItemMeta meta = stack.getItemMeta();

		if (meta != null && this.meta != null) {
			meta.setDisplayName(this.meta.getDisplayName());
			meta.setLore(this.meta.getLore());

			Map<ICardboardEnchantment, Integer> metaEnchants = this.meta.getEnchants();
			for (ICardboardEnchantment ce : metaEnchants.keySet()) {
				Enchantment e = ce.unbox();
				meta.addEnchant(e, metaEnchants.get(ce), true);
			}

			stack.setItemMeta(meta);
		}

		return stack;
	}

}
