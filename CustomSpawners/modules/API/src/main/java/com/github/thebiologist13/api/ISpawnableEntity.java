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

public interface ISpawnableEntity extends IObject {
	
	public int getAge(Entity en);
	
	public int getAir(Entity en);
	
	public String getCatType();
	
	public String getColor();
	
	public int getDamage(Entity en);
	
	public List<String> getDamageBlacklist();
	
	public List<String> getDamageWhitelist();
	
	public int getDroppedExp(Entity en);
	
	public List<ItemStack> getDrops();
	
	public ISpawnableEntity getRider();
	
	public ISInventory getInventory();
	
	public List<ItemStack> getItemStackDrops();
	
	public List<PotionEffect> getEffectsBukkit();
	
	public MaterialData getEndermanBlock();
	
	public int getFireTicks(Entity en);

	public int getFuseTicks(Entity en);

	public int getHealth(Entity en);

	public float getHeight();

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

	public Vector getVelocity(Entity en);

	public float getWidth();

	public double getXVelocity(Entity en);

	public float getYield(Entity en);

	public double getYVelocity(Entity en);

	public double getZVelocity(Entity en);

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
	
	public void setMaxAir(int max);
	
	public void setMaxHealth(int maxHealth);
	
	public void setLength(float length);
	
	public void setWidth(float width);
	
	public boolean showCustomName();
	
}
