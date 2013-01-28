package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
		String count = getValue(args, 1, "1");
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
		
		if(stack == null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + item + " is not a valid item.");
			return;
		}
		
		entity.setItemType(stack);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "item type", PLUGIN.getItemName(stack)));
	}

}
