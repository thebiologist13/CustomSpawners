package com.github.thebiologist13;

import java.util.ArrayList;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CSNBTManager extends NBTManager {
	
	public NBTTagCompound getSpawnerNBT(Spawner s) {
		NBTTagCompound sData = new NBTTagCompound(); //Spawner NBT
		NBTTagCompound eData = new NBTTagCompound(); //Entity NBT
		SpawnableEntity mainEntity = getMainEntity(s); //The primary entity of the spawner.
		
		byte sitting = (byte) ((mainEntity.isSitting()) ? 1 : 0);
		byte powered = (byte) ((mainEntity.isCharged()) ? 1 : 0);
		byte saddle = (byte) ((mainEntity.isSaddled()) ? 1 : 0);
		short angry = (short) ((mainEntity.isAngry()) ? 1 : 0);
		byte invulnerable = (byte) ((mainEntity.isInvulnerable()) ? 1 : 0);
		
		Location spawnLocation = s.getAreaPoints()[0]; //This can be changed. Really just needs a single point to spawn to. Should add option to disable.
		Vector velocity = mainEntity.getVelocity();
		NBTTagList pos = makeDoubleList(new double[] {spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()});
		NBTTagList motion = makeDoubleList(new double[] {velocity.getX(), velocity.getY(), velocity.getZ()});
		
		ArrayList<EntityPotionEffect> effectsCS = mainEntity.getEffects();
		NBTTagList effects = new NBTTagList();
		for(EntityPotionEffect e : effectsCS) {
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
		
		eData.setString("id", mainEntity.getType().getName());
		eData.set("Pos", pos);
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
		eData.setInt("CatType", Ocelot.Type.valueOf(mainEntity.getCatType()).getId());
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
		
		// Spawner Data
		
		sData.setInt("x", s.getLoc().getBlockX());
		sData.setInt("y", s.getLoc().getBlockY());
		sData.setInt("z", s.getLoc().getBlockZ());
		sData.setString("EntityId", mainEntity.getType().getName());
		sData.setShort("SpawnCount", (short) s.getMobsPerSpawn());
		sData.setShort("SpawnRange", (short) s.getRadius());
		sData.setShort("Delay", (short) s.getRate());
		sData.setShort("MinSpawnDelay", (short) s.getRate());
		sData.setShort("MaxSpawnDelay", (short) (s.getRate() + 2));
		sData.setShort("MaxNearbyEntities", (short) s.getMaxMobs());
		sData.setShort("RequiredPlayerRange", (short) s.getMaxPlayerDistance());
		sData.set("SpawnData", eData);
		
		return sData;
	}
	
	//Makes the mob inventory
	private NBTTagList makeInventory(EntityInventory i) {
		NBTTagList list = new NBTTagList();
		ArrayList<ItemStack> mainInv = i.getMainInventory();
		
		list.add(getItemNBT(mainInv.get(0)));
		list.add(getItemNBT(mainInv.get(1)));
		list.add(getItemNBT(mainInv.get(2)));
		list.add(getItemNBT(mainInv.get(3)));
		list.add(getItemNBT(mainInv.get(4)));
		
		return list;
	}
	
	//Parses health
	private int getHealth(SpawnableEntity e) {
		
		if(e.getHealth() == -2) {
			return 1;
		} else if(e.getHealth() == -1) {
			return e.getMaxHealth();
		} else {
			return e.getHealth();
		}
		
	}
	
	//Parses age
	private int getAge(SpawnableEntity e) {
		
		if(e.getAge() == -2) {
			return -24000; //Negative is a baby, and 24000 ticks is 20 minutes
		} else if(e.getAge() == -1) {
			return 0;
		} else {
			return e.getAge();
		}
		
	}
	
	//Parses air
	private int getAir(SpawnableEntity e) {

		if(e.getAir() == -2) {
			return 0;
		} else if(e.getAir() == -1) {
			return e.getMaxAir();
		} else {
			return e.getAir();
		}
				
	}
	
	//Gets the main entity of a spawner
	private SpawnableEntity getMainEntity(Spawner s) {
		return (SpawnableEntity) s.getTypeData().keySet().toArray()[0]; //May throw a ClassCastException
	}

}
