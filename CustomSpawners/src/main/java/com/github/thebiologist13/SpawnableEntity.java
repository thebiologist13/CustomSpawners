package com.github.thebiologist13;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.Citizens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.github.thebiologist13.api.ISpawnableEntity;
import com.github.thebiologist13.attributelib.Attribute;
import com.github.thebiologist13.serialization.SInventory;
import com.github.thebiologist13.serialization.SItemStack;
import com.github.thebiologist13.serialization.SPotionEffect;
import com.github.thebiologist13.serialization.SVector;
import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Monster;

/**
 * Represents an entity that can be spawned by CustomSpawners.
 * 
 * @author thebiologist13
 */
public class SpawnableEntity implements Serializable, ISpawnableEntity {

	/*
	 * Spawnable Entities are entities that Custom Spawners can spawn. They have
	 * lots of data associated with them.
	 */

	private static final long serialVersionUID = -60000847475741355L;

	//Attributes
	private List<Attribute> attributes;
	// Damage Blacklist
	private List<String> blacklist;
	//Data
	private Map<String, Object> data;
	// Drops
	private List<SItemStack> drops;
	// Effects
	private List<SPotionEffect> effects;
	// Items that do or do not inflict damage
	private List<SItemStack> itemDamage;
	// Modifiers
	private Map<String, String> modifiers;
	// Damage Whitelist
	private List<String> whitelist;

	// Initialize a SpawnableEntity
	public SpawnableEntity(EntityType type, int id) {
		this.data = new HashMap<String, Object>();
		this.blacklist = new ArrayList<String>();
		this.data = new HashMap<String, Object>();
		this.drops = new ArrayList<SItemStack>();
		this.itemDamage = new ArrayList<SItemStack>();
		this.effects = new ArrayList<SPotionEffect>();
		this.whitelist = new ArrayList<String>();
		this.setModifiers(new HashMap<String, String>());
		this.attributes = new ArrayList<Attribute>();

		this.data.put("type", type.getName());
		this.data.put("id", id);
		this.data.put("useWhitelist", false);
		this.data.put("useBlacklist", true);
	}

	@Override
	public void addAttribute(Attribute att) {
		attributes.add(att);
	}

	public void addDamageBlacklist(String damageType) {
		blacklist.add(damageType.toUpperCase());
	}

	public void addDamageWhitelist(String damageType) {
		whitelist.add(damageType.toUpperCase());
	}

	public void addDrop(ItemStack drop) {
		drops.add(new SItemStack(drop));
	}

	public void addDrop(SItemStack drop) {
		drops.add(drop);
	}

	public void addInventoryItem(ItemStack stack) {
		SInventory newInv = getInventory();
		newInv.addItem(new SItemStack(stack));
		setInventory(newInv);
	}

	public void addItemDamage(ItemStack value) {
		itemDamage.add(new SItemStack(value));
	}

	public void addModifier(String moddedProp, String expression) {
		modifiers.put(moddedProp, expression);
	}

	public void addPotionEffect(SPotionEffect effect) {
		effects.add(effect);
	}

	public SpawnableEntity cloneWithNewId(int id) {
		SpawnableEntity e = new SpawnableEntity(getType(), id);
		e.setData(data);
		e.setDamageBlacklist(blacklist);
		e.setDamageWhitelist(whitelist);
		e.setItemDamageList(getItemDamageList());
		e.setEffects(effects);
		return e;
	}

	public double evaluate(String expr, Entity entity) {
		
		Heroes h = CustomSpawners.getHeroes();
		int damage = ((this.data.containsKey("damage")) ? (Integer) this.data.get("damage") : 1);
		int exp = ((this.data.containsKey("exp")) ? (Integer) this.data.get("exp") : 1);
		if(h != null) {
			if(entity instanceof LivingEntity) {
				Monster m = h.getCharacterManager().getMonster((LivingEntity) entity);
				if(m != null) {
					damage = m.getDamage();
					exp = m.getExperience();
				}
			}
		}
		
		int hp = (this.data.containsKey("health")) ? (Integer) this.data.get("health") : getMaxHealth();
		double x = (this.data.containsKey("xVelo")) ? Double.parseDouble(this.data.get("xVelo").toString()) : 0d;
		double y = (this.data.containsKey("yVelo")) ? Double.parseDouble(this.data.get("yVelo").toString()) : 0d;
		double z = (this.data.containsKey("zVelo")) ? Double.parseDouble(this.data.get("zVelo").toString()) : 0d;
		int age = (this.data.containsKey("age")) ? (Integer) this.data.get("age") : getMaxAir();
		int air = (this.data.containsKey("air")) ? (Integer) this.data.get("air") : 0;

		if (entity != null) {
			expr = expr.replaceAll("@nearplayers", "" + 
					CustomSpawners.getNearbyPlayers(entity.getLocation(), 16).size());
			if (entity instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) entity;
				hp = (int) le.getHealth();
				air = le.getRemainingAir();
				if (le instanceof Ageable) {
					age = ((Ageable) le).getAge();
				}
			}
		} else {
			expr = expr.replaceAll("@nearplayers", "" + 0);
		}

		expr = expr.replaceAll("@hp", "" + hp);
		expr = expr.replaceAll("@health", "" + hp);
		expr = expr.replaceAll("@age", "" + age);
		expr = expr.replaceAll("@air", "" + air);
		expr = expr.replaceAll("@damage", ""+ ((this.data.containsKey("damage")) ? (Integer) this.data.get("damage") : 1));
		expr = expr.replaceAll("@x", "" + x);
		expr = expr.replaceAll("@y", "" + y);
		expr = expr.replaceAll("@z", "" + z);
		expr = expr.replaceAll("@fire", "" + ((this.data.containsKey("fireTicks")) ? (Integer) this.data.get("fireTicks") : 0));
		expr = expr.replaceAll("@fuse", "" + ((this.data.containsKey("fuse")) ? (Integer) this.data.get("fuse") : 0));
		expr = expr.replaceAll("@yield","" + ((this.data.containsKey("yield")) ? (Double) this.data.get("yield") : 4.0f));
		expr = expr.replaceAll("@xp", "" + ((this.data.containsKey("exp")) ? (Integer) this.data.get("exp") : 1));
		expr = expr.replaceAll("@players", "" + Bukkit.getServer().getOnlinePlayers().length);
		expr = expr.replaceAll("@heroes_damage", "" + damage);
		expr = expr.replaceAll("@heroes_experience", "" + exp);
		expr = expr.replaceAll("@h_damage", "" + damage);
		expr = expr.replaceAll("@heroes_dmg", "" + damage);
		expr = expr.replaceAll("@h_dmg", "" + damage);
		expr = expr.replaceAll("@h_experience", "" + exp);
		expr = expr.replaceAll("@h_exp", "" + exp);
		expr = expr.replaceAll("@heroes_xp", "" + exp);
		expr = expr.replaceAll("@h_xp", "" + exp);

		return CustomSpawners.evaluate(expr);
	}

	@Override
	public int getAge(Entity en) {
		int value = (this.data.containsKey("age")) ? (Integer) this.data.get("age") : 0;
		String expr = "";
		if (hasModifier("age")) {
			expr = getModifier("age");
			try {
				return (int) Math.round(evaluate(expr, en));
			} catch (IllegalArgumentException e) {}
		}

		return value;
	}

	@Override
	public int getAir(Entity en) {
		int value = (this.data.containsKey("air")) ? (Integer) this.data.get("air") : 0;
		String expr = "";
		if (hasModifier("air")) {
			expr = getModifier("air");
			try {
				return (int) Math.abs(Math.round(evaluate(expr, en)));
			} catch (IllegalArgumentException e) {}

		}

		return value;
	}

	@Override
	public List<Attribute> getAttributes() {
		return (attributes == null) ? attributes = new ArrayList<Attribute>() : attributes;
	}

	public List<String> getBlacklist() {
		return blacklist;
	}

	@Override
	public String getCatType() {
		return (this.data.containsKey("catType")) ? (String) this.data
				.get("catType") : "WILD_OCELOT";
	}

	@Override
	public String getColor() {
		return (this.data.containsKey("color")) ? (String) this.data
				.get("color") : "WHITE";
	}

	@Override
	public int getDamage(Entity en) {
		int value = (this.data.containsKey("damage")) ? (Integer) this.data.get("damage") : 1;
		String expr = "";
		if (hasModifier("damage")) {
			expr = getModifier("damage");
			try {
				return (int) Math.abs(Math.round(evaluate(expr, en)));
			} catch (IllegalArgumentException e) {
			}

		}

		return value;
	}

	@Override
	public List<String> getDamageBlacklist() {
		return blacklist;
	}

	@Override
	public List<String> getDamageWhitelist() {
		return whitelist;
	}

	public Map<String, Object> getData() {
		return data;
	}

	@Override
	public int getDroppedExp(Entity en) {
		int value = (this.data.containsKey("exp")) ? (Integer) this.data.get("exp"): 0;
		String expr = "";
		if (hasModifier("xp")) {
			expr = getModifier("xp");
			try {
				return (int) Math.abs(Math.round(evaluate(expr, en)));
			} catch (IllegalArgumentException e) {
			}

		}

		return value;
	}

	@Override
	public List<ItemStack> getDrops() {

		List<ItemStack> drops1 = new ArrayList<ItemStack>();

		for (SItemStack i : drops) {
			drops1.add(i.toItemStack());
		}

		return drops1;
	}

	public List<SPotionEffect> getEffects() {
		return effects;
	}

	@Override
	public List<PotionEffect> getEffectsBukkit() {
		List<PotionEffect> list = new ArrayList<PotionEffect>();
		for (SPotionEffect effect : effects) {
			list.add(effect.toPotionEffect());
		}
		return list;
	}

	@Override
	public MaterialData getEndermanBlock() {
		int id = (this.data.containsKey("enderBlock")) ? (Integer) this.data
				.get("enderBlock") : 1;
		byte damage = (this.data.containsKey("enderBlockDamage")) ? (Byte) this.data
				.get("enderBlockDamage") : 0;
		MaterialData block = new MaterialData(id, damage);
		return block;
	}

	@Override
	public int getFireTicks(Entity en) {
		int value = (this.data.containsKey("fireTicks")) ? (Integer) this.data
				.get("fireTicks") : 0;
		if (hasModifier("fire")) {
			String expr = getModifier("fire");
			try {
				value = (int) Math.abs(Math.round(evaluate(expr, en)));
			} catch (IllegalArgumentException e) {
			}
		}

		return value;
	}
	
	@Override
	public int getFuseTicks(Entity en) {
		int value = (this.data.containsKey("fuse")) ? (Integer) this.data.get("fuse") : 80;
		if (hasModifier("fuse")) {
			String expr = getModifier("fuse");
			try {
				value = (int) Math.abs(Math.round(evaluate(expr, en)));
			} catch (IllegalArgumentException e) {
			}
		}

		return value;
	}

	@Override
	public int getHealth(Entity en) {
		return (int) getHealthDouble(en);
	}

	@Override
	public double getHealthDouble(Entity en) {
		double hp = (this.data.containsKey("health")) ? Double.parseDouble(this.data.get("health").toString()) : -1.0D;
		String expr = "";
		if (hasModifier("health")) {
			expr = getModifier("health");
		} else if (hasModifier("hp")) {
			expr = getModifier("hp");
		}

		if (expr.isEmpty()) {
			return hp;
		}

		try {
			return Math.abs(Math.round(evaluate(expr, en)));
		} catch (IllegalArgumentException e) {}

		return hp;
	}

	@Override
	public float getHeight() {
		return (this.data.containsKey("height")) ? (Float) this.data.get("height") : -1f;
	}

	@Override
	public HorseData.Color getHorseColor() {
		String name = (String) ((this.data.containsKey("horseColor")) ? this.data.get("horseColor") : "WHITE");
		for(HorseData.Color c : HorseData.Color.values()) {
			if(c.getName().equalsIgnoreCase(name.toLowerCase()))
				return c;
		}
		return null;
	}

	@Override
	public HorseData.Type getHorseType() {
		String name = (String) ((this.data.containsKey("horseType")) ? this.data.get("horseType") : "NORMAL");
		for(HorseData.Type t : HorseData.Type.values()) {
			if(t.getName().equalsIgnoreCase(name.toLowerCase()))
				return t;
		}
		return null;
	}

	@Override
	public HorseData.Variant getHorseVariant() {
		String name = (String) ((this.data.containsKey("horseVariant")) ? this.data.get("horseVariant") : "NONE");
		for(HorseData.Variant v : HorseData.Variant.values()) {
			if(v.getName().equalsIgnoreCase(name.toLowerCase()))
				return v;
		}
		return null;
	}

	@Override
	public int getId() {
		return (Integer) this.data.get("id");
	}

	@Override
	public SInventory getInventory() {
		return (this.data.containsKey("inv")) ? (SInventory) this.data
				.get("inv") : new SInventory();
	}

	public List<SItemStack> getItemDamage() {
		return itemDamage;
	}

	@Override
	public List<ItemStack> getItemDamageList() {

		List<ItemStack> damage1 = new ArrayList<ItemStack>();

		for (SItemStack i : itemDamage) {
			damage1.add(i.toItemStack());
		}

		return damage1;
	}

	@Override
	public List<ItemStack> getItemStackDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		for (SItemStack stack : drops) {
			list.add(stack.toItemStack());
		}
		return list;
	}

	@Override
	public ItemStack getItemType() {
		return (this.data.containsKey("itemType")) ? 
				((SItemStack) this.data.get("itemType")).toItemStack()
				: new ItemStack(0, 1, (short) 0);
	}

	@Override
	public float getLength() {
		return (this.data.containsKey("length")) ? (Float) this.data.get("length") : -1f;
	}

	@Override
	public int getMaxAir() {
		return (this.data.containsKey("maxAir")) ? (Integer) this.data.get("maxAir") : 200;
	}

	@Override
	public int getMaxHealth() {
		return (this.data.containsKey("maxHealth")) ? (Integer) this.data.get("maxHealth") : 20;
	}

	@Override
	public String getModifier(String key) {
		return modifiers.get(key);
	}

	@Override
	public Map<String, String> getModifiers() {
		return modifiers;
	}

	@Override
	public String getName() {
		return (this.data.containsKey("name")) ? (String) this.data.get("name") : "";
	}

	@Override
	public int getNPC() {
		Citizens c = CustomSpawners.getCitizens();
		if(c != null) {
			if(this.data.containsKey("npc")) 
				return (Integer) this.data.get("npc");
		}
		return -1;
	}

	public SPotionEffect getPotionEffect() {
		return (this.data.containsKey("potionEffect")) ? (SPotionEffect) this.data
				.get("potionEffect") : new SPotionEffect(
				PotionEffectType.REGENERATION, 1, 0);
	}

	@Override
	public PotionEffect getPotionEffectBukkit() {
		return getPotionEffect().toPotionEffect();
	}

	@Override
	public Villager.Profession getProfession() {
		return (this.data.containsKey("profession")) ? Villager.Profession
				.valueOf((String) this.data.get("profession"))
				: Villager.Profession.FARMER;
	}

	@Override
	public Object getProp(String key) {
		return (this.data.containsKey(key)) ? this.data.get(key) : null;
	}
	
	@Override
	public ISpawnableEntity getRider() {
		SpawnableEntity rider = (this.data.containsKey("rider")) ? (SpawnableEntity) this.data.get("rider") : null;
		if(rider != null) {
			SpawnableEntity mapped = CustomSpawners.getEntity(rider.getId());
			if(mapped != null) {
				rider = mapped;
				this.data.put("rider", mapped);
			}
		}
		return rider;
	}

	public List<SItemStack> getSItemStackDrops() {
		return drops;
	}

	@Override
	public int getSlimeSize() {
		return (this.data.containsKey("slimeSize")) ? (Integer) this.data
				.get("slimeSize") : 1;
	}

	@Override
	public Spawner getSpawnerData() {
		return (this.data.containsKey("spawner")) ? (Spawner) this.data
				.get("spawner") : null;
	}

	@Override
	public EntityType getType() {
		String name = (String) this.data.get("type");
		for (EntityType e1 : EntityType.values()) {
			if (e1.toString().equalsIgnoreCase(name)) {
				return e1;
			} else if (e1.getName().equalsIgnoreCase(name)) {
				return e1;
			}
		}

		return null;
	}

	@Override
	public Vector getVelocity(Entity en) {
		return new Vector(getXVelocity(en), getYVelocity(en), getZVelocity(en));
		// return (this.data.containsKey("velocity")) ? ((SVector)
		// this.data.get("velocity")).toVector() : new Vector(0, 0, 0);
	}

	@Override
	public Vector getVelocity2(Entity en) {
		return new Vector(getX2Velocity(en), getY2Velocity(en), getZ2Velocity(en));
	}
	
	public List<String> getWhitelist() {
		return whitelist;
	}
	
	@Override
	public float getWidth() {
		return (this.data.containsKey("width")) ? (Float) this.data
				.get("width") : -1f;
	}

	//TODO Modifiers for 2nd velocity
	@Override
	public double getX2Velocity(Entity en) {
		double value = (this.data.containsKey("xVelo2")) ? Double.parseDouble(this.data.get("xVelo2").toString()) : 0d;
		if (hasModifier("x2")) {
			String expr = getModifier("x2");
			try {
				value = evaluate(expr, en);
			} catch (IllegalArgumentException e) {
			}

		}
		return value;
	}

	@Override
	public double getXVelocity(Entity en) {
		double value = (this.data.containsKey("xVelo")) ? Double.parseDouble(this.data.get("xVelo").toString()) : 0d;
		if (hasModifier("x")) {
			String expr = getModifier("x");
			try {
				value = evaluate(expr, en);
			} catch (IllegalArgumentException e) {
			}

		}
		return value;
	}

	@Override
	public double getY2Velocity(Entity en) {
		double value = (this.data.containsKey("yVelo2")) ? Double.parseDouble(this.data.get("yVelo2").toString()) : 0d;
		if (hasModifier("y2")) {
			String expr = getModifier("y2");
			try {
				value = evaluate(expr, en);
			} catch (IllegalArgumentException e) {
			}

		}
		return value;
	}

	@Override
	public float getYield(Entity en) {
		double value = (this.data.containsKey("yield")) ? (Double) this.data.get("yield") : 4.0f;

		if (hasModifier("yield")) {
			String expr = getModifier("yield");
			try {
				value = evaluate(expr, en);
			} catch (IllegalArgumentException e) {
			}
		}

		return (float) value;
	}

	@Override
	public double getYVelocity(Entity en) {
		double value = (this.data.containsKey("yVelo")) ? Double.parseDouble(this.data.get("yVelo").toString()) : 0d;
		if (hasModifier("y")) {
			String expr = getModifier("y");
			try {
				value = evaluate(expr, en);
			} catch (IllegalArgumentException e) {
			}

		}
		return value;
	}

	@Override
	public double getZ2Velocity(Entity en) {
		double value = (this.data.containsKey("zVelo2")) ? Double.parseDouble(this.data.get("zVelo2").toString()) : 0d;
		if (hasModifier("z2")) {
			String expr = getModifier("z2");
			try {
				value = evaluate(expr, en);
			} catch (IllegalArgumentException e) {
			}

		}
		return value;
	}

	@Override
	public double getZVelocity(Entity en) {
		double value = (this.data.containsKey("zVelo")) ? Double.parseDouble(this.data.get("zVelo").toString()) : 0d;
		if (hasModifier("z")) {
			String expr = getModifier("z");
			try {
				value = evaluate(expr, en);
			} catch (IllegalArgumentException e) {
			}

		}
		return value;
	}

	@Override
	public boolean hasAllDimensions() {
		return (this.data.containsKey("height")
				&& this.data.containsKey("width") && this.data
					.containsKey("length")) ? true : false;
	}

	@Override
	public boolean hasChests() {
		return (Boolean) ((this.data.containsKey("chest")) ? this.data.get("chest") : false);
	}

	@Override
	public boolean hasModifier(String key) {
		return modifiers.containsKey(key);
	}

	@Override
	public boolean hasProp(String key) {
		return this.data.containsKey(key);
	}

	@Override
	public boolean isAngry() {
		return (this.data.containsKey("angry")) ? (Boolean) this.data
				.get("angry") : false;
	}

	@Override
	public boolean isCharged() {
		return (this.data.containsKey("charged")) ? (Boolean) this.data
				.get("charged") : false;
	}

	@Override
	public boolean isIncendiary() {
		return (this.data.containsKey("incendiary")) ? (Boolean) this.data
				.get("incendiary") : false;
	}

	@Override
	public boolean isInvulnerable() {
		return (this.data.containsKey("invul")) ? (Boolean) this.data
				.get("invul") : false;
	}

	@Override
	@Deprecated
	public boolean isJockey() {
		return (this.data.containsKey("jockey")) ? (Boolean) this.data
				.get("jockey") : false;
	}

	@Override
	public boolean isPassive() {
		return (this.data.containsKey("passive")) ? (Boolean) this.data
				.get("passive") : false;
	}

	@Override
	public boolean isSaddled() {
		return (this.data.containsKey("saddle")) ? (Boolean) this.data
				.get("saddle") : false;
	}

	@Override
	public boolean isSitting() {
		return (this.data.containsKey("sit")) ? (Boolean) this.data.get("sit")
				: false;
	}

	@Override
	public boolean isTamed() {
		return (this.data.containsKey("tame")) ? (Boolean) this.data
				.get("tame") : false;
	}
	
	@Override
	public boolean isUsingBlacklist() {
		return (this.data.containsKey("useBlacklist")) ? (Boolean) this.data
				.get("useBlacklist") : true;
	}

	@Override
	public boolean isUsingCustomDamage() {
		return (this.data.containsKey("useCustomDamage")) ? (Boolean) this.data
				.get("useCustomDamage") : false;
	}

	@Override
	public boolean isUsingCustomDrops() {
		return (this.data.containsKey("useDrops")) ? (Boolean) this.data
				.get("useDrops") : false;
	}

	@Override
	public boolean isUsingInventory() {
		return (this.data.containsKey("useInventory")) ? (Boolean) this.data
				.get("useInventory") : false;
	}

	@Override
	public boolean isUsingWhitelist() {
		return (this.data.containsKey("useWhitelist")) ? (Boolean) this.data
				.get("useWhitelist") : false;
	}

	public void remove() {
		this.data.put("id", -1);
	}

	@Override
	public void removeAttribute(Attribute att) {
		attributes.remove(att);
	}

	public void removeNulls() {
		for(String s : data.keySet()) {
			if(data.get(s) == null) {
				data.remove(s);
			}
		}
	}

	@Override
	public boolean requiresBlockBelow() {
		return (this.data.containsKey("blockBelow")) ? (Boolean) this.data
				.get("blockBelow") : true;
	}

	public void setAge(int age) {
		this.data.put("age", age);
	}

	public void setAir(int air) {
		this.data.put("air", air);
	}

	public void setAngry(boolean angry) {
		this.data.put("angry", angry);
	}

	@Override
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}

	@Override
	public void setBlockBelow(boolean value) {
		this.data.put("blockBelow", value);
	}

	public void setCatType(String catType) {
		this.data.put("catType", catType);
	}

	public void setCharged(boolean isCharged) {
		this.data.put("charged", isCharged);
	}

	public void setChests(boolean value) {
		this.data.put("chest", value);
	}

	public void setColor(String color) {
		this.data.put("color", color);
	}

	public void setDamage(int damage) {
		this.data.put("damage", damage);
	}

	public void setDamageBlacklist(List<String> blacklist2) {
		this.blacklist = blacklist2;
	}

	public void setDamageWhitelist(List<String> whitelist2) {
		this.whitelist = whitelist2;
	}

	public void setData(Map<String, Object> data) {

		if (data == null)
			return;

		for(String s : data.keySet()) {
			
			if(s.equals("id") || s.equals("name"))
				continue;
			
			this.data.put(s, data.get(s));
		}
	}

	public void setDimensions(float height, float width, float length) {
		setHeight(height);
		setWidth(width);
		setLength(length);
	}

	public void setDroppedExp(int droppedExp) {
		this.data.put("exp", droppedExp);
	}

	public void setDrops(List<ItemStack> drops2) {

		ArrayList<SItemStack> drops1 = new ArrayList<SItemStack>();

		for (ItemStack i : drops2) {
			drops1.add(new SItemStack(i));
		}

		this.drops = drops1;
	}

	public void setDropsSItemStack(List<SItemStack> drops) {
		this.drops = drops;
	}

	public void setEffects(List<SPotionEffect> effects) {
		this.effects = effects;
	}

	public void setEndermanBlock(MaterialData endermanBlock) {
		this.data.put("enderBlock", endermanBlock.getItemTypeId());
		this.data.put("enderBlockDamage", endermanBlock.getData());
	}

	public void setFireTicks(int fireTicks) {
		this.data.put("fireTicks", fireTicks);
	}

	public void setFuseTicks(int fuseTicks) {
		this.data.put("fuse", fuseTicks);
	}

	public void setHealth(double health) {
		this.data.put("health", health);
	}

	@Override
	public void setHeight(float height) {
		this.data.put("height", height);
	}

	public void setHorseColor(HorseData.Color color) {
		this.data.put("horseColor", color.getName());
	}

	public void setHorseType(HorseData.Type type) {
		this.data.put("horseType", type.getName());
	}

	public void setHorseVariant(HorseData.Variant var) {
		this.data.put("horseVariant", var.getName());
	}

	public void setIncendiary(boolean incendiary) {
		this.data.put("incendiary", incendiary);
	}

	public void setInventory(SInventory inventory) {
		this.data.put("inv", inventory);
	}

	public void setInvulnerable(boolean invulnerable) {
		this.data.put("invul", invulnerable);
	}

	public void setItemDamage(List<SItemStack> itemDamage) {
		this.itemDamage = itemDamage;
	}

	public void setItemDamageList(List<ItemStack> list) {

		List<SItemStack> damage1 = new ArrayList<SItemStack>();

		for (ItemStack i : list) {
			damage1.add(new SItemStack(i));
		}

		this.itemDamage = damage1;

	}

	public void setItemType(ItemStack itemType) {
		this.data.put("itemType", new SItemStack(itemType));
	}

	@Deprecated
	public void setJockey(boolean isJockey) {
		this.data.put("jockey", isJockey);
	}

	@Override
	public void setLength(float length) {
		this.data.put("length", length);
	}

	@Override
	public void setMaxAir(int maxAir) {
		this.data.put("maxAir", maxAir);
	}

	@Override
	public void setMaxHealth(int maxHealth) {
		this.data.put("maxHealth", maxHealth);
	}

	public void setModifiers(Map<String, String> modifiers) {
		this.modifiers = modifiers;
	}

	public void setName(String name) {
		name = ChatColor.translateAlternateColorCodes('&', name);
		
		name = name.replaceAll("__", " ");
		
		this.data.put("name", name);
	}

	public void setNPC(int npc) {
		this.data.put("npc", npc);
	}

	public void setPassive(boolean passive) {
		this.data.put("passive", passive);
	}

	public void setPotionEffect(SPotionEffect potionEffect) {
		this.data.put("potionEffect", potionEffect);
	}

	public void setProfession(Villager.Profession villagerProfession) {
		this.data.put("profession", villagerProfession.toString());
	}

	public void setProp(String key, Object value) {
		this.data.put(key, value);
	}

	public void setRider(SpawnableEntity entity) {
		if(entity == null)
			this.data.remove("rider");
		else
			this.data.put("rider", entity);
	}
	
	public void setSaddled(boolean isSaddled) {
		this.data.put("saddle", isSaddled);
	}

	public void setShowName(boolean show) {
		this.data.put("showname", show);
	}

	public void setSItemStackDrops(List<SItemStack> drops2) {
		this.drops = drops2;
	}

	public void setSitting(boolean isSitting) {
		this.data.put("sit", isSitting);
	}

	public void setSlimeSize(int slimeSize) {
		this.data.put("slimeSize", slimeSize);
	}

	public void setSpawnerData(Spawner spawner) {
		this.data.put("spawner", spawner);
	}

	public void setTamed(boolean isTamed) {
		this.data.put("tame", isTamed);
	}
	
	public void setType(EntityType type) {
		this.data.put("type", type.toString());
	}

	public void setUseBlacklist(boolean useBlacklist) {

		this.data.put("useBlacklist", useBlacklist);
		this.data.put("useWhitelist", !useBlacklist);

	}

	public void setUseWhitelist(boolean useWhitelist) {

		this.data.put("useWhitelist", useWhitelist);
		this.data.put("useBlacklist", !useWhitelist);

	}

	public void setUsingCustomDamage(boolean useCustomDamage) {
		this.data.put("useCustomDamage", useCustomDamage);
	}

	public void setUsingCustomDrops(boolean useCustomDrops) {
		this.data.put("useDrops", useCustomDrops);
	}
	
	public void setUsingInventory(boolean usingInventory) {
		this.data.put("useInventory", usingInventory);
	}
	
	public void setVelocity(SVector velocity) {
		this.data.put("velocity", velocity);
		setXVelocity(velocity.getX());
		setYVelocity(velocity.getY());
		setZVelocity(velocity.getZ());
	}
	
	public void setVelocity2(SVector velocity) {
		this.data.put("velocity2", velocity);
		setX2Velocity(velocity.getX());
		setY2Velocity(velocity.getY());
		setZ2Velocity(velocity.getZ());
	}
	
	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
	}
	
	@Override
	public void setWidth(float width) {
		this.data.put("width", width);
	}
	
	public void setX2Velocity(double xVelocity) {
		this.data.put("xVelo2", xVelocity);
	}
	
	public void setXVelocity(double xVelocity) {
		this.data.put("xVelo", xVelocity);
	}

	public void setY2Velocity(double yVelocity) {
		this.data.put("yVelo2", yVelocity);
	}
	
	public void setYield(float yield) {
		this.data.put("yield", (double) yield);
	}
	
	public void setYVelocity(double yVelocity) {
		this.data.put("yVelo", yVelocity);
	}

	public void setZ2Velocity(double zVelocity) {
		this.data.put("zVelo2", zVelocity);
	}
	
	public void setZVelocity(double zVelocity) {
		this.data.put("zVelo", zVelocity);
	}
	
	@Override
	public boolean showCustomName() {
		return (Boolean) ((this.data.containsKey("showname")) ? this.data.get("showname") : false);
	}
	
}
