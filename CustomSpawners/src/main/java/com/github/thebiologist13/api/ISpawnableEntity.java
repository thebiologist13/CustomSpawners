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

import com.github.thebiologist13.HorseData;
import com.github.thebiologist13.attributelib.Attribute;

public interface ISpawnableEntity extends IObject {
	
	public void addAttribute(Attribute att);
	
	public int getAge(Entity en);
	
	public int getAir(Entity en);
	
	public List<Attribute> getAttributes();
	
	public String getCatType();
	
	public String getColor();
	
	public int getDamage(Entity en);
	
	public List<String> getDamageBlacklist();
	
	public List<String> getDamageWhitelist();
	
	public int getDroppedExp(Entity en);
	
	public List<ItemStack> getDrops();
	
	public List<PotionEffect> getEffectsBukkit();
	
	public MaterialData getEndermanBlock();
	
	public int getFireTicks(Entity en);
	
	public int getFuseTicks(Entity en);

	public int getHealth(Entity en);
	
	public double getHealthDouble(Entity en);
	
	public float getHeight();
	
	public HorseData.Color getHorseColor();

	public HorseData.Type getHorseType();

	public HorseData.Variant getHorseVariant();
	
	public ISInventory getInventory();

	public List<ItemStack> getItemDamageList();

	public List<ItemStack> getItemStackDrops();

	public ItemStack getItemType();

	public float getLength();

	public int getMaxAir();
	
	public int getMaxHealth();

	public String getModifier(String key);
	
	public Map<String, String> getModifiers();

	public String getName();
	
	public int getNPC();

	public PotionEffect getPotionEffectBukkit();

	public Villager.Profession getProfession();

	public Object getProp(String key);

	public ISpawnableEntity getRider();

	public int getSlimeSize();

	public ISpawner getSpawnerData();

	public EntityType getType();

	public Vector getVelocity(Entity en);

	public Vector getVelocity2(Entity en);

	public float getWidth();

	public double getX2Velocity(Entity en);
	
	public double getXVelocity(Entity en);

	public double getY2Velocity(Entity en);

	public float getYield(Entity en);

	public double getYVelocity(Entity en);

	public double getZ2Velocity(Entity en);
	
	public double getZVelocity(Entity en);
	
	public boolean hasAllDimensions();

	public boolean hasChests();

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
	
	public void removeAttribute(Attribute att);
	
	public boolean requiresBlockBelow();
	
	public void setAttributes(List<Attribute> attributes);
	
	public void setBlockBelow(boolean reqsBlockBelow);
	
	public void setHeight(float height);
	
	public void setLength(float length);
	
	public void setMaxAir(int max);
	
	public void setMaxHealth(int maxHealth);
	
	public void setWidth(float width);

	public boolean showCustomName();
	
}
