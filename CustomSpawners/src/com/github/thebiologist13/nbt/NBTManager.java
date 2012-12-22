package com.github.thebiologist13.nbt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_5.CraftWorld;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_4_5.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.serialization.SInventory;
import com.github.thebiologist13.serialization.SPotionEffect;

import net.minecraft.server.v1_4_5.NBTTagCompound;
import net.minecraft.server.v1_4_5.NBTTagDouble;
import net.minecraft.server.v1_4_5.NBTTagFloat;
import net.minecraft.server.v1_4_5.NBTTagList;
import net.minecraft.server.v1_4_5.TileEntity;
import net.minecraft.server.v1_4_5.TileEntityMobSpawner;

/**
 * NBTManager (will be) a library for Bukkit and Minecraft for
 * controlling NBT in Minecraft easily from Bukkit. 
 * 
 * This library tries to parse as many values for NBT as it 
 * can within it's getter methods (does not parse unimplemented
 * properties within Bukkit).
 * 
 * Licensed under GNU-GPLv3
 * 
 * Refer to the wiki on Github for more info about NBT 
 * (Named Binary Tag) information. 
 * 
 * @author thebiologist13
 * @version 1.1
 */
public class NBTManager {
	
	/**
	 * Makes the properties similar to all Bukkit entities into NBT for Minecraft.
	 * 
	 * @param e The entity to create a NBTTagCompound from.
	 * @return A NBTTagCompound with information on the entity.
	 */
	public NBTTagCompound getEntityNBT(Entity e) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		NBTTagList pos = makeDoubleList(new double[] {
				e.getLocation().getX(),
				e.getLocation().getY(),
				e.getLocation().getZ(),
		});
		NBTTagList mot = makeDoubleList(new double[] {
				e.getVelocity().getX(),
				e.getVelocity().getY(),
				e.getVelocity().getZ(),
		});
		NBTTagList rot = makeFloatList(new float[] {
				e.getLocation().getYaw(),
				e.getLocation().getPitch()
		});

		nbt.setString("id", String.valueOf(e.getEntityId()));
		nbt.set("Pos", pos);
		nbt.set("Motion", mot);
		nbt.set("Rotation", rot);
		nbt.setShort("Fire", (short) e.getFireTicks());
		
		return nbt;
	}
	
	/**
	 * Makes the properties similar to all  Bukkit living entities into NBT for Minecraft.
	 * 
	 * @param e A Bukkit LivingEntity to create NBTTagCompound from.
	 * @return A NBTTagCompound with information on the living entity.
	 */
	public NBTTagCompound getLivingEntityNBT(LivingEntity e) {
		NBTTagCompound nbt = new NBTTagCompound();

		//Gets the size of the collection of potion effects, then makes a new
		//NBTTagCompound array of that size.
		int size = e.getActivePotionEffects().size();
		NBTTagCompound[] efArray = new NBTTagCompound[size];
		
		//Makes an iterator to get elements from the potion effect collection.
		Iterator<PotionEffect> efItr = e.getActivePotionEffects().iterator();
		
		//Goes through to each element in efArray to add the potion effect
		for(int i = 0; i < size; i++) {
			PotionEffect pe = efItr.next();
			efArray[i] = makePotionCompound((byte) pe.getType().getId(), (byte) pe.getAmplifier(), pe.getDuration());
		}
		
		//Makes into NBTTagList of compounds
		NBTTagList effects = makeCompoundList(efArray);
		
		nbt = getEntityNBT(e);
		
		nbt.setShort("Air", (short) e.getRemainingAir());
		nbt.setShort("Health", (short) e.getHealth());
		nbt.set("ActiveEffects", effects);	
		
		return nbt;
	}
	
	/**
	 * Makes the properties of a Bukkit block like a Furnace or Mob Spawner
	 * into a TileEntity with NBT for Minecraft.
	 * 
	 * @param b The block to create a NBTTagCompound from.
	 * @return A NBTTagCompound with information on the tile entity.
	 * @throws NotTileEntityException when the given block is not a TileEntity.
	 */
	public NBTTagCompound getTileEntityNBT(Block b) throws NotTileEntityException {
		NBTTagCompound nbt = new NBTTagCompound();
		
		if(!isTileEntity(b)) {
			NotTileEntityException ex = new NotTileEntityException("Parameter block is not a tile entity.");
			ex.fillInStackTrace();
			throw ex;
		}
		
		CraftWorld cw = (CraftWorld) b.getWorld();
		TileEntity te = cw.getTileEntityAt(b.getX(), b.getY(), b.getZ());
		
		nbt.setInt("x", te.x);
		nbt.setInt("y", te.y);
		nbt.setInt("z", te.z);
		
		return nbt;
	}
	
	/**
	 * Makes the properties of a Bukkit ItemStack into a ItemStack with NBT
	 * for Minecraft.
	 * 
	 * @param i An item stack to generate the NBTTagCompound from.
	 * @return A NBTTagCompound with information on the item.
	 */
	public NBTTagCompound getItemNBT(ItemStack i) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		
		int enchSize = i.getEnchantments().size();
		NBTTagCompound[] enchantments = new NBTTagCompound[enchSize];
		int enchCount = 0;
		
		for(Enchantment e : i.getEnchantments().keySet()) {
			enchantments[enchCount] = makeEnchantmentCompound((short) e.getId(), (short) i.getEnchantmentLevel(e));
			enchCount++;
		}
		
		tag.set("ench", makeCompoundList(enchantments));

		nbt.setShort("id", (short) i.getTypeId());
		nbt.setShort("Damage", (short) i.getDurability());
		nbt.setByte("Count", (byte) i.getAmount());
		nbt.setCompound("tag", tag);
		
		return nbt;
	}
	
	/**
	 * Sets the NBT of a entity to what you specify. 
	 * 
	 * @param e0 The entity to set the NBT to.
	 * @param n The NBTTagCompound containing the information to set.
	 */
	public void setEntityNBT(Entity e, NBTTagCompound n) {
		/*
		 * Looks like gibberish, but it isn't. This calls the c(NBTTagCompound nbttagcompound) 
		 * method in net.minecraft.server.Entity, which applies all the NBT to the entity and its subclasses. 
		 */
		((CraftEntity) e).getHandle().c(n);
	}
	
	/**
	 * Sets the NBT of a TileEntity to what you specify.
	 * 
	 * @param b The block that is a TileEntity to set the NBT to.
	 * @param n The NBTTagCompound containing the information to be set.
	 * @throws NotTileEntityException when the given block is not a tile entity.
	 */
	public void setTileEntityNBT(Block b, NBTTagCompound n) throws NotTileEntityException {
		if(!isTileEntity(b)) {
			NotTileEntityException ex = new NotTileEntityException("Parameter block is not a tile entity.");
			ex.fillInStackTrace();
			throw ex;
		}
		
		CraftWorld cw = (CraftWorld) b.getWorld();
		TileEntity te = cw.getTileEntityAt(b.getX(), b.getY(), b.getZ());
		
		te.a(n);
	}
	
	/**
	 * Sets the NBT of a TileEntityMobSpawner to what you specify.
	 * 
	 * @param b The block that is a TileEntityMobSpawner to set the NBT to.
	 * @param n The NBTTagCompound containing the information to be set.
	 * @throws NotTileEntityException when the given block is not a tile entity.
	 */
	public void setTileEntityMobSpawnerNBT(Block b, NBTTagCompound n) throws NotTileEntityException {
		if(!isTileEntity(b)) {
			NotTileEntityException ex = new NotTileEntityException("Parameter block is not a tile entity.");
			ex.fillInStackTrace();
			throw ex;
		}
		
		CraftWorld cw = (CraftWorld) b.getWorld();
		TileEntityMobSpawner te = (TileEntityMobSpawner) cw.getTileEntityAt(b.getX(), b.getY(), b.getZ());
		
		te.a(n);
	}
	
	/**
	 * Sets the NBT of a item to what you specify. n must include
	 * id, count, damage, and tag, otherwise it throws a NPE.
	 * 
	 * @param i The item to set the NBT to.
	 * @param n The NBTTagCompound containing the information to be set.
	 */
	public void setItemNBT(ItemStack i, NBTTagCompound n) {
		CraftItemStack.asNMSCopy(i).c(n);
	}
	
	/**
	 * Finds whether a block is a tile entity or not.
	 * 
	 * @param b The block to test.
	 * @return True if the block is a tile entity.
	 */
	public boolean isTileEntity(Block b) {
		CraftWorld w = (CraftWorld) b.getWorld();
		TileEntity e = w.getTileEntityAt(b.getX(), b.getY(), b.getZ());
		
		if(e != null)
			return true;
		
		return false;
	}
	
	/**
	 * Make a NBTTagList of doubles for positions, velocity, etc.
	 * 
	 * @param d0 Double array to make into NBTTagList
	 * @return NBTTagList form of d0.
	 */
	public NBTTagList makeDoubleList(double[] d0) {
		NBTTagList list = new NBTTagList();
		int i = d0.length;

		for(int j = 0; j < i; j++) {
			double d = d0[j];

			list.add(new NBTTagDouble((String) null, d));
		}

		return list;

	}
	
	/**
	 * Make a NBTTagList of floats for values like rotation.
	 * 
	 * @param f0 Float array to make into NBTTagList
	 * @return NBTTagList form of f0.
	 */
	public NBTTagList makeFloatList(float[] f0) {
		NBTTagList list = new NBTTagList();
		int i = f0.length;

		for(int j = 0; j < i; j++) {
			float f = f0[j];

			list.add(new NBTTagFloat((String) null, f));
		}

		return list;

	}
	
	/**
	 * Make a NBTTagList of NBTTagCompounds for values like potion effects.
	 * 
	 * @param m0 Compound array to make into NBTTagList
	 * @return NBTTagList form of m0.
	 */
	public NBTTagList makeCompoundList(NBTTagCompound[] m0) {
		NBTTagList list = new NBTTagList();
		int i = m0.length;

		for(int j = 0; j < i; j++) {
			list.add(m0[j]);
		}

		return list;

	}

	/**
	 * Makes a NBTTagCompound for a level 1 potion effect that lasts for 1 minute.
	 * 
	 * @param id The ID number of the potion effect.
	 * @return NBTTagCompound with information about potion.
	 */
	public NBTTagCompound makePotionCompound(byte id) {
		NBTTagCompound potion = new NBTTagCompound();

		potion.setByte("Id", id);
		potion.setByte("Amplifier", (byte) 1);
		potion.setInt("Duration", 1200);
		potion.setByte("Ambient", (byte) 0);

		return potion;
	}
	
	/**
	 * Makes a NBTTagCompound for a potion effect that lasts for 1 minute.
	 * 
	 * @param id The ID number of the potion effect.
	 * @param level The level of the potion effect.
	 * @param dur The duration in ticks of the potion effect.
	 * @return NBTTagCompound with information about potion.
	 */
	public NBTTagCompound makePotionCompound(byte id, byte level) {
		NBTTagCompound potion = new NBTTagCompound();

		potion.setByte("Id", id);
		potion.setByte("Amplifier", level);
		potion.setByte("Ambient", (byte) 0);

		return potion;
	}
	
	/**
	 * Makes a NBTTagCompound for a potion effect.
	 * 
	 * @param id The ID number of the potion effect.
	 * @param level The level of the potion effect.
	 * @param dur The duration in ticks of the potion effect.
	 * @return NBTTagCompound with information about potion.
	 */
	public NBTTagCompound makePotionCompound(byte id, byte level, int dur) {
		NBTTagCompound potion = new NBTTagCompound();

		potion.setByte("Id", id);
		potion.setByte("Amplifier", level);
		potion.setInt("Duration", dur);
		potion.setByte("Ambient", (byte) 0);

		return potion;
	}
	
	/**
	 * Makes a NBTTagCompound for a potion effect with an option to be ambient.
	 * 
	 * @param id The ID number of the potion effect.
	 * @param level The level of the potion effect.
	 * @param dur The duration in ticks of the potion effect.
	 * @param ambient Whether or not the effect is ambient, like a beacon.
	 * @return NBTTagCompound with information about potion.
	 */
	public NBTTagCompound makePotionCompound(byte id, byte level, int dur, boolean ambient) {
		NBTTagCompound potion = new NBTTagCompound();
		byte a = (byte) ((ambient) ? 1 : 0);

		potion.setByte("Id", id);
		potion.setByte("Amplifier", level);
		potion.setInt("Duration", dur);
		potion.setByte("Ambient", a);

		return potion;
	}
	
	/**
	 * Make a enchantment into a NBTTagCompound.
	 * 
	 * @param id The ID number of the enchantment.
	 * @return NBTTagCompound version of the enchantment
	 */
	public NBTTagCompound makeEnchantmentCompound(short id) {
		NBTTagCompound ench = new NBTTagCompound();

		ench.setShort("id", id);
		ench.setShort("level", (short) 1);

		return ench;
	}
	
	/**
	 * Make a enchantment into a NBTTagCompound.
	 * 
	 * @param id The ID number of the enchantment.
	 * @param level The level of the enchantment.
	 * @return NBTTagCompound version of the enchantment
	 */
	public NBTTagCompound makeEnchantmentCompound(short id, short level) {
		NBTTagCompound ench = new NBTTagCompound();

		ench.setShort("id", id);
		ench.setShort("level", level);

		return ench;
	}
	
	/**
	 * Generates NBT for a CustomSpawners spawner.
	 * 
	 * @param s The Spawner to generate NBT from.
	 * @return NBTTagCompound with data.
	 */
	public NBTTagCompound getSpawnerNBT(Spawner s) {
		NBTTagCompound sData = new NBTTagCompound(); //Spawner NBT
		NBTTagCompound eData = new NBTTagCompound(); //Entity NBT
		SpawnableEntity mainEntity = s.getMainEntity(); //The primary entity of the spawner.
		
		Location spawnLocation = null;
		
		if(s.isUsingSpawnArea()) {
			spawnLocation = s.getAreaPoints()[0]; //This can be changed. Really just needs a single point to spawn to. Should add option to disable.
		}

		if(s.getTypeData().size() == 1) {
			eData = makeEntityData(mainEntity, spawnLocation);
			sData.set("SpawnData", eData);
		} else {
			List<Integer> typeData = s.getTypeData();
			NBTTagCompound[] potentials = new NBTTagCompound[typeData.size()];
			
			for(Integer i = 0; i < typeData.size(); i++) {
				NBTTagCompound potentialData = new NBTTagCompound();
				SpawnableEntity e = CustomSpawners.getEntity(typeData.get(i).toString());
				NBTTagCompound eData2 = makeEntityData(e, spawnLocation);
				
				if(i == 0) {
					sData.set("SpawnData", eData2);
				}
				
				potentialData.setCompound("Properties", eData2);
				potentialData.setInt("Weight", 0);
				potentialData.setString("Type", e.getType().getName());
				potentials[i] = potentialData;
			}
			
			sData.set("SpawnPotentials", makeCompoundList(potentials));
		}
		
		// Spawner Data
		
		sData.setInt("x", s.getLoc().getBlockX());
		sData.setInt("y", s.getLoc().getBlockY());
		sData.setInt("z", s.getLoc().getBlockZ());
		sData.setString("EntityId", mainEntity.getType().getName());
		sData.setShort("SpawnCount", (short) s.getMobsPerSpawn());
		sData.setShort("SpawnRange", Short.valueOf((short) s.getRadius()));
		sData.setShort("Delay", (short) s.getRate());
		sData.setShort("MinSpawnDelay", (short) s.getRate());
		sData.setShort("MaxSpawnDelay", (short) (s.getRate() + 2));
		sData.setShort("MaxNearbyEntities", (short) s.getMaxMobs());
		sData.setShort("RequiredPlayerRange", (short) s.getMaxPlayerDistance());
		
		return sData;
	}
	
	/**
	 * Makes a mob inventory into NBT.
	 * 
	 * @param i EntityInventory to use.
	 * @return NBTTagList with inventory information.
	 */
	public NBTTagList makeInventory(SInventory i) {
		NBTTagList list = new NBTTagList();
		ArrayList<ItemStack> mainInv = i.getMainInventory();
		
		list.add(getItemNBT(mainInv.get(0)));
		list.add(getItemNBT(mainInv.get(1)));
		list.add(getItemNBT(mainInv.get(2)));
		list.add(getItemNBT(mainInv.get(3)));
		list.add(getItemNBT(mainInv.get(4)));
		
		return list;
	}
	
	/**
	 * Parses CustomSpawners health.
	 * 
	 * @param e SpawnableEntity to parse from.
	 * @return The amount.
	 */
	private int getHealth(SpawnableEntity e) {
		
		if(e.getHealth() == -2) {
			return 1;
		} else if(e.getHealth() == -1) {
			return e.getMaxHealth();
		} else {
			return e.getHealth();
		}
		
	}
	
	/**
	 * Parses CustomSpawners age.
	 * 
	 * @param e SpawnableEntity to parse from.
	 * @return The amount.
	 */
	private int getAge(SpawnableEntity e) {
		
		if(e.getAge() == -2) {
			return -24000; //Negative is a baby, and 24000 ticks is 20 minutes
		} else if(e.getAge() == -1) {
			return 0;
		} else {
			return e.getAge();
		}
		
	}
	
	/**
	 * Parses CustomSpawners air.
	 * 
	 * @param e SpawnableEntity to parse from.
	 * @return The amount.
	 */
	private int getAir(SpawnableEntity e) {

		if(e.getAir() == -2) {
			return 0;
		} else if(e.getAir() == -1) {
			return e.getMaxAir();
		} else {
			return e.getAir();
		}
				
	}
	
	/**
	 * Makes the Entity Data.
	 * 
	 * @param mainEntity The entity to make data from.
	 * @return The data.
	 */
	private NBTTagCompound makeEntityData(SpawnableEntity mainEntity, Location spawnLocation) {
		
		NBTTagCompound eData = new NBTTagCompound();
		byte sitting = (byte) ((mainEntity.isSitting()) ? 1 : 0);
		byte powered = (byte) ((mainEntity.isCharged()) ? 1 : 0);
		byte saddle = (byte) ((mainEntity.isSaddled()) ? 1 : 0);
		short angry = (short) ((mainEntity.isAngry()) ? 1 : 0);
		byte invulnerable = (byte) ((mainEntity.isInvulnerable()) ? 1 : 0);
		
		Vector velocity = mainEntity.getVelocity();
		NBTTagList motion = makeDoubleList(new double[] {velocity.getX(), velocity.getY(), velocity.getZ()});
		
		ArrayList<SPotionEffect> effectsCS = mainEntity.getEffects();
		NBTTagList effects = new NBTTagList();
		for(SPotionEffect e : effectsCS) {
			effects.add(makePotionCompound((byte) e.getType().getId(), (byte) e.getAmplifier(), e.getDuration()));
		}
		
		//Several values excluded because CustomSpawners doesn't have properties for them (rotation, fall distance, on ground, and dimension)
		
		if(!mainEntity.getType().equals(EntityType.DROPPED_ITEM)) {
			eData.setInt("Age", getAge(mainEntity));
		} else {
			eData.setShort("Age", (short) getAge(mainEntity));
		}
		
		if(!mainEntity.getType().equals(EntityType.CREEPER)) {
			eData.setShort("Fuse", (short) mainEntity.getFuseTicks());
		} else {
			eData.setByte("Fuse", (byte) mainEntity.getFuseTicks());
		}
		
		Ocelot.Type catType = Ocelot.Type.WILD_OCELOT;
		for(Ocelot.Type t : Ocelot.Type.values()) {
			if(t.toString().equalsIgnoreCase(mainEntity.getCatType()))
				catType = t;
		}
		
		if(spawnLocation != null) {
			NBTTagList pos = makeDoubleList(new double[] {spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()});
			eData.set("Pos", pos);
		}
		
		eData.setString("id", mainEntity.getType().getName());
		eData.set("Motion", motion);
		eData.setShort("Fire", (short) mainEntity.getFireTicks());
		eData.setShort("Air", (short) getAir(mainEntity));
		eData.setByte("Invulnerable", invulnerable);
		eData.set("ActiveEffects", effects);
		eData.setShort("Health", (short) getHealth(mainEntity));
		eData.set("Equipment", makeInventory(mainEntity.getInventory())); //Put other inventory things after here
		eData.setByte("Sitting", sitting);
		eData.setByte("powered", powered);
		eData.setByte("ExplosionRadius", (byte) mainEntity.getYield());
		eData.setShort("carried", (short) mainEntity.getEndermanBlock().getItemTypeId());
		eData.setShort("carriedData", mainEntity.getEndermanBlock().getData());
		eData.setInt("CatType", catType.getId());
		eData.setByte("Saddle", saddle);
		eData.setShort("Anger", angry); //Pigmen
		eData.setByte("Color", DyeColor.valueOf(mainEntity.getColor()).getData()); //Could set a sheared variable here
		eData.setInt("Size", mainEntity.getSlimeSize());
		eData.setByte("Angry", (byte) angry); //Wolf
		eData.setInt("Profession", mainEntity.getProfession().getId()); //Stuff for offers could go here
		eData.setByte("PlayerCreated", (byte) 0);
		eData.setDouble("Damage", mainEntity.getDamage()); //Arrows
		eData.setCompound("Potion", makePotionCompound((byte) mainEntity.getPotionEffect().getType().getId(), 
				(byte) mainEntity.getPotionEffect().getAmplifier(), 
				mainEntity.getPotionEffect().getDuration()));
		eData.setCompound("Item", getItemNBT(mainEntity.getItemType())); //TODO Make this include potion type
		eData.setShort("Value", (short) mainEntity.getDroppedExp());
		eData.setByte("Tile", (byte) mainEntity.getItemType().getTypeId());
		eData.setByte("Data", mainEntity.getItemType().getData().getData());
		
		return eData;
	}
	
}
