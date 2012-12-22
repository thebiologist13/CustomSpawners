package com.github.thebiologist13;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Golem;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Spider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WaterMob;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.github.thebiologist13.listeners.DamageController;
import com.github.thebiologist13.serialization.SBlock;
import com.github.thebiologist13.serialization.SInventory;
import com.github.thebiologist13.serialization.SLocation;
import com.github.thebiologist13.serialization.SPotionEffect;

public class Spawner implements Serializable {
	
	private static final long serialVersionUID = -153105685313343476L;
	//Main Data
	private Map<String, Object> data = new HashMap<String, Object>();
	//Spawnable Mobs
	private List<Integer> typeData = new ArrayList<Integer>();
	//Integer is mob ID. This holds the entities that have been spawned so when one dies, it can be removed from maxMobs.
	private Map<Integer, SpawnableEntity> mobs = new HashMap<Integer, SpawnableEntity>(); 
	
	//Ticks left before next spawn
	private int ticksLeft = -1;
	
	/*
	 * Constructors
	 */
	
	public Spawner(SpawnableEntity type, Location loc, int id) {
		init(type, loc, id);
	}
	
	public Spawner(SpawnableEntity type, Location loc, String name, int id) {
		init(type, loc, id);
		data.put("name", name);
	}
	
	private void init(SpawnableEntity type, Location loc, int id) {
		SLocation[] areaPoints = new SLocation[2];
		
		areaPoints[0] = new SLocation(loc);
		areaPoints[1] = new SLocation(loc);
		
		data.put("id", id);
		data.put("loc", new SLocation(loc));
		data.put("block", new SBlock(loc.getBlock()));
		data.put("areaPoints", areaPoints);
		typeData.add(type.getId());
		data.put("converted", false);
		data.put("mainEntity", type);
		data.put("useSpawnArea", false);
	}
	
	public int getId() {
		return (Integer) this.data.get("id");
	}
	
	public String getName() {
		return (this.data.containsKey("name")) ? (String) this.data.get("name") : "";
	}

	public void setName(String name) {
		this.data.put("name", name);
	}

	public List<Integer> getTypeData() {
		return this.typeData;
	}

	public void setTypeData(List<Integer> typeDataParam) {
		
		if(typeDataParam == null) {
			return;
		}
		
		this.typeData = typeDataParam;
	}
	
	public void addTypeData(SpawnableEntity data) {
		typeData.add(data.getId());
	}
	
	public void removeTypeData(SpawnableEntity type) {
		if(typeData.contains(type.getId())) {
			typeData.remove(type.getId());
		}
	}
	
	public SpawnableEntity getMainEntity() {
		return (SpawnableEntity) this.data.get("mainEntity");
	}
	
	public boolean isActive() {
		return (this.data.containsKey("active")) ? (Boolean) this.data.get("active") : false;
	}

	public void setActive(boolean active) {
		this.data.put("active", active);
	}

	public boolean isHidden() {
		return (this.data.containsKey("hidden")) ? (Boolean) this.data.get("hidden") : false;
	}

	public void setHidden(boolean hidden) {
		this.data.put("hidden", hidden);
	}

	public double getRadius() {
		return (this.data.containsKey("radius")) ? (Double) this.data.get("radius") : 0d;
	}

	public void setRadius(double radius) {
		this.data.put("radius", radius);
	}

	public boolean isUsingSpawnArea() {
		return (this.data.containsKey("useSpawnArea")) ? (Boolean) this.data.get("useSpawnArea") : false;
	}

	public void setUseSpawnArea(boolean useSpawnArea) {
		this.data.put("useSpawnArea", useSpawnArea);
	}

	public boolean isRedstoneTriggered() {
		return (this.data.containsKey("redstone")) ? (Boolean) this.data.get("redstone") : false;
	}

	public void setRedstoneTriggered(boolean redstoneTriggered) {
		this.data.put("redstone", redstoneTriggered);
	}

	public int getMaxPlayerDistance() {
		return (this.data.containsKey("maxDistance")) ? (Integer) this.data.get("maxDistance") : 0;
	}

	public void setMaxPlayerDistance(int maxPlayerDistance) {
		this.data.put("maxDistance", maxPlayerDistance);
	}

	public int getMinPlayerDistance() {
		return (this.data.containsKey("minDistance")) ? (Integer) this.data.get("minDistance") : 0;
	}

	public void setMinPlayerDistance(int minPlayerDistance) {
		this.data.put("minDistance", minPlayerDistance);
	}

	public byte getMaxLightLevel() {
		return (this.data.containsKey("maxLight")) ? (Byte) this.data.get("maxLight") : 0;
	}

	public void setMaxLightLevel(byte maxLightLevel) {
		this.data.put("maxLight", maxLightLevel);
	}

	public byte getMinLightLevel() {
		return (this.data.containsKey("minLight")) ? (Byte) this.data.get("minLight") : 0;
	}

	public void setMinLightLevel(byte minLightLevel) {
		this.data.put("minLight", minLightLevel);
	}

	public int getMobsPerSpawn() {
		return (this.data.containsKey("mobsPerSpawn")) ? (Integer) this.data.get("mobsPerSpawn") : 0;
	}

	public void setMobsPerSpawn(int mobsPerSpawn) {
		this.data.put("mobsPerSpawn", mobsPerSpawn);
	}

	public int getMaxMobs() {
		return (this.data.containsKey("maxMobs")) ? (Integer) this.data.get("maxMobs") : 0;
	}

	public void setMaxMobs(int maxMobs) {
		this.data.put("maxMobs", maxMobs);
	}

	public Location getLoc() {
		return ((SLocation) this.data.get("loc")).toLocation();
	}

	public void setLoc(Location loc) {
		this.data.put("loc", new SLocation(loc));
		setBlock(loc.getBlock());
	}

	public Location[] getAreaPoints() {
		
		Location[] ap1 = new Location[2];
		SLocation[] ap = ((SLocation[]) this.data.get("areaPoints"));
		
		for(int i = 0; i < ap.length; i++) {
			ap1[i] = ap[i].toLocation();
		}
		
		return ap1;
	}

	public void setAreaPoints(Location[] areaPoints) {
		
		if(areaPoints.length != 2)
			return;
		
		SLocation[] ap1 = new SLocation[2];
		
		for(int i = 0; i < areaPoints.length; i++) {
			ap1[i] = new SLocation(areaPoints[i]);
		}
		
		this.data.put("areaPoints", ap1);
	}
	
	public void changeAreaPoint(int index, Location value) {
		if(index != 0 || index != 1) {
			return;
		}
		
		SLocation[] ap1 = (SLocation[]) this.data.get("areaPoints");
		ap1[index] = new SLocation(value);
		
		this.data.put("areaPoints", ap1);
	}

	public int getRate() {
		return (this.data.containsKey("rate")) ? (Integer) this.data.get("rate") : 60;
	}

	public void setRate(int rate) {
		this.data.put("rate", rate);
		this.ticksLeft = rate;
	}

	public Map<Integer, SpawnableEntity> getMobs() {
		return this.mobs;
	}
	
	public void setMobs(Map<Integer, SpawnableEntity> mobParam) {
		this.mobs = mobParam;
	}
	
	public void addMob(int mobId, SpawnableEntity entity) {
		mobs.put(mobId, entity);
	}
	
	public void removeMob(int mobId) {
		if(mobs.containsKey(mobId))
			mobs.remove(mobId);
	}
	
	public boolean isConverted() {
		return (Boolean) this.data.get("converted");
	}

	public void setConverted(boolean converted) {
		this.data.put("converted", converted);
	}

	public Block getBlock() {
		return ((SBlock) this.data.get("block")).toBlock();
	}
	
	public void setBlock(Block block) {
		this.data.put("block", new SBlock(block));
	}
	
	public Object getProp(String key) {
		return (this.data.containsKey(key)) ? this.data.get(key) : null;
	}
	
	public void setProp(String key, Object value) {
		if(key == null || value == null) {
			return;
		}
		
		this.data.put(key, value);
	}
	
	public boolean hasProp(String key) {
		return this.data.containsKey(key);
	}
	
	public void setData(Map<String, Object> data) {
		
		if(data == null)
			return;
		
		if(this.data.containsKey("id")) {
			data.put("id", (Integer) this.data.get("id"));
		}
		
		if(this.data.containsKey("name")) {
			data.put("name", (String) this.data.get("name"));
		}
		
		this.data = data;
	}
	
	/*
	 * Methods for spawning, timing, etc.
	 */

	//Tick the spawn rate down and spawn mobs if it is time to spawn. Return the ticks left.
	public int tick() {
		if(((Boolean) data.get("active")) && !(((Integer) data.get("rate")) <= 0)) {
			ticksLeft--;
			if(ticksLeft == 0) {
				ticksLeft = ((Integer) data.get("rate"));
				spawn();
				return 0;
			}
		}
		
		return ticksLeft;
	}
	
	//Remove the spawner
	public void remove() {
		/*
		 * If the ID is -1, it should be removed next tick by not 
		 * calling these functions and removing it's file.
		 * 
		 * Won't happen initially because when the spawner is initialized, it is given an ID
		 */
		data.put("id", -1);
		data.put("active", false);
	}
	
	//Spawn the mobs
	public void spawn() {
		//Are conditions valid for spawning?
		boolean canSpawn = true;

		//If the spawner is not active, return
		if(!isActive()) {
			return;
		}

		/*
		 * This block checks if the conditions are met to spawn mobs
		 */
		if(isRedstoneTriggered() && (!getBlock().isBlockIndirectlyPowered() || !getBlock().isBlockPowered())) {
			canSpawn = false;
		} else if(!isPlayerNearby()) {
			canSpawn = false;
		} else if(!(getBlock().getLightLevel() <= getMaxLightLevel()) && 
				!(getBlock().getLightLevel() <= getMinLightLevel())) {
			canSpawn = false;
		} else if(mobs.size() >= getMaxMobs()) {
			canSpawn = false;
		}

		//If we can spawn
		if(canSpawn) {
			
			//Break the loop if the spawn limit is reached
			if(!(mobs.size() == getMaxMobs())) {
				SpawnableEntity spawnType = randType();
				mainSpawn(spawnType);
			}
			
		}
		
	}
	
	//Spawn the mobs
	public void forceSpawn() {

		SpawnableEntity spawnType = randType();
		mainSpawn(spawnType);
		
	}
	
	//Spawn the mobs
	public void forceSpawnType(SpawnableEntity entity) {

		mainSpawn(entity);
		
	}
	
	/*
	 * Methods for choosing locations, checking things, etc.
	 */
	
	private void mainSpawn(SpawnableEntity spawnType) {
		
		//Loop to spawn until the mobs per spawn is reached
		for(int i = 0; i < getMobsPerSpawn(); i++) {
			
			if(mobs.size() + i >= getMaxMobs()) {
				return;
			}
			
			Location spLoc = getLoc();
			spLoc.setYaw(randRot());
			
			Entity e;
			
			if(spawnType.hasAllDimensions()) {
				Location spawnLocation = getSpawningLocation(spawnType, spawnType.requiresBlockBelow(), 
						spawnType.getHeight(), spawnType.getWidth(), spawnType.getLength());
				e = spawnTheEntity(spawnType, spawnLocation);
			} else {
				e = spawnTheEntity(spawnType, spLoc);
				
				net.minecraft.server.v1_4_5.Entity nmEntity = ((CraftEntity) e).getHandle();
				
				int id = spawnType.getId();
				CustomSpawners.entities.get(id).setHeight(nmEntity.height);
				CustomSpawners.entities.get(id).setWidth(nmEntity.width);
				CustomSpawners.entities.get(id).setLength(nmEntity.length);
				CustomSpawners.entities.get(id).setBlockBelow(getBlockBelowFromEntity(e));
				
				Location spawnLocation = getSpawningLocation(spawnType, getBlockBelowFromEntity(e), nmEntity.height, nmEntity.width, nmEntity.length);
				e.teleport(spawnLocation);
			}
			
			if(e != null) {
				
				assignMobProps(e, spawnType);
				
				mobs.put(e.getEntityId(), spawnType);
				
			}
			
		}
		
	}
	
	//Check if players are nearby
	private boolean isPlayerNearby() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			//Finds distance between spawner and player in 3D space.
			double distance = p.getLocation().distance(getLoc());
			if(distance <= ((Integer) data.get("maxDistance")) && distance >= ((Integer) data.get("minDistance"))) {
				return true;
			}
		}
		return false;
	}
	
	//Check if players are nearby
	private ArrayList<Player> getNearbyPlayers(Location source, double max) {
		ArrayList<Player> players = new ArrayList<Player>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			//Finds distance between spawner and player is 3D space.
			double distance = p.getLocation().distance(getLoc());
			
			if(distance <= max) {
				players.add(p);
			}
		}
		return players;
	}
	
	//Generate random double for location's parts within radius
	private double randomGenRad() {
		Random rand = new Random();
		if(rand.nextFloat() < 0.5) {
			return Math.floor((rand.nextDouble() * ((Double) data.get("radius"))) * -1) - 0.5;
		} else {
			return Math.floor((rand.nextDouble() * ((Double) data.get("radius"))) * 1) + 0.5;
		}
	}
	
	private double randomGenRange(double arg0, double arg1) {
		Random rand = new Random();
		double difference = Math.abs(arg0 - arg1);
		if(arg0 < arg1) {
			return arg0 + (difference * rand.nextDouble());
		} else if(arg1 < arg0) {
			return arg1 + (difference * rand.nextDouble());
		} else {
			return arg0;
		}
	}
	
	private SpawnableEntity randType() {
		Random rand = new Random();
		int index = rand.nextInt(typeData.size());
		int count = 0;
		
		for(Integer i : typeData) {
			if(count == index) {
				return CustomSpawners.getEntity(i.toString());
			} else {
				count++;
			}
		}
		
		return null;
	}
	
	private float randRot() {
		Random rand = new Random();
		return rand.nextFloat() * 360.0f;
	}
	
	//Assigns properties to a LivingEntity
	private void assignMobProps(Entity baseEntity, SpawnableEntity data) {
		
		baseEntity.setVelocity(data.getVelocity().clone());
		baseEntity.setFireTicks(data.getFireTicks());
		
		if(baseEntity instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) baseEntity;
			data.setMaxHealth(entity.getMaxHealth());
			data.setMaxAir(entity.getMaximumAir());
			setBasicProps(entity, data);
			
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
				} else if(monster instanceof Spider) {
					Spider s = (Spider) monster;
					if(data.isJockey()) {
						Skeleton skele = makeJockey(s);
						setBasicProps(skele, data);
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
			
			if(pro instanceof EnderPearl) {
				EnderPearl e = (EnderPearl) pro;
				ArrayList<Player> players = getNearbyPlayers(e.getLocation(), 16);
				int index = (int) Math.round(randomGenRange(0, players.size()));
				e.setShooter(players.get(index));
			} else if(pro instanceof Fireball) {
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
			
		}
		
	}
	
	//Makes a spider jockey
	private Skeleton makeJockey(Spider spider) {
		Location spiderLoc = spider.getLocation();
		Entity skele = spiderLoc.getWorld().spawnEntity(spiderLoc, EntityType.SKELETON);
		spider.setPassenger(skele);
		return (Skeleton) skele;
	}
	
	//Assigns some basic props
	private void setBasicProps(LivingEntity entity, SpawnableEntity data) {
		
		setInventory(entity, data.getInventory()); 
		
		for(SPotionEffect p : data.getEffects()) {
			PotionEffect ef = new PotionEffect(p.getType(), p.getDuration(), p.getAmplifier());
			entity.addPotionEffect(ef, true);
		}
		
		//Health handling
		if(data.getHealth() == -2) {
			entity.setHealth(1);
		} else if(data.getHealth() == -1) {
			entity.setHealth(entity.getMaxHealth());
		} else {
			if(data.getHealth() > entity.getMaxHealth()) {
				entity.setHealth(entity.getMaxHealth());
				DamageController.extraHealthEntities.put(entity.getEntityId(), data.getHealth() - entity.getMaxHealth());
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
	
	//Inventory stuff
	private void setInventory(LivingEntity entity, SInventory data) {
		
		EntityEquipment ee = entity.getEquipment();
		
		switch(entity.getType()) {
		case SKELETON:
			ee.setItemInHand(new ItemStack(Material.BOW));
			break;
		case PIG_ZOMBIE:
			ee.setItemInHand(new ItemStack(Material.GOLD_SWORD));
			break;
		} 
		
		ee.setItemInHand(data.getHand());
		ee.setArmorContents(data.getArmor());
		
	}
	
	//Explosive Properties
	private void setExplosiveProps(Explosive e, SpawnableEntity data) {
		e.setYield(data.getYield());
		e.setIsIncendiary(data.isIncendiary());
	}
	
	//Age properties
	private void setAgeProps(Ageable a, SpawnableEntity data) {
		if(data.getAge() == -2) {
			a.setBaby();
		} else if(data.getAge() == -1) {
			a.setAdult();
		} else {
			a.setAge(data.getAge());
		}
		
	}
	
	//Determines a valid spawn location
	private Location getSpawningLocation(SpawnableEntity type, boolean reqsBlockBelow, float height, float width, float length) {
		//Random locations
		double randX = 0;
		double randY = 0;
		double randZ = 0;
		
		//Can it spawn in liquids?
		boolean spawnInWater = true;
		
		//Actual location
		Location spawnLoc = null;
		
		//Amount of times tried
		int tries = 0;
		
		//As long as the mob has not been spawned, keep trying
		while(tries < 128) {

			tries++;
			Location loc = getLoc();
			Location[] areaPoints = getAreaPoints();
			
			//Getting a random location.
			if(!isUsingSpawnArea()) {
				//Generates a random location using randomGenRad(), a location for the block above, and a location for the block below.
				randX = randomGenRad()  + loc.getBlockX();
				randY = Math.round(randomGenRad()) + loc.getBlockY();
				randZ = randomGenRad() + loc.getBlockZ();

			} else {
				//Generates a random location in the spawn area using randomGenRange().
				randX = randomGenRange(areaPoints[0].getX(), areaPoints[1].getX());
				randY = randomGenRange(areaPoints[0].getY(), areaPoints[1].getY());
				randZ = randomGenRange(areaPoints[0].getZ(), areaPoints[1].getZ());
			}

			spawnLoc = new Location(loc.getWorld(), randX, randY + 1, randZ);
			spawnLoc.setYaw(randRot());
			
			if(!isEmpty(spawnLoc, spawnInWater)) {
				continue;
			}
			
			if(!areaEmpty(loc, spawnInWater, height, width, length))
				continue;
			
			if(reqsBlockBelow) {
				Location blockBelow = new Location(loc.getWorld(), randX, randY - 1, randZ);
				
				if(isEmpty(blockBelow, !spawnInWater)) {
					continue;
				}
			}
			
			if(!((getBlock().getLightLevel() <= getMaxLightLevel()) && (getBlock().getLightLevel() >= getMinLightLevel())))
				continue;
			
			return spawnLoc;
		}
		
		return null;
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
	
	private boolean areaEmpty(Location loc, boolean spawnInWater, float height, float width, float length) {
		
		for(int y = 0; y < height; y++) {
			double testY = loc.getY() + y;
			
			for(int x = (int) (loc.getX() - (width/2)); x < width; x++) {
				double testX = loc.getX() + x;
				
				for(int z = (int) (loc.getZ() - (length/2)); z < length; z++) {
					double testZ = loc.getZ() + z;
					Location testLoc = new Location(loc.getWorld(), testX, testY, testZ);
					
					if(!isEmpty(testLoc, spawnInWater))
						return false;
					
				}
				
			}
			
		}
		
		return true;
		
	}
	
	//Checks if a block is "empty"
	private boolean isEmpty(Location loc1, boolean liquidsNotSolid) {
		if(loc1.getBlock().isLiquid() && liquidsNotSolid) {
			return true;
		} else if(loc1.getBlock().isEmpty()){
			return true;
		} else {
			return false;
		}
		
	}
	
	//Spawns the actual entity
	private Entity spawnTheEntity(SpawnableEntity spawnType, Location spawnLocation) {
		if(spawnType.getType().equals(EntityType.DROPPED_ITEM)) {
			return getLoc().getWorld().dropItemNaturally(spawnLocation, spawnType.getItemType());
		} else if(spawnType.getType().equals(EntityType.FALLING_BLOCK)) {
			return getLoc().getWorld().spawnFallingBlock(spawnLocation, spawnType.getItemType().getType(), (byte) 0);
		} else {
			return getLoc().getWorld().spawn(spawnLocation, spawnType.getType().getEntityClass());
		}
		
	}
	
}
