package com.github.thebiologist13;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
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
	private boolean passive = false;
	
	//Initialize a SpawnableEntity
	public SpawnableEntity(EntityType type, int id) {
		this.type = type;
		this.id = id;
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
}
