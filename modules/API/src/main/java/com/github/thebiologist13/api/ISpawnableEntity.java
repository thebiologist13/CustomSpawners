package com.github.thebiologist13.api;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public interface ISpawnableEntity {
	
	public int getAge();
	
	public int getAge(Entity en);
	
	public int getAir();
	
	public String getCatType();
	
	public String getColor();
	
	public int getDamage();
	
	public int getDamage(Entity en);
	
	public List<String> getDamageBlacklist();
	
	public List<String> getDamageWhitelist();
	
	public int getDroppedExp();
	
	public List<ItemStack> getDrops();
	
	public ISpawnableEntity getRider();
	
	public ISInventory getInventory();
	
	public List<ItemStack> getItemStackDrops();
	
	public List<PotionEffect> getEffectsBukkit();
	
	public MaterialData getEndermanBlock();
	
	public int getFireTicks();

	public int getFuseTicks();

	public int getHealth();

	public float getHeight();

	public int getId();

	public List<ItemStack> getItemDamageList();

	public ItemStack getItemType();

	public float getLength();

	public int getMaxAir();

	public int getMaxHealth();
	
	public String getModifier(String key);

	public Map<String, String> getModifiers();

	public String getName();
	
	public ISpawner getSpawnerData();

	public PotionEffect getPotionEffectBukkit();

	public Villager.Profession getProfession();

	public Object getProp(String key);

	public int getSlimeSize();

	public EntityType getType();

	public Vector getVelocity();

	public float getWidth();

	public double getXVelocity();

	public float getYield();

	public double getYVelocity();

	public double getZVelocity();

	public boolean hasAllDimensions();
	
	public boolean hasModifier(String key);

	public boolean hasProp(String key);

	public boolean isAngry();

	public boolean isCharged();

	public boolean isIncendiary();

	public boolean isInvulnerable();

	@Deprecated
	public boolean isJockey();

	public boolean isPassive();

	public boolean isSaddled();

	public boolean isSitting();

	public boolean isTamed();

	public boolean isUsingBlacklist();

	public boolean isUsingCustomDamage();

	public boolean isUsingCustomDrops();

	public boolean isUsingInventory();
	
	public boolean isUsingWhitelist();
	
	public boolean requiresBlockBelow();
	
	public void setHeight(float height);
	
	public void setBlockBelow(boolean reqsBlockBelow);
	
	public void setLength(float length);
	
	public void setWidth(float width);
	
	public boolean showCustomName();
	
}
