package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityEnderBlockCommand extends EntityCommand {

	public EntityEnderBlockCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityEnderBlockCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String item = getValue(args, 0, "0");
		String count = getValue(args, 1, "0");
		
		if(!CustomSpawners.isInteger(count)) {
			PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
			return;
		}
		
		ItemStack stack = PLUGIN.getItem(item, Integer.parseInt(count));
		
		if(stack == null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + item + " is not a valid item.");
			return;
		}
		
		entity.setEndermanBlock(stack.getData());
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "enderman block", PLUGIN.getItemName(stack)));
		
	}

}
