package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.serialization.SInventory;

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
		
		String item = getValue(args, 0, "0"); //TODO if it is a player, make it so it can clone the item in their hand to inventory.
		String count = getValue(args, 1, "0");
		
		if(!CustomSpawners.isInteger(count)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must input an integer for the amount.");
			return;
		}
		
		ItemStack stack = PLUGIN.getItem(item, Integer.parseInt(count));
		
		if(stack == null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + item + " is not a valid item.");
			return;
		}
		
		if(subCommand.equals("addinventoryitem")) {
			inv.addItem(stack);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully added item " + ChatColor.GOLD + 
					PLUGIN.getItemName(stack) + ChatColor.GREEN + " to entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "'s inventory!");
		} else if(subCommand.equals("setinventory")) {
			inv.getContent().clear();
			inv.addItem(stack);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set " + ChatColor.GOLD + 
					PLUGIN.getItemName(stack) + ChatColor.GREEN + " as entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "'s inventory!");
		} else if(subCommand.equals("sethand")) {
			inv.setHand(stack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "item in hand", PLUGIN.getItemName(stack)));
		} else if(subCommand.equals("sethelmet")) {
			inv.setHelmet(stack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "helmet", PLUGIN.getItemName(stack)));
		} else if(subCommand.equals("setchest")) {
			inv.setChest(stack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "chestplate", PLUGIN.getItemName(stack)));
		} else if(subCommand.equals("setleggings")) {
			inv.setLeg(stack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "leggings", PLUGIN.getItemName(stack)));
		} else if(subCommand.equals("setboots")) {
			inv.setBoot(stack);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "boots", PLUGIN.getItemName(stack)));
		}
		
	}

}
