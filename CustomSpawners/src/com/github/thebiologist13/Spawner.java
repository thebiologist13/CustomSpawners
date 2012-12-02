package com.github.thebiologist13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.entity.CraftEntity;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.github.thebiologist13.listeners.DamageController;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

@SerializableAs("Spawner")
public class Spawner implements ConfigurationSerializable {
	
	//Main Data
	private Map<String, Object> data = new HashMap<String, Object>();
	//Spawnable Mobs
	private Map<Integer, SpawnableEntity> typeData = new HashMap<Integer, SpawnableEntity>();
	//Integer is mob ID. This holds the entities that have been spawned so when one dies, it can be removed from maxMobs.
	private Map<Integer, SpawnableEntity> mobs = new HashMap<Integer, SpawnableEntity>(); 
	//List of currently passive mobs from a spawner. When provoked, they are moved to "mobs"
	private Map<Integer, SpawnableEntity> passiveMobs = new HashMap<Integer, SpawnableEntity>(); 
	
	//Ticks left before next spawn
	private int ticksLeft = -1;
	
	/*
	 * Constructors
	 */
	
	public Spawner(SpawnableEntity type, Location loc, int id, Server server) {
		Location[] areaPoints = new Location[2];
		
		data.put("id", id);
		data.put("loc", loc);
		data.put("server", server);
		data.put("areaPoints", areaPoints);
		typeData.put(type.getId(), type);
		data.put("converted", false);
		data.put("mainEntity", type);
	}
	
	public Spawner(SpawnableEntity type, Location loc, String name, int id, Server server) {
		Location[] areaPoints = new Location[2];
		
		data.put("id", id);
		data.put("loc", loc);
		data.put("server", server);
		data.put("areaPoints", areaPoints);
		data.put("name", name);
		typeData.put(type.getId(), type);
		data.put("converted", false);
		data.put("mainEntity", type);
	}
	
	public void initServer(Server server) {
		data.put("server", server);
	}
	
	public int getId() {
		return (Integer) data.get("id");
	}
	
	public String getName() {
		return (data.containsKey("name")) ? (String) data.get("name") : "";
	}

	public void setName(String name) {
		data.put("name", name);
	}

	public Map<Integer, SpawnableEntity> getTypeData() {
		return this.typeData;
	}

	public void setTypeData(Map<Integer, SpawnableEntity> typeDataParam) {
		
		if(typeDataParam == null) {
			return;
		}
		
		this.typeData = typeDataParam;
	}
	
	public void addTypeData(SpawnableEntity data) {
		typeData.put(data.getId(), data);
	}
	
	public void removeTypeData(SpawnableEntity type) {
		if(typeData.containsKey(type.getId())) {
			typeData.remove(type.getId());
		}
	}
	
	public SpawnableEntity getMainEntity() {
		return (SpawnableEntity) data.get("mainEntity");
	}
	
	public boolean isActive() {
		return (data.containsKey("active")) ? (Boolean) data.get("active") : false;
	}

	public void setActive(boolean active) {
		data.put("active", active);
	}

	public boolean isHidden() {
		return (data.containsKey("hidden")) ? (Boolean) data.get("hidden") : false;
	}

	public void setHidden(boolean hidden) {
		data.put("hidden", hidden);
	}

	public double getRadius() {
		return (data.containsKey("radius")) ? (Double) data.get("radius") : 0d;
	}

	public void setRadius(double radius) {
		data.put("radius", radius);
	}

	public boolean isUsingSpawnArea() {
		return (data.containsKey("useSpawnArea")) ? (Boolean) data.get("useSpawnArea") : false;
	}

	public void setUseSpawnArea(boolean useSpawnArea) {
		data.put("useSpawnArea", useSpawnArea);
	}

	public boolean isRedstoneTriggered() {
		return (data.containsKey("redstone")) ? (Boolean) data.get("redstone") : false;
	}

	public void setRedstoneTriggered(boolean redstoneTriggered) {
		data.put("redstone", redstoneTriggered);
	}

	public int getMaxPlayerDistance() {
		return (data.containsKey("maxDistance")) ? (Integer) data.get("maxDistance") : 0;
	}

	public void setMaxPlayerDistance(int maxPlayerDistance) {
		data.put("maxDistance", maxPlayerDistance);
	}

	public int getMinPlayerDistance() {
		return (data.containsKey("minDistance")) ? (Integer) data.get("minDistance") : 0;
	}

	public void setMinPlayerDistance(int minPlayerDistance) {
		data.put("minDistance", minPlayerDistance);
	}

	public byte getMaxLightLevel() {
		return (data.containsKey("maxLight")) ? (Byte) data.get("maxLight") : 0;
	}

	public void setMaxLightLevel(byte maxLightLevel) {
		data.put("maxLight", maxLightLevel);
	}

	public byte getMinLightLevel() {
		return (data.containsKey("minLight")) ? (Byte) data.get("minLight") : 0;
	}

	public void setMinLightLevel(byte minLightLevel) {
		data.put("minLight", minLightLevel);
	}

	public int getMobsPerSpawn() {
		return (data.containsKey("mobsPerSpawn")) ? (Integer) data.get("mobsPerSpawn") : 0;
	}

	public void setMobsPerSpawn(int mobsPerSpawn) {
		data.put("mobsPerSpawn", mobsPerSpawn);
	}

	public int getMaxMobs() {
		return (data.containsKey("maxMobs")) ? (Integer) data.get("maxMobs") : 0;
	}

	public void setMaxMobs(int maxMobs) {
		data.put("maxMobs", maxMobs);
	}

	public Location getLoc() {
		return (Location) data.get("loc");
	}

	public void setLoc(Location loc) {
		data.put("loc", loc);
		data.put("block", loc.getBlock());
	}

	public Location[] getAreaPoints() {
		return (Location[]) data.get("areaPoints");
	}

	public void setAreaPoints(Location[] areaPoints) {
		data.put("areaPoints", areaPoints);
	}
	
	public void changeAreaPoint(int index, Location value) {
		if(index != 0 || index != 1) {
			return;
		}
		
		Location[] ap1 = (Location[]) data.get("areaPoints");
		ap1[index] = value;
		
		data.put("areaPoints", ap1);
	}

	public int getRate() {
		return (data.containsKey("rate")) ? (Integer) data.get("rate") : 60;
	}

	public void setRate(int rate) {
		data.put("rate", rate);
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
	
	public Map<Integer, SpawnableEntity> getPassiveMobs() {
		return passiveMobs;
	}

	public void setPassiveMobs(Map<Integer, SpawnableEntity> pMobParam) {
		this.passiveMobs = pMobParam;
	}
	
	public void addPassiveMob(int mobId, SpawnableEntity entity) {
		passiveMobs.put(mobId, entity);
	}
	
	public void removePassiveMob(int mobId) {
		if(passiveMobs.containsKey(mobId))
			passiveMobs.remove(mobId);
	}
	
	public Map<Integer, SpawnableEntity> getAllMobs() {
		Map<Integer, SpawnableEntity> allMobs = new HashMap<Integer, SpawnableEntity>();
		allMobs.putAll(mobs);
		allMobs.putAll(passiveMobs);
		return allMobs;
	}
	
	public boolean isConverted() {
		return (Boolean) data.get("converted");
	}

	public void setConverted(boolean converted) {
		data.put("converted", converted);
	}

	public Block getBlock() {
		return (Block) data.get("block");
	}
	
	public void setBlock(Block block) {
		data.put("block", block);
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
	
	public void setData(Map<String, Object> data) {
		
		if(data == null)
			return;
		
		data.put("id", (Integer) this.data.get("id"));
		data.put("name", (String) this.data.get("name"));
		
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
		if(!((Boolean) data.get("active"))) {
			return;
		}

		/*
		 * This block checks if the conditions are met to spawn mobs
		 */
		if(((Boolean) data.get("redstone")) && (!((Block) data.get("block")).isBlockIndirectlyPowered() || !((Block) data.get("block")).isBlockPowered())) {
			canSpawn = false;
		} else if(!isPlayerNearby()) {
			canSpawn = false;
		} else if(!(((Block) data.get("block")).getLightLevel() <= ((Byte) data.get("maxLight"))) && 
				!(((Block) data.get("block")).getLightLevel() <= ((Byte) data.get("minLight")))) {
			canSpawn = false;
		} else if(mobs.size() >= ((Integer) data.get("maxMobs"))) {
			canSpawn = false;
		}

		//If we can spawn
		if(canSpawn) {
			
			//Break the loop if the spawn limit is reached
			if(!(mobs.size() + passiveMobs.size() == ((Integer) data.get("maxMobs")))) {
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
		for(int i = 0; i < ((Integer) data.get("mobsPerSpawn")); i++) {

			/*Location spawnLocation = getSpawningLocation(spawnType);
			
			if(spawnLocation == null) {
				//System.out.println("[CSDEBUG] " + "Spawn location is null.");
				return;
			} else if(spawnType == null) {
				//System.out.println("[CSDEBUG] " + "Entity is null.");
				return;
			}*/
			
			Entity e = spawnTheEntity(spawnType, (Location) data.get("loc"));
			
			if(!wgAllows(e)) {
				e.remove();
				return;
			}
			
			if(e != null) {

				assignMobProps(e, spawnType);
				net.minecraft.server.Entity nmEntity = ((CraftEntity) e).getHandle();
				Location spawnLocation = getSpawningLocation(e, spawnType, nmEntity.height, nmEntity.width, nmEntity.length);
				e.teleport(spawnLocation);

				if(spawnType.isPassive()) {
					passiveMobs.put(e.getEntityId(), spawnType);
				} else {
					mobs.put(e.getEntityId(), spawnType);
				}
				
			}
			
		}
		
	}
	
	//Check if players are nearby
	private boolean isPlayerNearby() {
		for(Player p : ((Server) data.get("server")).getOnlinePlayers()) {
			
			//Finds distance between spawner and player in 3D space.
			double distance = p.getLocation().distance((Location) data.get("loc"));
			if(distance <= ((Integer) data.get("maxDistance")) && distance >= ((Integer) data.get("minDistance"))) {
				return true;
			}
		}
		return false;
	}
	
	//Check if players are nearby
	private ArrayList<Player> getNearbyPlayers(Location source, double max) {
		ArrayList<Player> players = new ArrayList<Player>();
		for(Player p : ((Server) data.get("server")).getOnlinePlayers()) {
			//Finds distance between spawner and player is 3D space.
			double distance = p.getLocation().distance((Location) data.get("loc"));
			
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
			return Math.floor((rand.nextDouble() * ((Integer) data.get("radius"))) * -1) - 0.5;
		} else {
			return Math.floor((rand.nextDouble() * ((Integer) data.get("radius"))) * 1) + 0.5;
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
	private Location getSpawningLocation(Entity e, SpawnableEntity type, float height, float width, float length) {
		//Random locations
		double randX = 0;
		double randY = 0;
		double randZ = 0;
		
		//For flying mobs. Will it require blocks beneath it to spawn?
		boolean reqsBlockBelow = true;
		
		//Can it spawn in liquids?
		boolean spawnInWater = true;
		
		//Actual location
		Location spawnLoc = null;
		
		//Amount of times tried
		int tries = 0;
		
		//Setting special properties
		switch(type.getType()) {
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
		
		//As long as the mob has not been spawned, keep trying
		while(tries < 128) {

			tries++;
			Location loc = (Location) data.get("loc");
			Location[] areaPoints = (Location[]) data.get("areaPoints");
			
			//Getting a random location.
			if(!((Boolean) data.get("useSpawnArea"))) {
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
			
			if(!((loc.getBlock().getLightLevel() <= ((Byte) data.get("maxLight"))) && (loc.getBlock().getLightLevel() >= ((Byte) data.get("minLight")))))
				continue;
			
			return spawnLoc;
		}
		
		return null;
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
			return ((Location) data.get("loc")).getWorld().dropItemNaturally(spawnLocation, spawnType.getItemType());
		} else if(spawnType.getType().equals(EntityType.FALLING_BLOCK)) {
			return ((Location) data.get("loc")).getWorld().spawnFallingBlock(spawnLocation, spawnType.getItemType().getType(), (byte) 0);
		} else {
			return ((Location) data.get("loc")).getWorld().spawn(spawnLocation, spawnType.getType().getEntityClass());
		}
		
	}
	
	private boolean wgAllows(Entity e) {
		
		WorldGuardPlugin wg = (WorldGuardPlugin) ((Server) data.get("server")).getPluginManager().getPlugin("WorldGuard");
		
		if(wg == null || !(wg instanceof WorldGuardPlugin))
			return true;
		
		ApplicableRegionSet set = wg.getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
		
		if(!set.allows(DefaultFlag.MOB_SPAWNING))
			return false;
		
		if(!set.getFlag(DefaultFlag.DENY_SPAWN).contains(e.getType()))
			return false;
		
		return true;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		map.put("typeData", typeData);
		map.put("mobs", mobs);
		map.put("passiveMobs", passiveMobs);
		return map;
	}
	
	public static Spawner deserialize(Map<String, Object> map) {
		Map<?,?> dataMap = (Map<?,?>) map.get("data");
		Map<?,?> typeDataMap = (Map<?,?>) map.get("typeData");
		Map<?,?> mobMap = (Map<?,?>) map.get("mobs");
		Map<?,?> pMobMap = (Map<?,?>) map.get("passiveMobs");
		SpawnableEntity type = (SpawnableEntity) dataMap.get("mainEntity");
		Location loc = (Location) dataMap.get("loc");
		int id = (Integer) dataMap.get("id");
		Spawner s = new Spawner(type, loc, id, null); //NOTE: Doesn't initialize server. Use initServer(Server server) later.
		
		Map<String, Object> dataParam = new HashMap<String, Object>();
		for(Object o : dataMap.keySet()) {
			
			if(o instanceof String) {
				String key = (String) o;
				dataParam.put(key, dataMap.get(key));
			}
				
		}
		
		Map<Integer, SpawnableEntity> typeDataParam = new HashMap<Integer, SpawnableEntity>();
		for(Object o : typeDataMap.keySet()) {
			
			if(o instanceof Integer) {
				int key = (Integer) o;
				typeDataParam.put(key, (SpawnableEntity) typeDataMap.get(key));
			}
				
		}
		
		Map<Integer, SpawnableEntity> mobParam = new HashMap<Integer, SpawnableEntity>();
		for(Object o : mobMap.keySet()) {
			
			if(o instanceof Integer) {
				int key = (Integer) o;
				mobParam.put(key, (SpawnableEntity) mobMap.get(key));
			}
				
		}
		
		Map<Integer, SpawnableEntity> pMobParam = new HashMap<Integer, SpawnableEntity>();
		for(Object o : pMobMap.keySet()) {
			
			if(o instanceof Integer) {
				int key = (Integer) o;
				pMobParam.put(key, (SpawnableEntity) pMobMap.get(key));
			}
				
		}
		
		s.setData(dataParam);
		
		s.setTypeData(typeDataParam);
		
		s.setMobs(mobParam);
		
		s.setPassiveMobs(pMobParam);
		
		return s;
	}
	
}
