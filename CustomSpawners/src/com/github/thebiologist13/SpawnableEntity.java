package com.github.thebiologist13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@SerializableAs("SpawnableEntity")
public class SpawnableEntity implements ConfigurationSerializable {
	
	/*
	 * Spawnable Entities are entities that
	 * Custom Spawners can spawn. They have lots
	 * of data associated with them.
	 */
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	//Basic Data
	private ArrayList<EntityPotionEffect> effects = new ArrayList<EntityPotionEffect>();
	//Damage Whitelist
	private ArrayList<String> whitelist = new ArrayList<String>();
	//Damage Blacklist
	private ArrayList<String> blacklist = new ArrayList<String>();
	//Items that do or do not inflict damage
	private ArrayList<ItemStack> itemDamage = new ArrayList<ItemStack>(); 
	//Drops
	private ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	//TODO isZombie prop
	//TODO isWither prop
	//TODO hurtMobs when falling
	//TODO add config defaults
	
	//Initialize a SpawnableEntity
	public SpawnableEntity(EntityType type, int id) {
		this.data.put("type", type);
		this.data.put("id", id);
		this.data.put("useWhitelist", false);
		this.data.put("useBlacklist", true);
	}
	
	public void setData(Map<String, Object> data) {
		
		if(data == null)
			return;
		
		data.put("id", (Integer) this.data.get("id"));
		data.put("name", (String) this.data.get("name"));
		
		this.data = data;
	}

	public Object getProp(String key) {
		return (data.containsKey(key)) ? data.get(key) : null;
	}
	
	public void setProp(String key, Object value) {
		data.put(key, value);
	}
	
	public boolean hasProp(String key) {
		return data.containsKey(key);
	}
	
	public String getName() {
		return (data.containsKey("name")) ? (String) data.get("name") : "";
	}

	public void setName(String name) {
		data.put("name", name);
	}

	public EntityType getType() {
		return (EntityType) data.get("type");
	}

	public void setType(EntityType type) {
		data.put("type", type);
	}

	public ArrayList<EntityPotionEffect> getEffects() {
		return effects;
	}

	public void setEffects(ArrayList<EntityPotionEffect> effects) {
		this.effects = effects;
	}
	
	public void addPotionEffect(EntityPotionEffect effect) {
		effects.add(effect);
	}

	public double getXVelocity() {
		return (data.containsKey("xVelo")) ? (Double) data.get("xVelo") : 0d;
	}

	public void setXVelocity(double xVelocity) {
		data.put("xVelo", xVelocity);
	}

	public double getYVelocity() {
		return (data.containsKey("yVelo")) ? (Double) data.get("yVelo") : 0d;
	}

	public void setYVelocity(double yVelocity) {
		data.put("yVelo", yVelocity);
	}

	public double getZVelocity() {
		return (data.containsKey("zVelo")) ? (Double) data.get("zVelo") : 0d;
	}

	public void setZVelocity(double zVelocity) {
		data.put("zVelo", zVelocity);
	}

	public int getAge() {
		return (data.containsKey("age")) ? (Integer) data.get("age") : 0;
	}

	public void setAge(int age) {
		data.put("age", age);
	}

	public int getHealth() {
		return (data.containsKey("health")) ? (Integer) data.get("health") : 1;
	}

	public void setHealth(int health) {
		data.put("health", health);
	}

	public MaterialData getEndermanBlock() {
		return (data.containsKey("enderBlock")) ? (MaterialData) data.get("enderBlock") : new MaterialData(1);
	}

	public void setEndermanBlock(MaterialData endermanBlock) {
		data.put("enderBlock", endermanBlock);
	}

	public boolean isSaddled() {
		return (data.containsKey("saddle")) ? (Boolean) data.get("saddle") : false;
	}

	public void setSaddled(boolean isSaddled) {
		data.put("saddle", isSaddled);
	}

	public boolean isCharged() {
		return (data.containsKey("charged")) ? (Boolean) data.get("charged") : false;
	}

	public void setCharged(boolean isCharged) {
		data.put("charged", isCharged);
	}

	public boolean isJockey() {
		return (data.containsKey("jockey")) ? (Boolean) data.get("jockey") : false;
	}

	public void setJockey(boolean isJockey) {
		data.put("jockey", isJockey);
	}

	public boolean isTamed() {
		return (data.containsKey("tame")) ? (Boolean) data.get("tame") : false;
	}

	public void setTamed(boolean isTamed) {
		data.put("tame", isTamed);
	}

	public boolean isAngry() {
		return (data.containsKey("angry")) ? (Boolean) data.get("angry") : false;
	}

	public void setAngry(boolean angry) {
		data.put("angry", angry);
	}

	public boolean isSitting() {
		return (data.containsKey("sit")) ? (Boolean) data.get("sit") : false;
	}

	public void setSitting(boolean isSitting) {
		data.put("sit", isSitting);
	}

	public String getCatType() {
		return (data.containsKey("catType")) ? (String) data.get("catType") : "BLACK_CAT";
	}

	public void setCatType(String catType) {
		data.put("catType", catType);
	}

	public int getSlimeSize() {
		return (data.containsKey("slimeSize")) ? (Integer) data.get("slimeSize") : 1;
	}

	public void setSlimeSize(int slimeSize) {
		data.put("slimeSize", slimeSize);
	}

	public String getColor() {
		return (data.containsKey("color")) ? (String) data.get("color") : "WHITE";
	}

	public void setColor(String color) {
		data.put("color", color);
	}

	public int getId() {
		return (Integer) data.get("id");
	}

	public Villager.Profession getProfession() {
		return (data.containsKey("profession")) ? (Villager.Profession) data.get("profession") : Villager.Profession.FARMER;
	}

	public void setProfession(Villager.Profession villagerProfession) {
		data.put("profession", villagerProfession);
	}

	public Vector getVelocity() {
		return (data.containsKey("velocity")) ? (Vector) data.get("velocity") : new Vector(0, 0, 0);
	}

	public void setVelocity(Vector velocity) {
		data.put("velocity", velocity);
		setXVelocity(velocity.getX());
		setYVelocity(velocity.getY());
		setZVelocity(velocity.getZ());
	}

	public int getAir() {
		return (data.containsKey("air")) ? (Integer) data.get("air") : 0;
	}

	public void setAir(int air) {
		data.put("air", air);
	}
	
	public void remove() {
		data.put("id", -1);
	}
	
	public boolean isPassive() {
		return (data.containsKey("passive")) ? (Boolean) data.get("passive") : false;
	}

	public void setPassive(boolean passive) {
		data.put("passive", passive);
	}

	public int getFireTicks() {
		return (data.containsKey("fire")) ? (Integer) data.get("fire") : 0;
	}

	public void setFireTicks(int fireTicks) {
		data.put("fireTicks", fireTicks);
	}

	public ArrayList<String> getDamageWhitelist() {
		return whitelist;
	}

	public void setDamageWhitelist(ArrayList<String> damageWhitelist) {
		this.whitelist = damageWhitelist;
	}
	
	public void addDamageWhitelist(String damageType) {
		whitelist.add(damageType);
	}

	public ArrayList<String> getDamageBlacklist() {
		return blacklist;
	}

	public void setDamageBlacklist(ArrayList<String> damageBlacklist) {
		this.blacklist = damageBlacklist;
	}
	
	public void addDamageBlacklist(String damageType) {
		blacklist.add(damageType);
	}

	public boolean isUsingWhitelist() {
		return (data.containsKey("useWhitelist")) ? (Boolean) data.get("useWhitelist") : false;
	}

	public void setUseWhitelist(boolean useWhitelist) {
		
		data.put("useWhitelist", useWhitelist);
		data.put("useBlacklist", !useWhitelist);
		
	}

	public boolean isUsingBlacklist() {
		return (data.containsKey("useBlacklist")) ? (Boolean) data.get("useBlacklist") : true;
	}

	public void setUseBlacklist(boolean useBlacklist) {
		
		data.put("useBlacklist", useBlacklist);
		data.put("useWhitelist", !useBlacklist);
		
	}

	public ArrayList<ItemStack> getItemDamageList() {
		return itemDamage;
	}

	public void setItemDamageList(ArrayList<ItemStack> itemDamage) {
		this.itemDamage = itemDamage;
	}
	
	public void addItemDamage(ItemStack value) {
		itemDamage.add(value);
	}

	public boolean isUsingCustomDamage() {
		return (data.containsKey("useCustomDamage")) ? (Boolean) data.get("useCustomDamage") : false;
	}

	public void setUsingCustomDamage(boolean useCustomDamage) {
		data.put("useCustomDamage", useCustomDamage);
	}

	public int getDamage() {
		return (data.containsKey("damage")) ? (Integer) data.get("damage") : 2;
	}

	public void setDamage(int damage) {
		data.put("damage", damage);
	}

	public EntityPotionEffect getPotionEffect() {
		return (data.containsKey("potionType")) ? (EntityPotionEffect) data.get("potionType") : new EntityPotionEffect(PotionEffectType.REGENERATION, 1, 0);
	}

	public void setPotionEffect(EntityPotionEffect potionEffect) {
		data.put("potionEffect", potionEffect);
	}

	public int getDroppedExp() {
		return (data.containsKey("exp")) ? (Integer) data.get("exp") : 1;
	}

	public void setDroppedExp(int droppedExp) {
		data.put("exp", droppedExp);
	}

	public int getFuseTicks() {
		return (data.containsKey("fuse")) ? (Integer) data.get("fuse") : 80;
	}

	public void setFuseTicks(int fuseTicks) {
		data.put("fuse", fuseTicks);
	}

	public float getYield() {
		return (data.containsKey("yield")) ? (Float) data.get("yield") : 4.0f;
	}

	public void setYield(float yield) {
		data.put("yield", yield);
	}

	public boolean isIncendiary() {
		return (data.containsKey("incendiary")) ? (Boolean) data.get("incendiary") : false;
	}

	public void setIncendiary(boolean incendiary) {
		data.put("incendiary", incendiary);
	}

	public ArrayList<ItemStack> getDrops() {
		return drops;
	}

	public void setDrops(ArrayList<ItemStack> drops) {
		this.drops = drops;
	}
	
	public void addDrop(ItemStack drop) {
		drops.add(drop);
	}

	public ItemStack getItemType() {
		return (data.containsKey("itemType")) ? (ItemStack) data.get("itemType") : new ItemStack(1, 1, (short) 0);
	}

	public void setItemType(ItemStack itemType) {
		data.put("itemType", itemType);
	}

	public boolean isUsingCustomDrops() {
		return (data.containsKey("useDrops")) ? (Boolean) data.get("useDrops") : false;
	}

	public void setUsingCustomDrops(boolean useCustomDrops) {
		data.put("useDrops", useCustomDrops);
	}

	public int getMaxHealth() {
		return (data.containsKey("maxHealth")) ? (Integer) data.get("maxHealth") : 20;
	}

	public void setMaxHealth(int maxHealth) {
		data.put("maxHealth", maxHealth);
	}

	public int getMaxAir() {
		return (data.containsKey("maxAir")) ? (Integer) data.get("maxAir") : 200;
	}

	public void setMaxAir(int maxAir) {
		data.put("maxAir", maxAir);
	}

	public boolean isInvulnerable() {
		return (data.containsKey("invul")) ? (Boolean) data.get("invul") : false;
	}

	public void setInvulnerable(boolean invulnerable) {
		data.put("invul", invulnerable);
	}

	public EntityInventory getInventory() {
		return (data.containsKey("inv")) ? (EntityInventory) data.get("inv") : new EntityInventory();
	}

	public void setInventory(EntityInventory inventory) {
		data.put("inv", inventory);
	}

	public boolean isUsingInventory() {
		return (data.containsKey("useInventory")) ? (Boolean) data.get("useInventory") : false;
	}

	public void setUsingInventory(boolean usingInventory) {
		data.put("useInventory", usingInventory);
	}
	
	public float getHeight() {
		return (data.containsKey("height")) ? (Float) data.get("height") : 1f;
	}
	
	public float getWidth() {
		return (data.containsKey("width")) ? (Float) data.get("width") : 1f;
	}
	
	public float getDepth() {
		return (data.containsKey("depth")) ? (Float) data.get("depth") : 1f;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		map.put("effects", effects);
		map.put("whitelist", whitelist);
		map.put("blacklist", blacklist);
		map.put("itemDamage", itemDamage);
		map.put("drops", drops);
		return map;
	}
	
	public static SpawnableEntity deserialize(Map<String, Object> map) {
		SpawnableEntity e;
		Object data = map.get("data");
		Object effects = map.get("effects");
		Object white = map.get("whitelist");
		Object black = map.get("blacklist");
		Object itemDamage = map.get("itemDamage");
		Object drops = map.get("drops");
		
		if(data instanceof Map) {
			Map<?,?> dataMap = (Map<?,?>) data;
			e = new SpawnableEntity((EntityType) dataMap.get("type"), (Integer) dataMap.get("id"));
		} else {
			return null;
		}
		
		if(effects instanceof List) {
			List<?> effectsList = (List<?>) effects;
			for(Object o : effectsList) {
				
				if(o instanceof EntityPotionEffect) {
					e.addPotionEffect((EntityPotionEffect) o);
				}
				
			}
		}
		
		if(white instanceof List) {
			List<?> list = (List<?>) white;
			for(Object o : list) {
				
				if(o instanceof String) {
					e.addDamageWhitelist((String) o);
				}
				
			}
		}
		
		if(black instanceof List) {
			List<?> list = (List<?>) black;
			for(Object o : list) {
				
				if(o instanceof String) {
					e.addDamageBlacklist((String) o);
				}
				
			}
		}
		
		if(itemDamage instanceof List) {
			List<?> list = (List<?>) itemDamage;
			for(Object o : list) {
				
				if(o instanceof ItemStack) {
					e.addItemDamage((ItemStack) o);
				}
				
			}
		}
		
		if(drops instanceof List) {
			List<?> list = (List<?>) drops;
			for(Object o : list) {
				
				if(o instanceof ItemStack) {
					e.addDrop((ItemStack) o);
				}
				
			}
		}
		
		return e;
	}

}
