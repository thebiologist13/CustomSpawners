package com.github.thebiologist13.v1_5_R2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import net.minecraft.server.v1_5_R2.AxisAlignedBB;
import net.minecraft.server.v1_5_R2.EntityEnderPearl;
import net.minecraft.server.v1_5_R2.EntityFallingBlock;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.EntityMinecartAbstract;
import net.minecraft.server.v1_5_R2.EntityMinecartMobSpawner;
import net.minecraft.server.v1_5_R2.EntityPotion;
import net.minecraft.server.v1_5_R2.MobSpawnerAbstract;
import net.minecraft.server.v1_5_R2.NBTBase;
import net.minecraft.server.v1_5_R2.NBTTagCompound;
import net.minecraft.server.v1_5_R2.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftFallingSand;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftMinecart;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Golem;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Spider;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WaterMob;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import com.github.thebiologist13.api.ISInventory;
import com.github.thebiologist13.api.ISItemStack;
import com.github.thebiologist13.api.ISpawnManager;
import com.github.thebiologist13.api.ISpawnableEntity;
import com.github.thebiologist13.api.ISpawner;
import com.github.thebiologist13.v1_5_R2.Converter;

public class SpawnManager implements ISpawnManager {
	
	private ISpawner spawner;
	
	public SpawnManager(ISpawner spawner) {
		this.spawner = spawner;
	}

	@Override
	public void forceSpawn() {
		mainSpawn(spawner.randType(), true);
	}
	
	@Override
	public void forceSpawn(ISpawnableEntity entity) {
		mainSpawn(entity, true);
	}

	@Override
	public void spawn() {
		
		boolean hasPower = spawner.getBlock().isBlockPowered() 
				|| spawner.getBlock().isBlockIndirectlyPowered();

		/*
		 * This block checks if the conditions are met to spawn mobs
		 */
		if(spawner.isRedstoneTriggered() && !hasPower) {
			return;
		} else if(!isPlayerNearby()) {
			return;
		} else if(spawner.getMobsIds().size() > spawner.getMaxMobs()) {
			return;
		} else if(!((getLight() <= spawner.getMaxLightLevel()) && (getLight() >= spawner.getMinLightLevel()))) {
			return;
		}
		
		mainSpawn(spawner.randType(), false);
	}
	
	@Override
	public Entity spawnMobAt(ISpawnableEntity entity, Location location) {
		Entity e = spawnTheEntity(entity, location);
		assignMobProps(e, entity);
		return e;
	}

	private Entity addRider(Entity vehicle, ISpawnableEntity riderData) {
		Location vehicleLoc = vehicle.getLocation();
		Entity rider = spawnMobAt(riderData, vehicleLoc);
		vehicle.setPassenger(rider);
		spawner.addSecondaryMob(rider.getUniqueId(), vehicle.getUniqueId());
		return rider;
	}

	private boolean areaEmpty(Location loc, boolean spawnInWater, float height, float width, float length) {
		height = (float) Math.floor(height) + 1.0f;
		width = (float) Math.floor(width) + 1.0f;
		length = (float) Math.floor(length) + 1.0f;

		for(int y = 0; y <= height; y++) {
			double testY = loc.getY() + y;
			for(int x = 0; x <= width; x++) {
				double testX = loc.getX() + x;
				for(int z = 0; z <= length; z++) {
					double testZ = loc.getZ() + z;
					Location testLoc = new Location(loc.getWorld(), testX, testY, testZ);
					if(!isEmpty(testLoc, spawnInWater))
						return false;
				}
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	private void assignMobProps(Entity baseEntity, ISpawnableEntity data) {

		//This needs to be before everything else!
		if(data.getRider() != null) {
			addRider(baseEntity, data.getRider());
		}
		
		baseEntity.setVelocity(data.getVelocity().clone());
		baseEntity.setFireTicks(data.getFireTicks());
		
		if(baseEntity instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) baseEntity;
			setBasicProps(entity, data);

			if(data.showCustomName()) {
				CraftLivingEntity le = (CraftLivingEntity) entity;
				le.setCustomName(data.getName());
				le.setCustomNameVisible(true);
			}
			
			if(entity instanceof Ageable) {
				Ageable a = (Ageable) entity;
				setAgeProps(a, data);
			}

			if(entity instanceof Animals) {
				Animals animal = (Animals) entity;

				//Setting animal specific properties
				if(animal instanceof Pig) {
					Pig p = (Pig) animal;
					p.setSaddle(data.isSaddled());
				} else if(animal instanceof Sheep) {
					Sheep s = (Sheep) animal;
					DyeColor color = DyeColor.valueOf(data.getColor());
					s.setColor(color);
				} else if(animal instanceof Wolf) {
					Wolf w = (Wolf) animal;
					w.setAngry(data.isAngry());
					w.setTamed(data.isTamed());
					if(data.isTamed()) {

						ArrayList<Player> nearPlayers = getNearbyPlayers(w.getLocation(), 16);
						int index = (int) Math.round(Math.rint(nearPlayers.size() - 1));
						if(nearPlayers != null) {
							w.setOwner(nearPlayers.get(index));
						}

						w.setSitting(data.isSitting());

					}
				} else if(animal instanceof Ocelot) {
					Ocelot o = (Ocelot) animal;
					o.setTamed(data.isTamed());
					if(data.isTamed()) {
						Ocelot.Type catType = Ocelot.Type.valueOf(data.getCatType());
						o.setCatType(catType);

						ArrayList<Player> nearPlayers = getNearbyPlayers(o.getLocation(), 16);
						int index = (int) Math.round(Math.rint(nearPlayers.size() - 1));
						if(nearPlayers != null) {
							o.setOwner(nearPlayers.get(index));
						}

						o.setSitting(data.isSitting());

					}
				}
			} else if(entity instanceof Villager) {
				Villager v = (Villager) entity;
				v.setAge(data.getAge());
				v.setProfession(data.getProfession());
			} else if(entity instanceof Monster) {
				Monster monster = (Monster) entity;

				//Setting monster specific properties.
				if(monster instanceof Enderman) {
					Enderman e = (Enderman) monster;
					e.setCarriedMaterial(data.getEndermanBlock());
				} else if(monster instanceof Creeper) {
					Creeper c = (Creeper) monster;
					c.setPowered(data.isCharged());
				} else if(monster instanceof PigZombie) {
					PigZombie p = (PigZombie) monster;
					if(data.isAngry()) {
						p.setAngry(true);
					}
					p.setBaby((data.getAge() < -1) ? true : false);
				} else if(monster instanceof Spider) {
					Spider s = (Spider) monster;
					if(data.isJockey()) {
						makeJockey(s, data);
					}
				} else if(monster instanceof Zombie) {
					Zombie z = (Zombie) monster;
					boolean isVillager = false;
					if(data.hasProp("zombie"))
						isVillager = (Boolean) (data.getProp("zombie"));
					z.setBaby((data.getAge() < -1) ? true : false);
					z.setVillager(isVillager);
				} else if(monster instanceof Skeleton) {
					Skeleton sk = (Skeleton) monster;
					SkeletonType skType = SkeletonType.NORMAL;

					if(data.hasProp("wither"))
						skType = ((Boolean) (data.getProp("wither")) == true) ? SkeletonType.WITHER : SkeletonType.NORMAL;

					sk.setSkeletonType(skType);

				}
			} else if(entity instanceof Golem) {
				Golem golem = (Golem) entity;

				if(golem instanceof IronGolem) {
					IronGolem i = (IronGolem) golem;
					if(data.isAngry()) {
						ArrayList<Player> nearPlayers = getNearbyPlayers(i.getLocation(), 16);
						int index = (int) Math.round(Math.rint(nearPlayers.size() - 1));
						if(nearPlayers != null) {
							i.setPlayerCreated(false);
							i.damage(0, nearPlayers.get(index));
							i.setTarget(nearPlayers.get(index));
						}
					}
				}
				//Some are not classified as animals or monsters
			} else if(entity instanceof Slime) {
				Slime s = (Slime) entity;
				s.setSize(data.getSlimeSize());
			} else if(entity instanceof MagmaCube) {
				MagmaCube m = (MagmaCube) entity;
				m.setSize(data.getSlimeSize());
			}

		} else if(baseEntity instanceof Projectile) {
			Projectile pro = (Projectile) baseEntity;

			//Eventually add explosive arrows and such :D

			if(pro instanceof Fireball) {
				Fireball f = (Fireball) pro;
				setExplosiveProps(f, data);
				f.setVelocity(new Vector(0, 0, 0));
				f.setDirection(data.getVelocity());
			} else if(pro instanceof SmallFireball) {
				SmallFireball f = (SmallFireball) pro;
				setExplosiveProps(f, data);
				f.setVelocity(new Vector(0, 0, 0));
				f.setDirection(data.getVelocity());
			}

		} else if(baseEntity instanceof Explosive) {

			Explosive ex = (Explosive) baseEntity;

			if(ex instanceof TNTPrimed) {
				TNTPrimed tnt = (TNTPrimed) ex;
				setExplosiveProps(tnt, data);
				tnt.setFuseTicks(data.getFuseTicks());
			}

		} else if(baseEntity instanceof Firework) {

			Firework f = (Firework) baseEntity;
			ItemMeta meta = data.getItemType().getItemMeta();
			if(meta != null) {
				if(meta instanceof FireworkMeta) {
					FireworkMeta fMeta = (FireworkMeta) meta;
					if(fMeta != null) {
						f.setFireworkMeta(fMeta);
					}

				}

			} 

		} else if(baseEntity instanceof Minecart) {

			Minecart m = (Minecart) baseEntity;
			if(data.hasProp("minecartSpeed")) {
				m.setMaxSpeed((Double) data.getProp("minecartSpeed"));
			}

		}
		
		setNBT(baseEntity, data);
		
	}

	private boolean getBlockBelowFromEntity(Entity e) {

		boolean reqsBlockBelow = true;

		switch(e.getType()) {
		case ENDER_DRAGON:
			reqsBlockBelow = false;
			break;
		case WITHER:
			reqsBlockBelow = false;
			break;
		case BLAZE:
			reqsBlockBelow = false;
			break;
		case BAT:
			reqsBlockBelow = false;
			break;
		default:
			reqsBlockBelow = true;
			if(e instanceof Flying)
				reqsBlockBelow = false;
			if(e instanceof WaterMob)
				reqsBlockBelow = false;
			if(!(e instanceof LivingEntity))
				reqsBlockBelow = false;
			break;
		}

		return reqsBlockBelow;

	}

	private byte getLight() {
		Block blk = spawner.getBlock();
		Location loc = spawner.getLoc();
		byte highest = 0;
		int x = blk.getX();
		int y = blk.getY();
		int z = blk.getZ();
		Block north = loc.getWorld().getBlockAt(x, y, z - 1);
		Block south = loc.getWorld().getBlockAt(x, y, z + 1);
		Block east = loc.getWorld().getBlockAt(x + 1, y, z);
		Block west = loc.getWorld().getBlockAt(x - 1, y, z);
		Block up = loc.getWorld().getBlockAt(x, y + 1, z);
		Block down = loc.getWorld().getBlockAt(x, y - 1, z);
		Block[] blocks = {north, south, east, west, up, down};
		for(Block b : blocks) {
			byte level = b.getLightLevel();
			if(level > highest) {
				highest = level;
			}
		}

		return highest;
	}

	private ArrayList<Player> getNearbyPlayers(Location source, double max) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			double distanceSquared = p.getLocation().distanceSquared(source);
			if (distanceSquared <= Math.pow((double) max, 2)) {
				players.add(p);
			}
		}
		return players;
	}
	
	private Location getSpawningLocation(ISpawnableEntity type, boolean reqsBlockBelow, 
			boolean ignoreLight, float height, float width, float length) {

		//Can it spawn in liquids?
		boolean spawnInWater = true;

		//Actual location
		Location spawnLoc = spawner.getLoc();

		//Amount of times tried
		int tries = 0;

		//As long as the mob has not been spawned, keep trying
		while(tries < 128) {

			tries++;
			
			//Getting a random location.
			if(!spawner.isUsingSpawnArea()) {
				spawnLoc = randomGenRad();
			} else {
				spawnLoc = randomGenArea();
			}

			//loc is the location of the spawner block
			//spawnLoc is the location being tested to spawn in
			if(!isEmpty(spawnLoc, spawnInWater)) {
				continue;
			}

			if(type.getType().equals(EntityType.SQUID) && 
					!(spawnLoc.getBlock().getType().equals(Material.STATIONARY_WATER) ||
							spawnLoc.getBlock().getType().equals(Material.WATER))) {
				continue;
			}

			if(!areaEmpty(spawnLoc, spawnInWater, height, width, length)) {
				continue;
			}

			//Block below
			if(reqsBlockBelow) {
				Location blockBelow = new Location(spawnLoc.getWorld(), 
						spawnLoc.getX(), spawnLoc.getY() - 1, spawnLoc.getZ());

				if(isEmpty(blockBelow, false)) {
					continue;
				}
			}

			//Light leveling
			
			if(!ignoreLight) {
				if(!((spawnLoc.getBlock().getLightLevel() <= spawner.getMaxLightLevel()) && 
						(spawnLoc.getBlock().getLightLevel() >= spawner.getMinLightLevel())))
					continue;
			}

			return spawnLoc;
		}

		return null;
	}

	private boolean isEmpty(Location loc1, boolean liquidsNotSolid) {
		if(loc1.getBlock().isLiquid() && liquidsNotSolid) {
			return true;
		} else if(loc1.getBlock().isEmpty()){
			return true;
		} else if(!isSolidBlock(loc1.getBlock())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isPlayerNearby() {
		for(Player p : Bukkit.getOnlinePlayers()) {

			Location spLoc = spawner.getLoc();
			World pWorld = p.getLocation().getWorld();
			World sWorld = spLoc.getWorld();

			if(!pWorld.equals(sWorld)) {
				continue;
			}

			//Finds distance between spawner and player in 3D space.
			double distanceSquared = p.getLocation().distanceSquared(spLoc);
			double max = spawner.getMaxPlayerDistance();
			double min = spawner.getMinPlayerDistance();
			if(distanceSquared <= Math.pow(max, 2) && distanceSquared >= Math.pow(min, 2)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSolidBlock(Block block){
        return block.getTypeId() != 0 && net.minecraft.server.v1_5_R2.Block.byId[block.getTypeId()].material.isSolid();
    }
	
	private void mainSpawn(ISpawnableEntity spawnType, boolean ignoreLight) {

		//Loop to spawn until the mobs per spawn is reached
		for(int i = 0; i < spawner.getMobsPerSpawn(); i++) {
			
			if(spawner.getMobsIds().size() == spawner.getMaxMobs())
				return;

			Location spLoc = spawner.getLoc();
			
			if(!spLoc.getChunk().isLoaded())
				return;

			Entity e;

			if(spawnType.hasAllDimensions()) {
				Location spawnLocation = getSpawningLocation(spawnType, spawnType.requiresBlockBelow(), ignoreLight,
						spawnType.getHeight(), spawnType.getWidth(), spawnType.getLength());

				if(spawnLocation == null)
					continue;

				if(!spawnLocation.getChunk().isLoaded())
					continue;

				e = spawnTheEntity(spawnType, spawnLocation);

//				net.minecraft.server.v1_5_R2.Entity nmEntity = ((CraftEntity) e).getHandle();
//
//				AxisAlignedBB bb = nmEntity.boundingBox;
//
//				spawnType.setHeight((float) (bb.d - bb.a));
//				spawnType.setWidth((float) (bb.e - bb.b));
//				spawnType.setLength((float) (bb.f - bb.c));
//				spawnType.setBlockBelow(getBlockBelowFromEntity(e));
			} else { //If it doesn't have dimension data

				e = spawnTheEntity(spawnType, spLoc);

				net.minecraft.server.v1_5_R2.Entity nmEntity = ((CraftEntity) e).getHandle();

				spawnType.setHeight(nmEntity.height);
				spawnType.setWidth(nmEntity.width);
				spawnType.setLength(nmEntity.length);
				spawnType.setBlockBelow(getBlockBelowFromEntity(e));

				Location spawnLocation = getSpawningLocation(spawnType, getBlockBelowFromEntity(e),  ignoreLight,
						nmEntity.height, nmEntity.width, nmEntity.length);

				if(spawnLocation == null)
					continue;
				
				if(!spawnLocation.getChunk().isLoaded())
					continue;

				e.teleport(spawnLocation);
			}

			if(e != null) {
				
				assignMobProps(e, spawnType);
				spawner.addMob(e.getUniqueId(), spawnType);

			}

		}

	}
	
	private Skeleton makeJockey(Spider spider, ISpawnableEntity data) {
		spider.setPassenger(null);
		Location spiderLoc = spider.getLocation();
		LivingEntity skele = (LivingEntity) spiderLoc.getWorld().spawn(spiderLoc, EntityType.SKELETON.getEntityClass());
		assignMobProps(skele, data);
		setInventory(skele, data.getInventory());
		spider.setPassenger(skele);
		spawner.addSecondaryMob(skele.getUniqueId(), spider.getUniqueId());
		return (Skeleton) skele;
	}

	private NBTTagList makePotion(PotionEffect effect) {
		NBTTagList list = new NBTTagList();
		NBTTagCompound potionType = new NBTTagCompound();
		potionType.setByte("Id", (byte) effect.getType().getId());
		potionType.setByte("Amplifier", (byte) effect.getAmplifier());
		potionType.setInt("Duration", effect.getDuration());
		potionType.setByte("Ambient", (byte) 0);
		list.add(potionType);
		return list;
	}

	private Location randomGenArea() {
		Location[] areaPoints = spawner.getAreaPoints();
		double x, y, z;
		World w = areaPoints[0].getWorld();
		x = randomGenRange(areaPoints[0].getX(), areaPoints[1].getX());
		y = randomGenRange(areaPoints[0].getY(), areaPoints[1].getY());
		z = randomGenRange(areaPoints[0].getZ(), areaPoints[1].getZ());
		
		return new Location(w, x, y, z);
	}	
	
	private Location randomGenRad() {
		double x, y, z;
		double sX, sY, sZ;
		double r = spawner.getRadius();
		Location sL = spawner.getLoc();
		sX = sL.getX();
		sY = sL.getY();
		sZ = sL.getZ();
		do {
			x = randomGenRange(sX - r, sX + r);
			y = randomGenRange(sY - r, sY + r);
			z = randomGenRange(sZ - r, sZ + r);
		} while(sL.distance(new Location(sL.getWorld(), x, y, z)) > r);
		
		return new Location(sL.getWorld(), x, y, z);
	}
	
	private double randomGenRange(double arg0, double arg1) {
		double range = (arg0 < arg1) ? arg1 - arg0 : arg0 - arg1;
		if(range < 1)
			return Math.floor(arg0) + 0.5d;
		double min = (arg0 < arg1) ? arg0 : arg1;
		return Math.floor(min + (Math.random() * range)) + 0.5d;
	}

	private double randomRotation() {
		return Math.random() * 360.0d;
	}
	
	private void setAgeProps(Ageable a, ISpawnableEntity data) {
		if(data.getAge() == -2) {
			a.setBaby();
		} else if(data.getAge() == -1) {
			a.setAdult();
		} else {
			a.setAge(data.getAge());
		}
	}

	private void setBasicProps(LivingEntity entity, ISpawnableEntity data) {

		setInventory(entity, data.getInventory()); 

		entity.addPotionEffects(data.getEffectsBukkit());

		//Health handling
		if(data.getHealth() == -2) {
			entity.setHealth(1);
		} else if(data.getHealth() == -1) {
			entity.setHealth(entity.getMaxHealth());
		} else {
			if(data.getHealth() > entity.getMaxHealth()) {
				entity.setMaxHealth(data.getHealth());
				entity.setHealth(data.getHealth());
			} else if(data.getHealth() < 0) {
				entity.setHealth(0);
			} else {
				entity.setHealth(data.getHealth());
			}
		}

		//Air handling
		if(data.getAir() == -2) {
			entity.setRemainingAir(0);
		} else if(data.getAir() == -1) {
			entity.setRemainingAir(entity.getMaximumAir());
		} else {
			entity.setRemainingAir(data.getAir());
		}

	}

	private void setDefaultInventory(LivingEntity e) {
		EntityEquipment ee = e.getEquipment();

		if(ee == null) {
			return;
		}
		
		switch(e.getType()) {
		case SKELETON:
			Skeleton sk = ((Skeleton) e);
			if(sk.getSkeletonType().getId() == SkeletonType.WITHER.getId()) {
				ee.setItemInHand(new ItemStack(Material.STONE_SWORD));
			} else {
				ee.setItemInHand(new ItemStack(Material.BOW));
			}
			break;
		case PIG_ZOMBIE:
			ee.setItemInHand(new ItemStack(Material.GOLD_SWORD));
			break;
		default:
			break;
		} 
		
	}
	
	private void setExplosiveProps(Explosive e, ISpawnableEntity data) {
		e.setYield(data.getYield());
		e.setIsIncendiary(data.isIncendiary());
	}

	private void setInventory(LivingEntity entity, ISInventory data) {

		EntityEquipment ee = entity.getEquipment();

		if(ee == null) {
			return;
		}
		
		if(!data.hasHand()) {
			setDefaultInventory(entity);
		} else {
			ee.setItemInHand(data.getHand());
		}
		
		ee.setArmorContents(data.getArmor());  
		
		List<ISItemStack> array = data.getMainInventoryISItemStacks();
		ee.setItemInHandDropChance(array.get(0).getDropChance() / 100.0f);
		ee.setBootsDropChance(array.get(1).getDropChance() / 100.0f);
		ee.setLeggingsDropChance(array.get(2).getDropChance() / 100.0f);
		ee.setChestplateDropChance(array.get(3).getDropChance() / 100.0f);
		ee.setHelmetDropChance(array.get(4).getDropChance() / 100.0f);

	}

	//Sets unimplemented data
	//TODO Test how well this works. i.e. does it spawn mobs before the data set?
	private void setNBT(Entity entity, ISpawnableEntity data) {
		Converter nbt = new Converter();
		NBTTagCompound nbtComp = nbt.getEntityNBT(entity);
		
		if(entity instanceof Creeper) {
			byte rad = (byte) Math.round(data.getYield());
			nbtComp.setByte("ExplosionRadius", rad);
			nbtComp.setShort("Fuse", (short) data.getFuseTicks());
		}

		if(entity instanceof Ghast) {
			int rad = Math.round(data.getYield());
			nbtComp.setInt("ExplosionPower", rad);
		}
		
		if(entity instanceof Minecart) {
			
			EntityMinecartAbstract abs = ((CraftMinecart) entity).getHandle();
			
			if(!data.getItemType().getType().equals(Material.AIR)) { //XXX They float because minecraft is a weaboo
				int id = data.getItemType().getTypeId();
				int dur = (int) data.getItemType().getDurability();
				abs.a(true); //Show tile
				abs.k(id); //Tile ID
				abs.l(dur); //Tile durability
			}
			
			if(data.getSpawnerData() != null && entity instanceof SpawnerMinecart) {
				EntityMinecartMobSpawner cart = (EntityMinecartMobSpawner) abs;
				
				NBTTagCompound spawnerData = new NBTTagCompound();
				NBTBase[] props = nbt.getPropertyArray(data.getSpawnerData());
				for(NBTBase base : props) {
					if(base.getName().equals("id") || base.getName().equals("x") || 
							base.getName().equals("y") || base.getName().equals("z")) {
						continue;
					}
					nbtComp.set(base.getName(), base);
					spawnerData.set(base.getName(), base);
				}
				
				try {
					Field theSpawner = cart.getClass().getDeclaredField("a");
					theSpawner.setAccessible(true);
					MobSpawnerAbstract spawner = (MobSpawnerAbstract) theSpawner.get(cart);
					spawner.a(spawnerData);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			
			return;
			
		} 
		
		if(entity instanceof FallingBlock) {
			if(data.getSpawnerData() != null) {
				EntityFallingBlock block = ((CraftFallingSand) entity).getHandle();
				nbtComp.setCompound("TileEntityData", nbt.getSpawnerNBT((ISpawner) data.getSpawnerData()));
				try {
					Class<?> entityClass = block.getClass();
					Method[] methods = entityClass.getDeclaredMethods();
					for (Method method : methods) {
						if ((method.getName() == "a")
								&& (method.getParameterTypes().length == 1)
								&& (method.getParameterTypes()[0] == NBTTagCompound.class)) {
							method.setAccessible(true);
							method.invoke(block, nbtComp);
						}
					}	
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		nbt.setEntityNBT(entity, nbtComp);
		
	}

	private Entity spawnTheEntity(ISpawnableEntity spawnType, Location spawnLocation) {
		
		Entity e;
		
		if(spawnType.getType().equals(EntityType.DROPPED_ITEM)) {
			return spawnLocation.getWorld().dropItem(spawnLocation, spawnType.getItemType());
		} else if(spawnType.getType().equals(EntityType.FALLING_BLOCK)) {
			return spawnLocation.getWorld().spawnFallingBlock(spawnLocation, spawnType.getItemType().getType(), (byte) spawnType.getItemType().getDurability());
		} else if(spawnType.getType().equals(EntityType.SPLASH_POTION)) {
			World world = spawnLocation.getWorld();
			PotionEffect effect = spawnType.getPotionEffectBukkit();
			PotionType type = PotionType.getByEffect(effect.getType());
			Potion p = new Potion(type);
			int data = p.toDamageValue();

			net.minecraft.server.v1_5_R2.World nmsWorld = ((CraftWorld) world).getHandle();
			EntityPotion ent = new EntityPotion(nmsWorld, spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), new net.minecraft.server.v1_5_R2.ItemStack(373, 1, data));
			NBTTagCompound nbt = new NBTTagCompound();

			ent.b(nbt); //Gets all the normal tags
			NBTTagCompound potionTag = nbt.getCompound("Potion");
			NBTTagCompound tagTag = new NBTTagCompound();
			if(potionTag == null) {
				potionTag = new NBTTagCompound();
				potionTag.setShort("id", (short) 373);
				potionTag.setShort("Damage", (short) data);
				potionTag.setByte("Count", (byte) 1);
				tagTag.set("CustomPotionEffects", makePotion(effect));
			} else {
				tagTag = potionTag.getCompound("tag");
				tagTag.set("CustomPotionEffects", makePotion(effect));
			}

			potionTag.setCompound("tag", tagTag);
			nbt.setCompound("Potion", potionTag);
			ent.a(nbt);

			nmsWorld.addEntity(ent);
			return ent.getBukkitEntity();
		} else if(spawnType.getType().equals(EntityType.ENDER_PEARL)) { //TODO Make sure this works when >1 player
			World world = spawnLocation.getWorld();
			List<Player> nearby = getNearbyPlayers(spawnLocation, spawner.getMaxPlayerDistance() + 1);
			int index = (new Random()).nextInt(nearby.size());
			EntityLiving nearPlayer = ((CraftLivingEntity) nearby.get(index)).getHandle();
			net.minecraft.server.v1_5_R2.World nmsWorld = ((CraftWorld) world).getHandle();
			EntityEnderPearl ent = new EntityEnderPearl(nmsWorld, nearPlayer);
			ent.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), 0, 0);
			nmsWorld.addEntity(ent);
			return ent.getBukkitEntity();
		} else if(spawnType.getType().equals(EntityType.LIGHTNING)) {
			return spawnLocation.getWorld().strikeLightningEffect(spawnLocation);
		} else if(spawnType.getType().equals(EntityType.MINECART)) { 
			//XXX Keep an eye on here for new minecart stuff.
			if(spawnType.getItemType().getType().equals(Material.MOB_SPAWNER)) {
				e = spawnLocation.getWorld().spawn(spawnLocation, SpawnerMinecart.class);
			} else if(spawnType.getItemType().getType().equals(Material.TNT)) {
				e = spawnLocation.getWorld().spawn(spawnLocation, ExplosiveMinecart.class);
			} else if(spawnType.getItemType().getTypeId() == 154) {
				e = spawnLocation.getWorld().spawn(spawnLocation, HopperMinecart.class);
			} else if(spawnType.getItemType().getType().equals(Material.CHEST)) {
				e = spawnLocation.getWorld().spawn(spawnLocation, StorageMinecart.class);
			} else if(spawnType.getItemType().getType().equals(Material.FURNACE)) {
				e = spawnLocation.getWorld().spawn(spawnLocation, PoweredMinecart.class);
			} else if(spawnType.getItemType().getType().equals(Material.DISPENSER)) {
				e = spawnLocation.getWorld().spawn(spawnLocation, Minecart.class);
			} else {
				e = spawnLocation.getWorld().spawn(spawnLocation, Minecart.class);
			}
		} else {
			e = spawnLocation.getWorld().spawn(spawnLocation, spawnType.getType().getEntityClass());;
		}
		
		net.minecraft.server.v1_5_R2.Entity entity0 = ((CraftEntity) e).getHandle();
		entity0.yaw = (float) randomRotation(); //TODO For Rotation
		
		return e;

	}

}
