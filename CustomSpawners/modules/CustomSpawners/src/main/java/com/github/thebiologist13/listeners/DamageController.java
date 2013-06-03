package com.github.thebiologist13.listeners;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.api.IDamageController;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class DamageController implements IDamageController {
	
	//Map for aggroed mobs (entity id, spawnableEntity)
	public static ConcurrentHashMap<UUID, Integer> angryMobs = new ConcurrentHashMap<UUID, Integer>();
	
	//Explosions
	public static ConcurrentHashMap<Integer, CustomExplosion> explode = new ConcurrentHashMap<Integer, CustomExplosion>();
	
	/*
	 * The key is an entity ID. The value is ticks left. If an entity ID is in this list, 
	 * it has caught fire from somewhere else (other than fire ticks prop.) and should take fire damage.
	 */
	public static ConcurrentHashMap<UUID, Integer> negatedFireImmunity = new ConcurrentHashMap<UUID, Integer>();

	private CustomSpawners plugin;
	
	public DamageController(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	//Get the damage to deal
	@Override
	public int getModifiedDamage(EntityDamageEvent ev) {
		
		DamageCause cause = ev.getCause();
		Entity entity = ev.getEntity();
		UUID mobId = entity.getUniqueId();
		int damage = ev.getDamage();
		
		if(entity instanceof Player) {
			
			if(ev instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
				Entity damager = eve.getDamager();
				UUID id = damager.getUniqueId();
				
				SpawnableEntity e = plugin.getEntityFromSpawner(id);
				
				if(e != null) {
					
					if(cause.equals(DamageCause.ENTITY_EXPLOSION)) {
						ev.setCancelled(true);
						return 0;
					}
					
					return e.getDamage(damager);
				} else {
					
					if(explode.containsKey(id)) {
						damage = explode.get(id).getDamage();
						if(damage == -1) {
							damage = ev.getDamage();
						}
						
					}
					
				}
				
			}
			
		} else {
			
			SpawnableEntity e = plugin.getEntityFromSpawner(entity.getUniqueId());
			
			if(e == null) {
				return damage;
			}
			
			if(!wgAllows(entity)) {
				ev.setCancelled(true);
				return 0;
			}
			
			if(ev instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
				Entity damager = eve.getDamager();
				UUID id = damager.getUniqueId();
				
				if(damager instanceof Player) {
					angryMobs.put(mobId, e.getId());
				}
				
				if(damager instanceof Projectile) {
					Projectile p = (Projectile) damager;
					if(p.getShooter() instanceof Player) 
						angryMobs.put(mobId, e.getId());
				}
				
				SpawnableEntity e1 = plugin.getEntityFromSpawner(id);
				
				if(e1 != null && cause.equals(DamageCause.ENTITY_EXPLOSION)) {
					ev.setCancelled(true);
					return 0;
				}
				
				if(explode.containsKey(id)) {
					damage = explode.get(id).getDamage();
					if(damage == -1) {
						damage = ev.getDamage();
					}
					
				}
				
			}
			
			if(e.isInvulnerable()) {
				ev.setCancelled(true);
				return 0;
			}
			
			if(e.isUsingBlacklist()) { //Things to not take damage from
				List<String> black = e.getDamageBlacklist();
				
				if(black.contains("SPAWNER_FIRE_TICKS") && cause.equals(DamageCause.FIRE_TICK)) {
					
					if(negatedFireImmunity.containsKey(mobId)) {
						int newTicks = negatedFireImmunity.get(mobId) - 1;
						
						if(newTicks == 0) {
							negatedFireImmunity.remove(mobId);
						}
						
						negatedFireImmunity.replace(mobId, newTicks);
					} else {
						negatedFireImmunity.put(mobId, e.getFireTicks(entity));
					}
					
					ev.setCancelled(true);
					return 0;
					
				} else if(black.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent)) {
					
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					Entity damager = eve.getDamager();
					
					if(damager instanceof Player) {
						
						Player p = (Player) damager;
						
						for(ItemStack i : e.getItemDamageList()) {
							ItemStack hand = p.getItemInHand();
							if(hand.getType().equals(Material.AIR) && i.getTypeId() == 0) {
								ev.setCancelled(true);
								return 0;
							}
							if(hand.getTypeId() == i.getTypeId() && hand.getData().getData() == i.getData().getData()) {
								ev.setCancelled(true);
								return 0;
							}
						}
						
					}
					
				} else if(black.contains(cause.name().toUpperCase())) {
					
					ev.setCancelled(true);
					return 0;
					
				}
				
			} else if(e.isUsingWhitelist()) {
				
				List<String> white = e.getDamageWhitelist();
				
				if(white.contains("ITEM") && (ev instanceof EntityDamageByEntityEvent)) {
					
					EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) ev;
					Entity damager = eve.getDamager();
					
					if(damager instanceof Player) {
						
						Player p = (Player) damager;
						
						for(ItemStack i : e.getItemDamageList()) {
							ItemStack hand = p.getItemInHand();
							if(hand.getType().equals(Material.AIR) && i.getTypeId() == 0)
								return damage;
							if(hand.getTypeId() == i.getTypeId() && hand.getData().getData() == i.getData().getData())
								return damage;
						}
					}
				} else if(white.contains(cause.name().toUpperCase())) {
					return damage;
				}
				
				ev.setCancelled(true);
				return 0;
				
			}
		}
		
		//The EntityHpTracker has been removed in favor of Damagable.setMaxHealth(int hp)
			
		return damage;
		
	}

	private boolean wgAllows(Entity e) {
		
		WorldGuardPlugin wg = CustomSpawners.getWG();
		
		if(wg == null)
			return true;
		
		ApplicableRegionSet set = wg.getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
		
		if(!set.allows(DefaultFlag.MOB_DAMAGE))
			return false;
		
		return true;
	}
	
}
