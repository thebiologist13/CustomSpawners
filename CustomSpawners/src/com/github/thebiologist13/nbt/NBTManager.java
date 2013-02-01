package com.github.thebiologist13.nbt;

import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.NBTTagDouble;
import net.minecraft.server.v1_4_R1.NBTTagList;
import net.minecraft.server.v1_4_R1.TileEntity;
import net.minecraft.server.v1_4_R1.TileEntityMobSpawner;

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
	 * Gets NBT data of a entity.
	 * Credit goes to kyran_ <br>
	 * <a href=http://forums.bukkit.org/threads/getting-entity-nbt-data.113049/>
	 * http://forums.bukkit.org/threads/getting-entity-nbt-data.113049/</a>
	 * 
	 * @author kyran_
	 * @param object Entity to get NBT data from.
	 * @return The NBT of the entity.
	 */
	public <T extends Object> NBTTagCompound getTag(T object){
        NBTTagCompound compound = new NBTTagCompound();
        
        Class<? extends Object> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods){
            if ((method.getName() == "d") && (method.getParameterTypes().length == 1) && (method.getParameterTypes()[0] == NBTTagCompound.class)){
                try {
                	method.setAccessible(true);
                    method.invoke(object, compound);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return compound;
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
		CraftItemStack.asNMSCopy(i).save(nbt);
		
		return nbt;
	}
	
	/**
	 * Sets the NBT of a entity to what you specify. 
	 * 
	 * @param e The entity to set the NBT to.
	 * @param n The NBTTagCompound containing the information to set.
	 */
	public void setEntityNBT(Entity e, NBTTagCompound n) {
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
			Location pos = (spawnLocation == null) ? s.getLoc() : spawnLocation;
			Entity e = s.forceSpawnOnLoc(mainEntity, pos);
			net.minecraft.server.v1_4_R1.Entity nmsEntity = ((CraftEntity) e).getHandle();
			eData = getTag(nmsEntity);
			
			if(eData.isEmpty()) { //If empty
				return null;
			}
			
			EntityType type = e.getType();
			String id = type.getName();
			
			if(id == null || id.isEmpty()) {
				switch(type) {
				case SPLASH_POTION:
					id = "ThrownPotion";
					break;
				case EGG:
					id = "Egg";
					break;
				default:
					return null;
				}
			}
			
			eData.setString("id", id);
			
			if(eData.hasKey("Pos") && spawnLocation == null) {
				eData.remove("Pos");
			}
			
			eData.set("Motion", makeDoubleList(new double[] 
					{mainEntity.getXVelocity(), mainEntity.getYVelocity(), mainEntity.getZVelocity()}
			));
			
			sData.set("SpawnData", eData);
			e.remove();
		} else {
			List<Integer> typeData = s.getTypeData();
			NBTTagCompound[] potentials = new NBTTagCompound[typeData.size()];
			
			for(int i = 0; i < typeData.size(); i++) {
				NBTTagCompound potentialData = new NBTTagCompound();
				SpawnableEntity se = CustomSpawners.getEntity(typeData.get(i).toString());
				Location pos = (spawnLocation == null) ? s.getLoc() : spawnLocation;
				Entity e = s.forceSpawnOnLoc(se, pos);
				net.minecraft.server.v1_4_R1.Entity nmsEntity = ((CraftEntity) e).getHandle();
				
				NBTTagCompound eData2 = getTag(nmsEntity);
				if(eData2.isEmpty()) { //If empty
					return null;
				}

				EntityType type = e.getType();
				String id = type.getName();
				
				if(id == null || id.isEmpty()) {
					switch(type) {
					case SPLASH_POTION:
						id = "ThrownPotion";
						break;
					case EGG:
						id = "Egg";
						break;
					default:
						return null;
					}
				}
				
				eData2.setString("id", id);
				
				if(eData2.hasKey("Pos") && spawnLocation == null) {
					eData2.remove("Pos");
				}
				
				eData2.set("Motion", makeDoubleList(new double[] 
						{se.getXVelocity(), se.getYVelocity(), se.getZVelocity()}
				));
				
				if(i == 0) {
					eData = eData2;
					sData.set("SpawnData", eData2);
				}
				
				potentialData.setCompound("Properties", eData2);
				potentialData.setInt("Weight", i + 1);
				potentialData.setString("Type", eData2.getString("id"));
				potentials[i] = potentialData;
				
				e.remove();
			}
			
			sData.set("SpawnPotentials", makeCompoundList(potentials));
		}
		
		// Spawner Data
		
		sData.setString("id", "MobSpawner");
		sData.setInt("x", s.getLoc().getBlockX());
		sData.setInt("y", s.getLoc().getBlockY());
		sData.setInt("z", s.getLoc().getBlockZ());
		sData.setString("EntityId", eData.getString("id"));
		sData.setShort("SpawnCount", (short) s.getMobsPerSpawn());
		sData.setShort("SpawnRange", (short) s.getRadius());
		sData.setShort("Delay", (short) s.getRate());
		sData.setShort("MinSpawnDelay", (short) s.getRate());
		sData.setShort("MaxSpawnDelay", (short) (s.getRate() + 1));
		sData.setShort("MaxNearbyEntities", (short) s.getMaxMobs());
		sData.setShort("RequiredPlayerRange", (short) s.getMaxPlayerDistance());
		
		return sData;
	}
	
	/**
	 * Make a NBTTagList of NBTTagCompounds for values like potion effects.
	 * 
	 * @param m0 Compound array to make into NBTTagList
	 * @return NBTTagList form of m0.
	 */
	private NBTTagList makeCompoundList(NBTTagCompound[] m0) {
		NBTTagList list = new NBTTagList();
		int i = m0.length;

		for(int j = 0; j < i; j++) {
			list.add(m0[j]);
		}

		return list;

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
	 *//*
	public NBTTagList makeFloatList(float[] f0) {
		NBTTagList list = new NBTTagList();
		int i = f0.length;

		for(int j = 0; j < i; j++) {
			float f = f0[j];

			list.add(new NBTTagFloat((String) null, f));
		}

		return list;

	}

	*//**
	 * Makes a NBTTagCompound for a level 1 potion effect that lasts for 1 minute.
	 * 
	 * @param id The ID number of the potion effect.
	 * @return NBTTagCompound with information about potion.
	 *//*
	public NBTTagCompound makePotionCompound(byte id) {
		NBTTagCompound potion = new NBTTagCompound();

		potion.setByte("Id", id);
		potion.setByte("Amplifier", (byte) 1);
		potion.setInt("Duration", 1200);
		potion.setByte("Ambient", (byte) 0);

		return potion;
	}
	
	*//**
	 * Makes a NBTTagCompound for a potion effect that lasts for 1 minute.
	 * 
	 * @param id The ID number of the potion effect.
	 * @param level The level of the potion effect.
	 * @param dur The duration in ticks of the potion effect.
	 * @return NBTTagCompound with information about potion.
	 *//*
	public NBTTagCompound makePotionCompound(byte id, byte level) {
		NBTTagCompound potion = new NBTTagCompound();

		potion.setByte("Id", id);
		potion.setByte("Amplifier", level);
		potion.setByte("Ambient", (byte) 0);

		return potion;
	}
	
	*//**
	 * Makes a NBTTagCompound for a potion effect.
	 * 
	 * @param id The ID number of the potion effect.
	 * @param level The level of the potion effect.
	 * @param dur The duration in ticks of the potion effect.
	 * @return NBTTagCompound with information about potion.
	 *//*
	public NBTTagCompound makePotionCompound(byte id, byte level, int dur) {
		NBTTagCompound potion = new NBTTagCompound();

		potion.setByte("Id", id);
		potion.setByte("Amplifier", level);
		potion.setInt("Duration", dur);
		potion.setByte("Ambient", (byte) 0);

		return potion;
	}
	
	*//**
	 * Makes a NBTTagCompound for a potion effect with an option to be ambient.
	 * 
	 * @param id The ID number of the potion effect.
	 * @param level The level of the potion effect.
	 * @param dur The duration in ticks of the potion effect.
	 * @param ambient Whether or not the effect is ambient, like a beacon.
	 * @return NBTTagCompound with information about potion.
	 *//*
	public NBTTagCompound makePotionCompound(byte id, byte level, int dur, boolean ambient) {
		NBTTagCompound potion = new NBTTagCompound();
		byte a = (byte) ((ambient) ? 1 : 0);

		potion.setByte("Id", id);
		potion.setByte("Amplifier", level);
		potion.setInt("Duration", dur);
		potion.setByte("Ambient", a);

		return potion;
	}
	
	*//**
	 * Make a enchantment into a NBTTagCompound.
	 * 
	 * @param id The ID number of the enchantment.
	 * @return NBTTagCompound version of the enchantment
	 *//*
	public NBTTagCompound makeEnchantmentCompound(short id) {
		NBTTagCompound ench = new NBTTagCompound();

		ench.setShort("id", id);
		ench.setShort("level", (short) 1);

		return ench;
	}
	
	*//**
	 * Make a enchantment into a NBTTagCompound.
	 * 
	 * @param id The ID number of the enchantment.
	 * @param level The level of the enchantment.
	 * @return NBTTagCompound version of the enchantment
	 *//*
	public NBTTagCompound makeEnchantmentCompound(short id, short level) {
		NBTTagCompound ench = new NBTTagCompound();

		ench.setShort("id", id);
		ench.setShort("level", level);

		return ench;
	}
	
	*//**
	 * Makes a mob inventory into NBT.
	 * 
	 * @param i EntityInventory to use.
	 * @return NBTTagList with inventory information.
	 *//*
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
	
	*//**
	 * Parses CustomSpawners health.
	 * 
	 * @param e SpawnableEntity to parse from.
	 * @return The amount.
	 *//*
	private int getHealth(SpawnableEntity e) {
		
		if(e.getHealth() == -2) {
			return 1;
		} else if(e.getHealth() == -1) {
			return e.getMaxHealth();
		} else {
			return e.getHealth();
		}
		
	}
	
	*//**
	 * Parses CustomSpawners age.
	 * 
	 * @param e SpawnableEntity to parse from.
	 * @return The amount.
	 *//*
	private int getAge(SpawnableEntity e) {
		
		if(e.getAge() == -2) {
			return -24000; //Negative is a baby, and 24000 ticks is 20 minutes
		} else if(e.getAge() == -1) {
			return 0;
		} else {
			return e.getAge();
		}
		
	}
	
	*//**
	 * Parses CustomSpawners air.
	 * 
	 * @param e SpawnableEntity to parse from.
	 * @return The amount.
	 *//*
	private int getAir(SpawnableEntity e) {

		if(e.getAir() == -2) {
			return 0;
		} else if(e.getAir() == -1) {
			return e.getMaxAir();
		} else {
			return e.getAir();
		}
				
	}
	
	*//**
	 * Makes the Entity Data.
	 * 
	 * @param mainEntity The entity to make data from.
	 * @return The data.
	 *//*
	private NBTTagCompound makeEntityData(SpawnableEntity mainEntity, Location spawnLocation, String id) {
		
		NBTTagCompound eData = new NBTTagCompound();
		byte sitting = (byte) ((mainEntity.isSitting()) ? 1 : 0);
		byte powered = (byte) ((mainEntity.isCharged()) ? 1 : 0);
		byte saddle = (byte) ((mainEntity.isSaddled()) ? 1 : 0);
		short angry = (short) ((mainEntity.isAngry()) ? 1 : 0);
		byte invulnerable = (byte) ((mainEntity.isInvulnerable()) ? 1 : 0);
		byte isWither = 0;
		byte isBaby = 0;
		byte isVillager = 0;
		
		if(mainEntity.hasProp("wither")) {
			isWither = (byte) (((Boolean) (mainEntity.getProp("wither"))) ? 1 : 0);
		}
		
		if(mainEntity.getAge() < -1) {
			isBaby = 1;
		}
		
		if(mainEntity.hasProp("zombie")) {
			isVillager = (byte) (((Boolean) (mainEntity.getProp("zombie"))) ? 1 : 0);
		}
		
		Vector velocity = mainEntity.getVelocity();
		NBTTagList motion = makeDoubleList(new double[] {velocity.getX(), velocity.getY(), velocity.getZ()});
		
		List<SPotionEffect> effectsCS = mainEntity.getEffects();
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
		
		if(mainEntity.getType().equals(EntityType.CREEPER)) {
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
		
		if(!mainEntity.getInventory().isEmpty()) {
			eData.set("Equipment", makeInventory(mainEntity.getInventory()));
		}
		
		eData.setString("id", id);
		eData.set("Motion", motion);
		eData.setShort("Fire", (short) mainEntity.getFireTicks());
		eData.setShort("Air", (short) getAir(mainEntity));
		eData.setByte("Invulnerable", invulnerable);
		eData.set("ActiveEffects", effects);
		eData.setShort("Health", (short) getHealth(mainEntity));
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
		eData.setCompound("Item", getItemNBT(mainEntity.getItemType()));
		eData.setShort("Value", (short) mainEntity.getDroppedExp());
		eData.setByte("Tile", (byte) mainEntity.getItemType().getTypeId());
		eData.setByte("Data", mainEntity.getItemType().getData().getData());
		eData.setByte("SkeletonType", isWither);
		eData.setByte("isBaby", isBaby);
		eData.setByte("isVillager", isVillager);
		
		return eData;
	}*/
	
}
