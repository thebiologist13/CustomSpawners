package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityItemTypeCommand extends EntityCommand {

	public EntityItemTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityItemTypeCommand(CustomSpawners plugin, String mainPerm) {
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
		
		entity.setItemType(stack);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "item type", PLUGIN.getItemName(stack)));
	}

}
