package com.github.thebiologist13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.github.thebiologist13.listeners.DamageController;

public class Spawner {
	
	//TODO Switch to NBT
	private NBTTagCompound data = new NBTTagCompound();
	
	//Identification variables
	private String name = "";
	private int id = -1; //Identification # for spawner
	
	//TODO Make sure this is compatible with all files
	private HashMap<Integer, SpawnableEntity> typeData = new HashMap<Integer, SpawnableEntity>(); //SpawnableEntities in format SpawnableEntity ID #, SpawnableEntity
	
	//Spawner Properties
	private boolean active = false; //Is this spawner active?
	private boolean hidden = false; //Is the spawner hidden?
	private double radius = 0; //Radius that it can spawn in
	private boolean useSpawnArea = false; //Should it use a separate spawn area?
	private boolean redstoneTriggered = false; //Does it need to be redstone triggered to spawn?
	private int maxPlayerDistance = -1; //Maximum distance a player can be from it to spawn mobs
	private int minPlayerDistance = -1; //Minimum distance
	private byte maxLightLevel = -1; //Maximum light level the spawner can spawn entities at
	private byte minLightLevel = -1; //Minimum light level
	private int mobsPerSpawn = 0; //Amount of mobs to spawn per time
	private int maxMobs = -1; //Maximum amount of mobs it will spawn
	
	//Location variables
	private Location loc = null; //Location of the spawner
	private Location[] areaPoints = new Location[2]; //Points for a spawn area.
	
	//Block Type
	private Block block = null;
	
	//Spawning rates/timing
	private int ticksLeft = -1; //Ticks left before next spawn
	private int rate = -1; //Rate in how many ticks to spawn at
	
	//List of mobs
	//Integer is mob ID. This holds the entities that have been spawned so when one dies, it can be removed from maxMobs.
	private ConcurrentHashMap<Integer, SpawnableEntity> mobs = new ConcurrentHashMap<Integer, SpawnableEntity>(); 
	//List of currently passive mobs from a spawner. When provoked, they are moved to "mobs"
	private ConcurrentHashMap<Integer, SpawnableEntity> passiveMobs = new ConcurrentHashMap<Integer, SpawnableEntity>(); 
	
	//The current state of the spawner. i.e. if it is a mob spawner block or custom spawner. True if it is a mob spawner block.
	private boolean converted = false;
	
	//Other
	private Server server = null; //Used to get online players
	
	/*
	 * Constructors
	 */
	
	public Spawner(SpawnableEntity type, Location loc, int id, Server server) {
		this.id = id;
		this.loc = loc;
		this.server = server;
		this.block = loc.getBlock();
		typeData.put(type.getId(), type);
		
		areaPoints[0] = loc;
		areaPoints[1] = loc;
	}
	
	public Spawner(SpawnableEntity type, Location loc, String name, int id, Server server) {
		this.id = id;
		this.name = name;
		this.loc = loc;
		this.server = server;
		this.block = loc.getBlock();
		typeData.put(type.getId(), type);
		
		areaPoints[0] = loc;
		areaPoints[1] = loc;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<Integer, SpawnableEntity> getTypeData() {
		return typeData;
	}

	public void setTypeData(HashMap<Integer, SpawnableEntity> typeData) {
		
		if(typeData == null) {
			return;
		}
		
		this.typeData = typeData;
	}
	
	public void addTypeData(SpawnableEntity data) {
		typeData.put(data.getId(), data);
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public boolean isUsingSpawnArea() {
		return useSpawnArea;
	}

	public void setUseSpawnArea(boolean useSpawnArea) {
		this.useSpawnArea = useSpawnArea;
	}

	public boolean isRedstoneTriggered() {
		return redstoneTriggered;
	}

	public void setRedstoneTriggered(boolean redstoneTriggered) {
		this.redstoneTriggered = redstoneTriggered;
	}

	public int getMaxPlayerDistance() {
		return maxPlayerDistance;
	}

	public void setMaxPlayerDistance(int maxPlayerDistance) {
		this.maxPlayerDistance = maxPlayerDistance;
	}

	public int getMinPlayerDistance() {
		return minPlayerDistance;
	}

	public void setMinPlayerDistance(int minPlayerDistance) {
		this.minPlayerDistance = minPlayerDistance;
	}

	public byte getMaxLightLevel() {
		return maxLightLevel;
	}

	public void setMaxLightLevel(byte maxLightLevel) {
		this.maxLightLevel = maxLightLevel;
	}

	public byte getMinLightLevel() {
		return minLightLevel;
	}

	public void setMinLightLevel(byte minLightLevel) {
		this.minLightLevel = minLightLevel;
	}

	public int getMobsPerSpawn() {
		return mobsPerSpawn;
	}

	public void setMobsPerSpawn(int mobsPerSpawn) {
		this.mobsPerSpawn = mobsPerSpawn;
	}

	public int getMaxMobs() {
		return maxMobs;
	}

	public void setMaxMobs(int maxMobs) {
		this.maxMobs = maxMobs;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
		this.block = loc.getBlock();
	}

	public Location[] getAreaPoints() {
		return areaPoints;
	}

	public void setAreaPoints(Location[] areaPoints) {
		this.areaPoints = areaPoints;
	}
	
	public void changeAreaPoint(int index, Location value) {
		if(index != 0 || index != 1) {
			return;
		}
		
		areaPoints[index] = value;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
		this.ticksLeft = rate;
	}

	public ConcurrentHashMap<Integer, SpawnableEntity> getMobs() {
		return mobs;
	}
	
	public void setMobs(ConcurrentHashMap<Integer, SpawnableEntity> mobs) {
		this.mobs = mobs;
	}
	
	public void addMob(int mobId, SpawnableEntity entity) {
		mobs.put(id, entity);
	}
	
	public void removeMob(int mobId) {
		if(mobs.containsKey(mobId))
			mobs.remove(mobId);
	}
	
	public ConcurrentHashMap<Integer, SpawnableEntity> getPassiveMobs() {
		return passiveMobs;
	}

	public void setPassiveMobs(ConcurrentHashMap<Integer, SpawnableEntity> passiveMobs) {
		this.passiveMobs = passiveMobs;
	}
	
	public void addPassiveMob(int mobId, SpawnableEntity entity) {
		passiveMobs.put(id, entity);
	}
	
	public void removePassiveMob(int mobId) {
		if(passiveMobs.containsKey(mobId))
			passiveMobs.remove(mobId);
	}
	
	public boolean isConverted() {
		return converted;
	}

	public void setConverted(boolean converted) {
		this.converted = converted;
	}

	public Block getBlock() {
		return block;
	}
	
	public void setBlock(Block block) {
		this.block =  block;
	}
	
	/*
	 * Methods for spawning, timing, etc.
	 */

	//Tick the spawn rate down and spawn mobs if it is time to spawn. Return the ticks left.
	public int tick() {
		if(active && !(rate <= 0)) {
			ticksLeft--;
			if(ticksLeft == 0) {
				ticksLeft = rate;
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
		this.id = -1;
		this.active = false;
	}
	
	//Spawn the mobs
	public void spawn() {
		//Are conditions valid for spawning?
		boolean canSpawn = true;

		//If the spawner is not active, return
		if(!active) {
			return;
		}

		/*
		 * This block checks if the conditions are met to spawn mobs
		 */
		if(redstoneTriggered && (!loc.getBlock().isBlockIndirectlyPowered() || !loc.getBlock().isBlockPowered())) {
			canSpawn = false;
		} else if(!isPlayerNearby()) {
			canSpawn = false;
		} else if(!(loc.getBlock().getLightLevel() <= maxLightLevel) && !(loc.getBlock().getLightLevel() >= minLightLevel)) {
			canSpawn = false;
		} else if(mobs.size() >= maxMobs) {
			canSpawn = false;
		}

		//If we can spawn
		if(canSpawn) {
			
			//Loop to spawn until the mobs per spawn is reached
			for(int i = 0; i < mobsPerSpawn; i++) {
				
				//Break the loop if the spawn limit is reached
				if(mobs.size() + passiveMobs.size() == maxMobs) {
					break;
				}

				SpawnableEntity spawnType = randType();
				Location spawnLocation = getSpawningLocation(spawnType);
				
				if(spawnLocation == null) {
					//System.out.println("[CSDEBUG] " + "Spawn location is null.");
					return;
				} else if(spawnType == null) {
					//System.out.println("[CSDEBUG] " + "Entity is null.");
					return;
				}
				
				Entity e = spawnTheEntity(spawnType, spawnLocation);
				
				if(e != null) {

					assignMobProps(e, spawnType);

					if(spawnType.isPassive()) {
						passiveMobs.put(e.getEntityId(), spawnType);
					} else {
						mobs.put(e.getEntityId(), spawnType);
					}
					
				}
				
			}
			
		}
		
	}
	
	//Spawn the mobs
	public void forceSpawn() {

		//Loop to spawn until the mobs per spawn is reached
		for(int i = 0; i < mobsPerSpawn; i++) {
			
			SpawnableEntity spawnType = randType();
			Location spawnLocation = getSpawningLocation(spawnType);
			
			if(spawnLocation == null) {
				//System.out.println("[CSDEBUG] " + "Spawn location is null.");
				return;
			} else if(spawnType == null) {
				//System.out.println("[CSDEBUG] " + "Entity is null.");
				return;
			}
			
			Entity e = spawnTheEntity(spawnType, spawnLocation);
			
			if(e != null) {

				assignMobProps(e, spawnType);

				if(spawnType.isPassive()) {
					passiveMobs.put(e.getEntityId(), spawnType);
				} else {
					mobs.put(e.getEntityId(), spawnType);
				}
				
			}
			
		}
		
	}
	
	//Spawn the mobs
	public void forceSpawnType(SpawnableEntity entity) {

		//Loop to spawn until the mobs per spawn is reached
		for(int i = 0; i < mobsPerSpawn; i++) {
			
			Location spawnLocation = getSpawningLocation(entity);
			
			if(spawnLocation == null) {
				//System.out.println("[CSDEBUG] " + "Spawn location is null.");
				return;
			} else if(entity == null) {
				//System.out.println("[CSDEBUG] " + "Entity is null.");
				return;
			}
			
			Entity e = spawnTheEntity(entity, spawnLocation);
			
			if(e != null) {

				assignMobProps(e, entity);
				if(entity.isPassive()) {
					passiveMobs.put(e.getEntityId(), entity);
				} else {
					mobs.put(e.getEntityId(), entity);
				}
				
			}
			
		}
		
	}
	
	/*
	 * Methods for choosing locations, checking things, etc.
	 */
	
	//Check if players are nearby
	private boolean isPlayerNearby() {
		for(Player p : server.getOnlinePlayers()) {
			//Finds distance between spawner and player in 3D space.
			double distance = Math.sqrt(
					Math.pow((p.getLocation().getX() - loc.getX()), 2) + // x coordinate
					Math.pow((p.getLocation().getY() - loc.getY()), 2) + // y coordinate
					Math.pow((p.getLocation().getZ() - loc.getZ()), 2)   // z coordinate
					);
			if(distance <= maxPlayerDistance && distance >= minPlayerDistance) {
				return true;
			}
		}
		return false;
	}
	
	//Check if players are nearby
	private ArrayList<Player> getNearbyPlayers(Location source, double max) {
		ArrayList<Player> players = new ArrayList<Player>();
		for(Player p : server.getOnlinePlayers()) {
			//Finds distance between spawner and player is 3D space.
			double distance = Math.sqrt(
					Math.pow((p.getLocation().getX() - source.getX()), 2) + // x coordinate
					Math.pow((p.getLocation().getY() - source.getY()), 2) + // y coordinate
					Math.pow((p.getLocation().getZ() - source.getZ()), 2)   // z coordinate
					);
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
			return (rand.nextDouble() * radius) * -1;
		} else {
			return (rand.nextDouble() * radius) * 1;
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
		
		for(SpawnableEntity e : typeData.values()) {
			if(count == index) {
				return e;
			} else {
				count++;
			}
		}
		
		return null;
	}
	
	//Assigns properties to a LivingEntity
	private void assignMobProps(Entity baseEntity, SpawnableEntity data) {
		
		baseEntity.setVelocity(data.getVelocity());
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
			//TODO add things like ender pearl after here
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
		
		if(data.isUsingCustomDamage())
			DamageController.damageModEntities.put(baseEntity.getEntityId(), data.getDamage());
		
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
		for(EntityPotionEffect p : data.getEffects()) {
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
	private Location getSpawningLocation(SpawnableEntity type) {
		//Random locations
		double randX = 0;
		double randY = 0;
		double randZ = 0;
		
		//Height of Mob
		int h = 1;
		
		//For flying mobs. Will it require blocks beneath it to spawn?
		boolean reqsBlockBelow = true;
		
		//Can it spawn in liquids?
		boolean spawnInWater = true;
		
		//Actual location
		Location spawnLoc = null;
		
		//Amount of times tried
		int tries = 0;
		
		//Setting special properties
		//TODO Eventually improve this
		switch(type.getType()) {
		case ZOMBIE:
			h = 2;
			break;
		case SKELETON:
			h = 2;
			break;
		case CREEPER:
			h = 2;
			break;
		case ENDERMAN:
			h = 3;
			break;
		case PIG_ZOMBIE:
			h = 2;
			break;
		case SLIME:
			h = type.getSlimeSize();
			break;
		case MAGMA_CUBE:
			h = type.getSlimeSize();
			break;
		case BLAZE:
			h = 2;
			break;
		case GHAST:
			h = 4;
			break;
		case VILLAGER:
			h = 2;
			break;
		case IRON_GOLEM:
			h = 2;
			break;
		case SNOWMAN:
			h = 2;
			break;
		case ENDER_DRAGON:
			h = 3;
			break;
		case EGG:
			reqsBlockBelow = false;
			break;
		case ENDER_PEARL:
			reqsBlockBelow = false;
			break;
		case ARROW:
			reqsBlockBelow = false;
			break;
		case SNOWBALL:
			reqsBlockBelow = false;
			break;
		case FALLING_BLOCK:
			reqsBlockBelow = false;
			break;
		case PRIMED_TNT:
			reqsBlockBelow = false;
			break;
		case SMALL_FIREBALL:
			reqsBlockBelow = false;
			break;
		case FIREBALL:
			reqsBlockBelow = false;
			break;
		case SPLASH_POTION:
			reqsBlockBelow = false;
			break;
		case THROWN_EXP_BOTTLE:
			reqsBlockBelow = false;
			break;
		case DROPPED_ITEM:
			reqsBlockBelow = false;
			break;
		case ENDER_CRYSTAL:
			reqsBlockBelow = false;
			break;
		default:
			h = 1;
			reqsBlockBelow = true;
			break;
		}
		
		//As long as the mob has not been spawned, keep trying
		while(tries < 128) {

			//Getting a random location.
			if(!useSpawnArea) {
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
			
			boolean canSpawnH = false;
			boolean canSpawnB = false;
			boolean canSpawnL = false;
			
			int counter = 0;
			while(counter < h) {
				Location checkLoc = new Location(loc.getWorld(), randX, randY + counter, randZ);
				
				if(isEmpty(checkLoc, spawnInWater)) {
					canSpawnH = true;
				}
				
				counter++;
			}
			
			if(reqsBlockBelow) {
				Location blockBelow = new Location(loc.getWorld(), randX, randY - 1, randZ);
				
				if(!isEmpty(blockBelow, !spawnInWater)) {
					canSpawnB = true;
				}
			} else {
				canSpawnB = true;
			}
			
			if((loc.getBlock().getLightLevel() <= maxLightLevel) && (loc.getBlock().getLightLevel() >= minLightLevel)) {
				canSpawnL = true;
			}
			
			if(canSpawnH && canSpawnB && canSpawnL) {
				return spawnLoc;
			}
			
			tries++;
		}
		
		return null;
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
			return loc.getWorld().dropItemNaturally(spawnLocation, spawnType.getItemType());
		} else if(spawnType.getType().equals(EntityType.SPLASH_POTION)) { 
			
			ArrayList<Player> nearPlayers = getNearbyPlayers(loc, maxPlayerDistance) ;
			
			if(nearPlayers.size() > 0) {
				Player nearPlayer = nearPlayers.get(0);
				Projectile potion = nearPlayer.launchProjectile(ThrownPotion.class);
				potion.teleport(spawnLocation);
			}
			
		} else if(spawnType.getType().equals(EntityType.FALLING_BLOCK)) {
			return loc.getWorld().spawnFallingBlock(spawnLocation, spawnType.getItemType().getType(), (byte) 0);
		} else {
			return loc.getWorld().spawnEntity(spawnLocation, spawnType.getType());
		}
		
		return null;
		
	}
	
}
