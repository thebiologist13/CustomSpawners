package com.github.thebiologist13;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Spawner {
	public int id = -1; //Identification # for spawner
	public EntityType type = null; //Type of entity to spawn
	public double radius = 0; //Radius that it can spawn in
	public boolean redstoneTriggered = false; //Does it need to be redstone triggered to spawn?
	public int maxPlayerDistance = -1; //Maximum distance a player can be from it to spawn mobs
	public int minPlayerDistance = -1; //Minimum distance
	public boolean active = false; //Is this spawner active?
	public Location loc = null; //Location of the spawner
	public byte maxLightLevel = -1; //Maximum light level the spawner can spawn entities at
	public byte minLightLevel = -1; //Minimum light level
	public boolean hidden = false;
	public int ticksLeft = -1; //Ticks left before next spawn
	public int rate = -1; //Rate in how many ticks to spawn at
	public ArrayList<Integer> mobs = new ArrayList<Integer>(); //Integer is mob ID. This holds the entities that have been spawned so when one dies, it can be removed from maxMobs.
	public int mobsPerSpawn = 0; //Amount of mobs to spawn per time
	public int maxMobs = -1; //Maximum amount of mobs it will spawn
	private Server server = null; //Used to get online players
	
	//Make a new custom spawner of EntityType type, at Location loc, with ID id, and in Server server.
	public Spawner(EntityType type, Location loc, int id, Server server) {
		this.id = id;
		this.type = type;
		this.loc = loc;
		this.server = server;
	}
	
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
		//Random locations
		double randX = 0;
		double randY = 0;
		double randZ = 0;
		
		//If the spawner is not active, return
		if(!active) {
			return;
		}
		
		/*
		 * This block checks if the conditions are met to spawn mobs
		 */
		if(redstoneTriggered && !loc.getBlock().isBlockPowered()) {
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
				if(mobs.size() == maxMobs) {
					break;
				}
				
				//Has the mob been spawned?
				boolean spawned = false;
				
				//Amount of times to try
				int tries = 0;
				
				//As long as the mob has not been spawned, keep trying
				while(!spawned && tries <= 256) {
					//Generates a random location using randomGen(), a location for the block above, and a location for the block below.
					randX = randomGen()  + loc.getBlockX();
					randY = Math.round(randomGen()) + loc.getBlockY();
					randZ = randomGen() + loc.getBlockZ();
					Location spawnLoc = new Location(loc.getWorld(), randX, randY, randZ);
					Location blockBelow = new Location(loc.getWorld(), randX, randY - 1, randZ);
					Location blockAbove = new Location(loc.getWorld(), randX, randY + 1, randZ);
					
					//Gets the light level of the spawn block
					boolean acceptableLight = false;
					byte light = spawnLoc.getBlock().getLightLevel();
					if(light <= maxLightLevel && light >= minLightLevel) {acceptableLight = true;}
					
					/*
					 * If the blocks are empty, it has something to stand on, and the light level is acceptable: 
					 * spawn the creature and add it to the list of mobs, then set this mob spawned
					 */
					if(spawnLoc.getBlock().isEmpty() && blockAbove.getBlock().isEmpty() && !blockBelow.getBlock().isEmpty() && acceptableLight) {
						LivingEntity le = loc.getWorld().spawnCreature(spawnLoc, type);
						mobs.add(le.getEntityId());
						spawned = true;
					}
					
					tries++;
				}
			}
		}
	}

	//Called when a mob dies
	public boolean onMobDeath(LivingEntity entity) {
		for(int i = 0; i < mobs.size(); i++) {
			if(mobs.get(i) == entity.getEntityId()) {
				mobs.remove(i);
				return true;
			}
		}
		return false;
	}
	
	//Check if players are nearby
	private boolean isPlayerNearby() {
		for(Player p : server.getOnlinePlayers()) {
			//Finds distance between spawner and player is 3D space.
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
	
	//Generate random double for location's parts within radius
	private double randomGen() {
		Random rand = new Random();
		if(rand.nextFloat() < 0.5) {
			return (rand.nextDouble() * radius) * -1;
		} else {
			return (rand.nextDouble() * radius) * 1;
		}
	}
}
