package com.github.thebiologist13.commands.entities;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.api.ISItemStack;
import com.github.thebiologist13.serialization.SPotionEffect;

public class EntityInfoCommand extends EntityCommand {

	public EntityInfoCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityInfoCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		PLUGIN.sendMessage(sender, getInfo(entity));
	}

	@SuppressWarnings("deprecation")
	private String[] getInfo(SpawnableEntity s) {
		
		String effectMessage = ""; 
		List<SPotionEffect> effects = s.getEffects();
		for(int i = 0; i < effects.size(); i++) {
			if(i == 0) {
				effectMessage += effects.get(i).getType().getName() + " " + effects.get(i).getAmplifier();
			} else {
				effectMessage += ", " + effects.get(i).getType().getName() + " " + effects.get(i).getAmplifier();
			}
		}
		
		if(effectMessage.isEmpty()) {
			effectMessage = "No effects.";
		}
		
		String header = ChatColor.GREEN + "Information on entity with ID " + ChatColor.GOLD + String.valueOf(s.getId());
		
		if(!s.getName().isEmpty()) {
			header += " (" + s.getName() + ")";
		}
		
		String nameOfType = s.getType().toString();
		
		if(nameOfType.equals("Spider") && s.isJockey()) {
			nameOfType = "Spider Jockey";
		}
		
		String blackListMsg = "";
		List<String> blackList = s.getDamageBlacklist();
		for(int i = 0; i < blackList.size(); i++) {
			if(i == 0) {
				blackListMsg += blackList.get(i);
			} else {
				blackListMsg += ", " + blackList.get(i);
			}
		}
		
		String whiteListMsg = "";
		List<String> whiteList = s.getDamageWhitelist();
		for(int i = 0; i < whiteList.size(); i++) {
			if(i == 0) {
				whiteListMsg += whiteList.get(i);
			} else {
				whiteListMsg += ", " + whiteList.get(i);
			}
		}
		
		String itemListMsg = "";
		List<ItemStack> itemList = s.getItemDamageList();
		for(int i = 0; i < itemList.size(); i++) {
			ItemStack item = itemList.get(i);
			if(i == 0) {
				itemListMsg += item.getTypeId() + ":" + item.getDurability();
			} else {
				itemListMsg += ", " + item.getTypeId() + ":" + item.getDurability();
			}
		}
		
		String dropMsg = "";
		List<ItemStack> drops = s.getDrops();
		for(int i = 0; i < drops.size(); i++) {
			ItemStack item = drops.get(i);
			String append = PLUGIN.getItemName(item) + " #" + item.getAmount(); 
			if(i == 0) {
				dropMsg += append;
			} else {
				dropMsg += ", " + append;
			}
		}
		
		String invMsg = "";
		invMsg += "[HEAD] -> " + PLUGIN.getItemName(s.getInventory().getArmor()[3]) + " ";
		invMsg += "[CHEST] -> " + PLUGIN.getItemName(s.getInventory().getArmor()[2]) + " ";
		invMsg += "[LEGS] -> " + PLUGIN.getItemName(s.getInventory().getArmor()[1]) + " ";
		invMsg += "[BOOTS] -> " + PLUGIN.getItemName(s.getInventory().getArmor()[0]) + " ";
		invMsg += "[HAND] -> " + PLUGIN.getItemName(s.getInventory().getHand()) + " ";
		
		Map<Integer, ISItemStack> content = s.getInventory().getContent();
		for(Integer i : content.keySet()) {
			ISItemStack stack = content.get(i);
			invMsg += "[" + i + "] ->" + PLUGIN.getItemName(stack) + " #" + stack.getCount() + " "; 
		}
		
		SPotionEffect epe = s.getPotionEffect();
		
		String typeOfExpDrop = (s.getType().equals(EntityType.THROWN_EXP_BOTTLE)) ? "Experience Bottle Exp: " : "Mob Dropped Exp: ";
		
		boolean isWither = false;
		if(s.hasProp("wither")) {
			isWither = (Boolean) s.getProp("wither");
		}
		
		boolean isVillager = false;
		if(s.hasProp("zombie")) {
			isVillager = (Boolean) s.getProp("zombie");
		}
		
		double minecartSpeed = 0.4d;
		if(s.hasProp("minecartSpeed")) {
			minecartSpeed = (Double) s.getProp("minecartSpeed");
		}
		
		String health = "" + s.getHealth(null);
		if(s.hasModifier("health") || s.hasModifier("hp")) {
			health += " (Dynamic)";
		}
		
		String modMessage = "";
		int count = 0;
		for(String str : s.getModifiers().keySet()) {
			if(count == 0) {
				modMessage += str + " = " + s.getModifier(str);
			} else {
				modMessage += ", " + str + " = " + s.getModifier(str);
			}
			count++;
		}
		
		String age = "" + s.getAge(null);
		age = (s.hasModifier("age")) ? age + " (Dynamic)" : age;
		
		String air = "" + s.getAir(null);
		air = (s.hasModifier("air")) ? air + " (Dynamic)" : air;
		
		String damage = "" + s.getDamage(null);
		damage = (s.hasModifier("damage")) ? damage + " (Dynamic)" : damage;
		
		String x = "" + s.getXVelocity(null);
		x = (s.hasModifier("x")) ? x + " (Dynamic)" : x;
		
		String y = "" + s.getYVelocity(null);
		y = (s.hasModifier("y")) ? y + " (Dynamic)" : y;
		
		String z = "" + s.getZVelocity(null);
		z = (s.hasModifier("z")) ? z + " (Dynamic)" : z;
		
		String xp = "" + s.getDroppedExp(null);
		xp = (s.hasModifier("xp")) ? z + " (Dynamic)" : xp;
		
		//Send info
		String[] message = {
				"",
				header + ChatColor.GREEN + ": ",
				"",
				ChatColor.GOLD + "Type: " + nameOfType,
				ChatColor.GOLD + "Name: " + s.getName(),
				ChatColor.GOLD + "Show Name: " + s.showCustomName(),
				ChatColor.GOLD + "Effects: " + effectMessage,
				ChatColor.GOLD + "X Velocity: " + x,
				ChatColor.GOLD + "Y Velocity: " + y,
				ChatColor.GOLD + "Z Velocity: " + z,
				ChatColor.GOLD + "Age: " + age,
				ChatColor.GOLD + "Health: " + health,
				ChatColor.GOLD + "Air: " + air,
				ChatColor.GOLD + "Profession: " + s.getProfession().toString(),
				ChatColor.GOLD + "Enderman Block: " + PLUGIN.getItemName(s.getEndermanBlock().toItemStack()),
				ChatColor.GOLD + "Saddled: " + s.isSaddled(),
				ChatColor.GOLD + "Charged: " + s.isCharged(),
				ChatColor.GOLD + "Jockey: " + s.isJockey(),
				ChatColor.GOLD + "Rider: " + PLUGIN.getFriendlyName(s.getRider()),
				ChatColor.GOLD + "Tamed: " + s.isTamed(),
				ChatColor.GOLD + "Sitting: " + s.isSitting(),
				ChatColor.GOLD + "Angry: " + s.isAngry(),
				ChatColor.GOLD + "Cat Type: " + s.getCatType(),
				ChatColor.GOLD + "Slime Size: " + s.getSlimeSize(),
				ChatColor.GOLD + "Color: " + s.getColor(),
				ChatColor.GOLD + "Passive: " + String.valueOf(s.isPassive()),
				ChatColor.GOLD + "Fire Ticks: " + s.getFireTicks(null),
				ChatColor.GOLD + "Using Blacklist: " + s.isUsingBlacklist(),
				ChatColor.GOLD + "Using Whitelist: " + s.isUsingWhitelist(),
				ChatColor.GOLD + "Damage Blacklist: " + blackListMsg,
				ChatColor.GOLD + "Damage Whitelist: " + whiteListMsg,
				ChatColor.GOLD + "Damage Itemlist: " + itemListMsg,
				ChatColor.GOLD + "Using Custom Damage: " + s.isUsingCustomDamage(),
				ChatColor.GOLD + "Damage Dealt: " + damage,
				ChatColor.GOLD + "Potion Type: " + epe.getType().getName() + " " + epe.getAmplifier() + " - " + PLUGIN.convertTicksToTime(epe.getDuration()),
				ChatColor.GOLD + typeOfExpDrop + xp,
				ChatColor.GOLD + "Fuse Ticks: " + s.getFuseTicks(null),
				ChatColor.GOLD + "Explosive Yield: " + s.getYield(null),
				ChatColor.GOLD + "Incendiary: " + s.isIncendiary(),
				ChatColor.GOLD + "Item Type: " + PLUGIN.getItemName(s.getItemType()),
				ChatColor.GOLD + "Using Custom Drops: " + s.isUsingCustomDrops(),
				ChatColor.GOLD + "Drops: " + dropMsg,
				ChatColor.GOLD + "Inventory: " + invMsg,
				ChatColor.GOLD + "Invincible: " + s.isInvulnerable(),
				ChatColor.GOLD + "Wither: " + String.valueOf(isWither),
				ChatColor.GOLD + "Villager: " + String.valueOf(isVillager),
				ChatColor.GOLD + "Spawner: " + PLUGIN.getFriendlyName(s.getSpawnerData()),
				ChatColor.GOLD + "Minecart Speed: " + minecartSpeed,
				ChatColor.GOLD + "Modifiers: " + modMessage,
				ChatColor.GREEN + "Scroll Up for More Properties."
		};
		
		return message;
		
	}

}
