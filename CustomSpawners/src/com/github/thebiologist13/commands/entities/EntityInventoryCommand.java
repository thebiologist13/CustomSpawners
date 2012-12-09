package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;
import com.github.thebiologist13.serialization.SInventory;

public class EntityInventoryCommand extends SpawnerCommand {
	
	public EntityInventoryCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		Player p = null;
		String command = getAssignedCommand(arg3).toLowerCase();
		SpawnableEntity s = getSelectedEntity(arg0);
		final String PERMISSION = "customspawners.entities.inventory";
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(!p.hasPermission(PERMISSION)) {
			plugin.sendMessage(arg0, NO_PERMISSION);
		}
		
		if(command == null) {
			plugin.sendMessage(arg0, GENERAL_ERROR);
			return;
		}
		
		if(s == null) {
			if(arg3.length >= 2) {
				s = CustomSpawners.getEntity(arg3[1]);
			} else {
				plugin.sendMessage(arg0, NEEDS_SELECTION);			
			}
			
			if(s == null) {
				plugin.sendMessage(arg0, NO_ID);
			}
		}
		
		String value;
		ItemStack stack;
		
		if(arg3.length == 2) {
			value = arg3[1];
		} else if(arg3.length == 3) {
			value = arg3[2];
		} else {
			plugin.sendMessage(arg0, GENERAL_ERROR);
			return;
		}
		
		SInventory inv = new SInventory();
		stack = plugin.getItemStack(value);
		
		if(command.equals("clearinventory")) {
			s.setInventory(new SInventory());
			plugin.sendMessage(arg0, "Successfully cleared entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s inventory.");
		} else if(command.equals("addinventoryitem")) {
			s.addInventoryItem(stack);
			plugin.sendMessage(arg0, "Successfully added item to entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s inventory.");
		} else if(command.equals("setinventory")) {
			inv.addItem(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s inventory.");
		} else if(command.equals("sethelmet")) {
			inv = s.getInventory();
			inv.setHelmet(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s helmet.");
		} else if(command.equals("setchest")) {
			inv = s.getInventory();
			inv.setChest(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s chestplate.");
		} else if(command.equals("setleggings")) {
			inv = s.getInventory();
			inv.setLeg(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s leggings.");
		} else if(command.equals("setboots")) {
			inv = s.getInventory();
			inv.setBoot(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s boots.");
		} else if(command.equals("sethand")) {
			inv = s.getInventory();
			inv.setHand(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s item in hand.");
		} else {
			plugin.sendMessage(arg0, NOT_COMMAND);
		}
		
	}

}
