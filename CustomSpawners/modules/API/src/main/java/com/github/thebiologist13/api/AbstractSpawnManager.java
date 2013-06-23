package com.github.thebiologist13.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.herocraftonline.heroes.Heroes;

public abstract class AbstractSpawnManager implements ISpawnManager {

	protected ISpawner spawner;
	
	private Heroes heroes = null;
	
	public AbstractSpawnManager(ISpawner spawner) {
		this.spawner = spawner;
		heroes = getHeroes();
	}

	public Entity addRider(Entity vehicle, ISpawnableEntity riderData) {
		Location vehicleLoc = vehicle.getLocation();
		Entity rider = spawnMobAt(riderData, vehicleLoc);
		vehicle.setPassenger(rider);
		spawner.addSecondaryMob(rider.getUniqueId(), vehicle.getUniqueId());
		return rider;
	}

	public boolean areaEmpty(Location loc, boolean spawnInWater, float height, float width, float length) {
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
	public void assignMobProps(Entity baseEntity, ISpawnableEntity data) {

		//This needs to be before everything else!
		if(data.getRider() != null) {
			addRider(baseEntity, data.getRider());
		}
		
		baseEntity.setVelocity(data.getVelocity(baseEntity).clone());
		baseEntity.setFireTicks(data.getFireTicks(baseEntity));
		
		if(baseEntity instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) baseEntity;
			setBasicProps(entity, data);

			if(data.showCustomName()) {
				setCustomName(entity, data);
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
				v.setAge(data.getAge(baseEntity));
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
					p.setBaby((data.getAge(baseEntity) < -1) ? true : false);
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
					z.setBaby((data.getAge(baseEntity) < -1) ? true : false);
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
				f.setDirection(data.getVelocity(baseEntity));
			} else if(pro instanceof SmallFireball) {
				SmallFireball f = (SmallFireball) pro;
				setExplosiveProps(f, data);
				f.setVelocity(new Vector(0, 0, 0));
				f.setDirection(data.getVelocity(baseEntity));
			}

		} else if(baseEntity instanceof Explosive) {

			Explosive ex = (Explosive) baseEntity;

			if(ex instanceof TNTPrimed) {
				TNTPrimed tnt = (TNTPrimed) ex;
				setExplosiveProps(tnt, data);
				tnt.setFuseTicks(data.getFuseTicks(baseEntity));
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

		} else if(baseEntity instanceof ExperienceOrb) {
			ExperienceOrb o = (ExperienceOrb) baseEntity;
			o.setExperience(data.getDroppedExp(baseEntity));
		}
		
		setNBT(baseEntity, data);
		
	}

	@Override
	public abstract void forceSpawn();
	
	@Override
	public abstract void forceSpawn(ISpawnableEntity entity);
	
	public boolean getBlockBelowFromEntity(Entity e) {

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

	public byte getLight() {
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

	public abstract void setCustomName(LivingEntity entity, ISpawnableEntity data);
	
	public ArrayList<Player> getNearbyPlayers(Location source, double max) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			
			if(!p.getWorld().equals(source.getWorld()))
				continue;
			
			double distanceSquared = p.getLocation().distanceSquared(source);
			if (distanceSquared <= Math.pow((double) max, 2)) {
				players.add(p);
			}
		}
		return players;
	}

	public Heroes getHeroes() {
		
		if(heroes != null)
			return heroes;
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Heroes");
		
		if(plugin == null && !(plugin instanceof Heroes))
			return null;
		
		return (Heroes) plugin;
	}
	
	public Location getSpawningLocation(ISpawnableEntity type, boolean reqsBlockBelow, 
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

	public boolean isEmpty(Location loc1, boolean liquidsNotSolid) {
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

	public abstract boolean isSolidBlock(Block b);
	
	public boolean isPlayerNearby() {
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
	
	public abstract void mainSpawn(ISpawnableEntity spawnType, boolean ignoreLight);
	
	public Skeleton makeJockey(Spider spider, ISpawnableEntity data) {
		spider.setPassenger(null);
		Location spiderLoc = spider.getLocation();
		LivingEntity skele = (LivingEntity) spiderLoc.getWorld().spawn(spiderLoc, EntityType.SKELETON.getEntityClass());
		assignMobProps(skele, data);
		setInventory(skele, data.getInventory());
		spider.setPassenger(skele);
		spawner.addSecondaryMob(skele.getUniqueId(), spider.getUniqueId());
		return (Skeleton) skele;
	}

	public Location randomGenArea() {
		Location[] areaPoints = spawner.getAreaPoints();
		double x, y, z;
		World w = areaPoints[0].getWorld();
		x = randomGenRange(areaPoints[0].getX(), areaPoints[1].getX());
		y = randomGenRange(areaPoints[0].getY(), areaPoints[1].getY());
		z = randomGenRange(areaPoints[0].getZ(), areaPoints[1].getZ());
		
		return new Location(w, x, y, z);
	}

	public Location randomGenRad() {
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
	
	public double randomGenRange(double arg0, double arg1) {
		double range = (arg0 < arg1) ? arg1 - arg0 : arg0 - arg1;
		if(range < 1)
			return Math.floor(arg0) + 0.5d;
		double min = (arg0 < arg1) ? arg0 : arg1;
		return Math.floor(min + (Math.random() * range)) + 0.5d;
	}

	public double randomRotation() {
		return Math.random() * 360.0d;
	}	
	
	public void setAgeProps(Ageable a, ISpawnableEntity data) {
		if(data.getAge(a) == -2) {
			a.setBaby();
		} else if(data.getAge(a) == -1) {
			a.setAdult();
		} else {
			a.setAge(data.getAge(a));
		}
	}
	
	public void setBasicProps(LivingEntity entity, ISpawnableEntity data) {

		setInventory(entity, data.getInventory()); 

		entity.addPotionEffects(data.getEffectsBukkit());
		
		data.setMaxHealth(entity.getMaxHealth());
		data.setMaxAir(entity.getMaximumAir());

		//Health handling
		if(data.getHealth(entity) == -2) {
			entity.setHealth(1);
		} else if(data.getHealth(entity) == -1) {
			entity.setHealth(entity.getMaxHealth());
		} else {
			if(data.getHealth(entity) > entity.getMaxHealth()) {
				entity.setMaxHealth(data.getHealth(entity));
				entity.setHealth(data.getHealth(entity));
			} else if(data.getHealth(entity) < 0) {
				entity.setHealth(0);
			} else {
				entity.setHealth(data.getHealth(entity));
			}
		}

		//Air handling
		if(data.getAir(entity) == -2) {
			entity.setRemainingAir(0);
		} else if(data.getAir(entity) == -1) {
			entity.setRemainingAir(entity.getMaximumAir());
		} else {
			entity.setRemainingAir(data.getAir(entity));
		}

	}

	public void setDefaultInventory(LivingEntity e) {
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
	
	public void setExplosiveProps(Explosive e, ISpawnableEntity data) {
		e.setYield(data.getYield(e));
		e.setIsIncendiary(data.isIncendiary());
	}

	public void setInventory(LivingEntity entity, ISInventory data) {

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

	public abstract void setNBT(Entity entity, ISpawnableEntity data);
	
	@Override
	public abstract void spawn();

	@Override
	public abstract Entity spawnMobAt(ISpawnableEntity entity, Location location);
	
	public abstract Entity spawnTheEntity(ISpawnableEntity spawnType, Location spawnLocation);
	
}
