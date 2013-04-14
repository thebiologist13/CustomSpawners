package com.github.thebiologist13.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.thebiologist13.api.ICardboardEnchantment;
import com.github.thebiologist13.api.ISItemMeta;
import com.github.thebiologist13.api.ISItemStack;
import com.github.thebiologist13.serialization.meta.SArmorMeta;
import com.github.thebiologist13.serialization.meta.SBookMeta;
import com.github.thebiologist13.serialization.meta.SEnchantmentMeta;
import com.github.thebiologist13.serialization.meta.SFireworkEffect;
import com.github.thebiologist13.serialization.meta.SFireworkEffectMeta;
import com.github.thebiologist13.serialization.meta.SFireworkMeta;
import com.github.thebiologist13.serialization.meta.SMapMeta;
import com.github.thebiologist13.serialization.meta.SPotionMeta;
import com.github.thebiologist13.serialization.meta.SSkullMeta;

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
		this.meta = generateSItemMeta(i);
	}
	
	public SItemMeta generateSItemMeta(ItemStack stack) {
		if(!stack.hasItemMeta()) 
			return null;
		
		ItemMeta meta = stack.getItemMeta();
		
		if(meta instanceof LeatherArmorMeta) {
			return new SArmorMeta(stack);
		} else if(meta instanceof BookMeta) {
			return new SBookMeta(stack);
		} else if(meta instanceof EnchantmentStorageMeta) {
			return new SEnchantmentMeta(stack);
		} else if(meta instanceof FireworkEffectMeta) {
			return new SFireworkEffectMeta(stack);
		} else if(meta instanceof FireworkMeta) {
			return new SFireworkMeta(stack);
		} else if(meta instanceof MapMeta) {
			return new SMapMeta(stack);
		} else if(meta instanceof PotionMeta) {
			return new SPotionMeta(stack);
		} else if(meta instanceof SkullMeta) {
			return new SSkullMeta(stack);
		} else {
			return new SItemMeta(stack);
		}
		
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
			
			if(this.meta instanceof SArmorMeta && meta instanceof LeatherArmorMeta) {
				SArmorMeta meta0 = (SArmorMeta) this.meta;
				LeatherArmorMeta meta1 = (LeatherArmorMeta) meta;
				
				if(meta0.getColor() != null)
					meta1.setColor(meta0.getColor().getBukkitColor());
				
				stack.setItemMeta(meta1);
			} else if(this.meta instanceof SBookMeta && meta instanceof BookMeta) {
				SBookMeta meta0 = (SBookMeta) this.meta;
				BookMeta meta1 = (BookMeta) meta;
				
				if(meta0.hasAuthor()) {
					meta1.setAuthor(meta0.getAuthor());
				}
				if(meta0.hasTitle()) {
					meta1.setTitle(meta0.getTitle());
				}
				if(meta0.hasPages()) {
					meta1.setPages(meta0.getPages());
				}
				
				stack.setItemMeta(meta1);
			} else if(this.meta instanceof SEnchantmentMeta && meta instanceof EnchantmentStorageMeta) {
				SEnchantmentMeta meta0 = (SEnchantmentMeta) this.meta;
				EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) meta;
				
				for(CardboardEnchantment ce : meta0.getStoredEnchantments().keySet()) {
					Enchantment e = ce.unbox();
					meta1.addStoredEnchant(e, meta0.getStoredEnchantments().get(e), true);
				}
				
				stack.setItemMeta(meta1);
			} else if(this.meta instanceof SFireworkEffectMeta && meta instanceof FireworkEffectMeta) {
				SFireworkEffectMeta meta0 = (SFireworkEffectMeta) this.meta;
				FireworkEffectMeta meta1 = (FireworkEffectMeta) meta;
				
				FireworkEffect built = buildEffect(meta0.getEffect());
				if(built != null)
					meta1.setEffect(built);
				
				stack.setItemMeta(meta1);
			} else if(this.meta instanceof SFireworkMeta && meta instanceof FireworkMeta) {
				SFireworkMeta meta0 = (SFireworkMeta) this.meta;
				FireworkMeta meta1 = (FireworkMeta) meta;
				
				for(SFireworkEffect e : meta0.getEffects()) {
					FireworkEffect built = buildEffect(e);
					if(built != null)
						meta1.addEffect(built);
				}
				
				meta1.setPower(meta0.getPower());
				
				stack.setItemMeta(meta1);
			} else if(this.meta instanceof SMapMeta && meta instanceof MapMeta) {
				SMapMeta meta0 = (SMapMeta) this.meta;
				MapMeta meta1 = (MapMeta) meta;
				
				meta1.setScaling(meta0.isScaling());
				
				stack.setItemMeta(meta1);
			} else if(this.meta instanceof SPotionMeta && meta instanceof PotionMeta) {
				SPotionMeta meta0 = (SPotionMeta) this.meta;
				PotionMeta meta1 = (PotionMeta) meta;
				
				for(SPotionEffect e : meta0.getEffects()) {
					meta1.addCustomEffect(e.toPotionEffect(), false);
				}
				
				stack.setItemMeta(meta1);
			} else if(this.meta instanceof SSkullMeta && meta instanceof SkullMeta) {
				SSkullMeta meta0 = (SSkullMeta) this.meta;
				SkullMeta meta1 = (SkullMeta) meta;
				
				meta1.setOwner(meta0.getOwner());
				
				stack.setItemMeta(meta1);
			} else {
				stack.setItemMeta(meta);
			}
		}

		return stack;
	}
	
	private FireworkEffect buildEffect(SFireworkEffect sEffect) {
		FireworkEffect.Builder effect = FireworkEffect.builder();
		List<Color> colors = new ArrayList<Color>();
		if(sEffect != null) {
			for(com.github.thebiologist13.serialization.meta.Color c : sEffect.getColors()) {
				colors.add(c.getBukkitColor());
			}
			
			effect.flicker(sEffect.isFlicker());
			effect.trail(sEffect.isTrail());
			effect.with(sEffect.getType());
			effect.withColor(colors);
			effect.withFade(sEffect.getFade());
			FireworkEffect built = effect.build();
			return built;
		}
		return null;
	}

}
