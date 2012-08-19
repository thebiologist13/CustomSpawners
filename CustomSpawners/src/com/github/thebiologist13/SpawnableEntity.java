package com.github.thebiologist13;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class SpawnableEntity {
	
	/*
	 * Spawnable Entities are entities that
	 * Custom Spawners can spawn. They have lots
	 * of data associated with them.
	 */
	
	//Identification Variables
	private String name = "";
	private int id = 0;
	
	//Basic Data
	private EntityType type = null;
	private ArrayList<EntityPotionEffect> effects = new ArrayList<EntityPotionEffect>();
	private double xVelocity = 0;
	private double yVelocity = 0;
	private double zVelocity = 0;
	private Vector velocity = new Vector(xVelocity, yVelocity, zVelocity);
	private int age = 0;
	private int health = 1;
	private int air = 0;
	private boolean passive = false;
	private int fireTicks = 0;
	private ArrayList<String> damageWhitelist = new ArrayList<String>();
	private ArrayList<String> damageBlacklist = new ArrayList<String>();
	private ArrayList<Integer> itemDamage = new ArrayList<Integer>();
	private ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	private boolean useWhitelist = false;
	private boolean useBlacklist = true;
	private boolean useCustomDamage = false; //Check
	private int damage = 3; //Check
	
	//Specific Data
	private Villager.Profession villagerProfession = null;
	private MaterialData endermanBlock = null;
	private boolean isSaddled = false;
	private boolean isCharged = false;
	private boolean isJockey = false;
	private boolean isTamed = false;
	private boolean angry = false;
	private boolean isSitting = false;
	private String catType = "";
	private int slimeSize = 1;
	private String color = "";
	private EntityPotionEffect potionEffect = null; //Check
	private int droppedExp = 0; //Check
	private int fuseTicks = 80;
	private float yield = 5.0f;
	private boolean incendiary = false;
	private ItemStack itemType = null;
	
	//Initialize a SpawnableEntity
	public SpawnableEntity(EntityType type, int id) {
		this.type = type;
		this.id = id;
		
		if(type.equals(EntityType.DROPPED_ITEM)) {
			itemType = new ItemStack(1); //TODO make configurable
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public ArrayList<EntityPotionEffect> getEffects() {
		return effects;
	}

	public void setEffects(ArrayList<EntityPotionEffect> effects) {
		this.effects = effects;
	}
	
	public void addPoitionEffect(EntityPotionEffect effect) {
		effects.add(effect);
	}

	public double getXVelocity() {
		return xVelocity;
	}

	public void setXVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public double getYVelocity() {
		return yVelocity;
	}

	public void setYVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public double getZVelocity() {
		return zVelocity;
	}

	public void setZVelocity(double zVelocity) {
		this.zVelocity = zVelocity;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public MaterialData getEndermanBlock() {
		return endermanBlock;
	}

	public void setEndermanBlock(MaterialData endermanBlock) {
		this.endermanBlock = endermanBlock;
	}

	public boolean isSaddled() {
		return isSaddled;
	}

	public void setSaddled(boolean isSaddled) {
		this.isSaddled = isSaddled;
	}

	public boolean isCharged() {
		return isCharged;
	}

	public void setCharged(boolean isCharged) {
		this.isCharged = isCharged;
	}

	public boolean isJockey() {
		return isJockey;
	}

	public void setJockey(boolean isJockey) {
		this.isJockey = isJockey;
	}

	public boolean isTamed() {
		return isTamed;
	}

	public void setTamed(boolean isTamed) {
		this.isTamed = isTamed;
	}

	public boolean isAngry() {
		return angry;
	}

	public void setAngry(boolean angry) {
		this.angry = angry;
	}

	public boolean isSitting() {
		return isSitting;
	}

	public void setSitting(boolean isSitting) {
		this.isSitting = isSitting;
	}

	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	public int getSlimeSize() {
		return slimeSize;
	}

	public void setSlimeSize(int slimeSize) {
		this.slimeSize = slimeSize;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public Villager.Profession getProfession() {
		return villagerProfession;
	}

	public void setProfession(Villager.Profession villagerProfession) {
		this.villagerProfession = villagerProfession;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
		this.xVelocity = velocity.getX();
		this.yVelocity = velocity.getY();
		this.zVelocity = velocity.getZ();
	}

	public int getAir() {
		return air;
	}

	public void setAir(int air) {
		this.air = air;
	}
	
	public void remove() {
		this.id = -1;
	}
	
	public boolean isPassive() {
		return passive;
	}

	public void setPassive(boolean passive) {
		this.passive = passive;
	}

	public int getFireTicks() {
		return fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public ArrayList<String> getDamageWhitelist() {
		return damageWhitelist;
	}

	public void setDamageWhitelist(ArrayList<String> damageWhitelist) {
		this.damageWhitelist = damageWhitelist;
	}
	
	public void addDamageWhitelist(String damageType) {
		damageWhitelist.add(damageType);
	}

	public ArrayList<String> getDamageBlacklist() {
		return damageBlacklist;
	}

	public void setDamageBlacklist(ArrayList<String> damageBlacklist) {
		this.damageBlacklist = damageBlacklist;
	}
	
	public void addDamageBlacklist(String damageType) {
		damageBlacklist.add(damageType);
	}

	public boolean isUsingWhitelist() {
		return useWhitelist;
	}

	public void setUseWhitelist(boolean useWhitelist) {
		this.useWhitelist = useWhitelist;
		
		if(useWhitelist) {
			this.useBlacklist = false;
		} else {
			this.useBlacklist = true;
		}
		
	}

	public boolean isUsingBlacklist() {
		return useBlacklist;
	}

	public void setUseBlacklist(boolean useBlacklist) {
		this.useBlacklist = useBlacklist;
		
		if(useBlacklist) {
			this.useWhitelist = false;
		} else {
			this.useWhitelist = true;
		}
	}

	public ArrayList<Integer> getItemDamageList() {
		return itemDamage;
	}

	public void setItemDamageList(ArrayList<Integer> itemDamage) {
		this.itemDamage = itemDamage;
	}
	
	public void addItemDamage(int value) {
		itemDamage.add(value);
	}

	public boolean isUsingCustomDamage() {
		return useCustomDamage;
	}

	public void setUsingCustomDamage(boolean useCustomDamage) {
		this.useCustomDamage = useCustomDamage;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public EntityPotionEffect getPotionEffect() {
		return potionEffect;
	}

	public void setPotionEffect(EntityPotionEffect potionEffect) {
		this.potionEffect = potionEffect;
	}

	public int getDroppedExp() {
		return droppedExp;
	}

	public void setDroppedExp(int droppedExp) {
		this.droppedExp = droppedExp;
	}

	public int getFuseTicks() {
		return fuseTicks;
	}

	public void setFuseTicks(int fuseTicks) {
		this.fuseTicks = fuseTicks;
	}

	public float getYield() {
		return yield;
	}

	public void setYield(float yield) {
		this.yield = yield;
	}

	public boolean isIncendiary() {
		return incendiary;
	}

	public void setIncendiary(boolean incendiary) {
		this.incendiary = incendiary;
	}

	public ArrayList<ItemStack> getDrops() {
		return drops;
	}

	public void setDrops(ArrayList<ItemStack> drops) {
		this.drops = drops;
	}

	public ItemStack getItemType() {
		return itemType;
	}

	public void setItemType(ItemStack itemType) {
		this.itemType = itemType;
	}

}
