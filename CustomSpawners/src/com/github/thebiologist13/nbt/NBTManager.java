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
		net.minecraft.server.v1_4_R1.Entity nms = ((CraftEntity) e).getHandle();
		Class<?> entityClass = nms.getClass();
		Method[] methods = entityClass.getMethods();
        for (Method method : methods){
            if ((method.getName() == "a") && (method.getParameterTypes().length == 1) && (method.getParameterTypes()[0] == NBTTagCompound.class)){
                try {
                	method.setAccessible(true);
                    method.invoke(nms, n);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        
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
	
}
