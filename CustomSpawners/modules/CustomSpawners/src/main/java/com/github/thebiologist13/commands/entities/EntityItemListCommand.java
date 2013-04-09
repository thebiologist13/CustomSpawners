package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityItemListCommand extends EntityCommand {

	public EntityItemListCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityItemListCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String item = getValue(args, 0, "0");
		
		ItemStack stack = PLUGIN.getItem(item, 1);
		
		if(stack == null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + item + " is not a valid item.");
			return;
		}
		
		if(subCommand.equals("clearitemdamage")) {
			entity.setItemDamageList(new ArrayList<ItemStack>());
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully cleared item list on entity " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!");
		} else if(subCommand.equals("additemdamage")) {
			entity.addItemDamage(stack);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully added item " + ChatColor.GOLD + 
					PLUGIN.getItemName(stack) + ChatColor.GREEN + " to item list on entity " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!");
		} else if(subCommand.equals("setitemdamage")) {
			List<ItemStack> list = new ArrayList<ItemStack>();
			list.add(stack);
			entity.setItemDamageList(list);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set item " + ChatColor.GOLD + 
					PLUGIN.getItemName(stack) + ChatColor.GREEN + " as item list on entity " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!");
		}
		
	}

}
