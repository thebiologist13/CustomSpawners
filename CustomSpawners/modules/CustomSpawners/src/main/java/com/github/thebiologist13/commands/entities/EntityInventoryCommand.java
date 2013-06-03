package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.serialization.SInventory;
import com.github.thebiologist13.serialization.SItemStack;

public class EntityInventoryCommand extends EntityCommand {
	
	public EntityInventoryCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityInventoryCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		SInventory inv = entity.getInventory();
		
		if(subCommand.equals("clearinventory")) {
			inv.empty();
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully cleared entity with ID " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "'s inventory.");
			return;
		}
		
		String item = getValue(args, 0, "0");
		String count = getValue(args, 1, "1");
		String chance = "";
		String[] split = item.split("%");
		
		if(split.length == 2) {
			item = split[0];
			chance = split[1];
		}
		
		ItemStack stack = null;
		
		if(item.equals("hand") || item.equals("holding")) {
			if(!(sender instanceof Player)) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "You need to be in-game to add you item in hand to a mob inventory.");
				return;
			}
			
			Player p = (Player) sender;
			
			stack = p.getItemInHand();
			
		} else {
			if(!CustomSpawners.isInteger(count)) {
				PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
				return;
			}
			
			stack = PLUGIN.getItem(item, Integer.parseInt(count));
		}
		
		SItemStack newStack = new SItemStack(stack);
		float toDrop = 100.0f;
		
		if(!chance.isEmpty() && split.length == 2) {
			if(!CustomSpawners.isFloat(chance)) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "Drop chances must be a decimal number.");
				return;
			}
			
			toDrop = Float.parseFloat(chance);
			
			if(toDrop < 0 || toDrop > 100) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "Drop chances must be between 0 and 100.");
				return;
			}
			
			newStack.setDropChance(toDrop);
		} else if(chance.isEmpty() && split.length == 2) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must input a number for the drop chance.");
			return;
		}
		
		if(stack == null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + item + " is not a valid item.");
			return;
		}
		
		if(subCommand.equals("addinventoryitem")) {
			inv.addItem(newStack);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully added item " + ChatColor.GOLD + 
					PLUGIN.getItemName(newStack) + ChatColor.GREEN + " to entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "'s inventory!");
		} else if(subCommand.equals("setinventory")) {
			inv.getContent().clear();
			inv.addItem(newStack);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set " + ChatColor.GOLD + 
					PLUGIN.getItemName(newStack) + ChatColor.GREEN + " as entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "'s inventory!");
		} else if(subCommand.equals("sethand")) {
			inv.setHand(newStack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "item in hand", PLUGIN.getItemName(newStack)));
		} else if(subCommand.equals("sethelmet")) {
			inv.setHelmet(newStack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "helmet", PLUGIN.getItemName(newStack)));
		} else if(subCommand.equals("setchest")) {
			inv.setChest(newStack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "chestplate", PLUGIN.getItemName(newStack)));
		} else if(subCommand.equals("setleggings")) {
			inv.setLeg(newStack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "leggings", PLUGIN.getItemName(newStack)));
		} else if(subCommand.equals("setboots")) {
			inv.setBoot(newStack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "boots", PLUGIN.getItemName(newStack)));
		}
		
		entity.setInventory(inv);
		
	}

}
