package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.serialization.SItemStack;

public class EntityDropsCommand extends EntityCommand {

	public EntityDropsCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityDropsCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("usedrop")) {
			
			String in = getValue(args, 0, "false");
			entity.setUsingCustomDrops(Boolean.parseBoolean(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "use drops", in));
			
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
			
			stack = p.getItemInHand().clone();
			
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
		
		if(subCommand.equals("adddrop")) {
			
			if(!CustomSpawners.isInteger(count)) {
				PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
				return;
			}
			
			if(stack == null) {
				PLUGIN.sendMessage(sender, ChatColor.RED + item + " is not a valid item.");
				return;
			}
			
			entity.addDrop(newStack);
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully added drop item " + ChatColor.GOLD + 
					PLUGIN.getItemName(stack) + ChatColor.GREEN + " to entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
			
		} else if(subCommand.equals("setdrop")) {
			
			if(!CustomSpawners.isInteger(count)) {
				PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
				return;
			}
			
			if(stack == null) {
				PLUGIN.sendMessage(sender, ChatColor.RED + item + " is not a valid item.");
				return;
			}
			
			List<SItemStack> drops = new ArrayList<SItemStack>();
			drops.add(newStack);
			
			entity.setSItemStackDrops(drops);
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set drop item to " + ChatColor.GOLD + 
					PLUGIN.getItemName(stack) + ChatColor.GREEN + " on entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
			
		} else if(subCommand.equals("cleardrop")) {
			
			entity.setSItemStackDrops(new ArrayList<SItemStack>());
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully cleared drops of entity " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!");
			
		}
		
	}

}
