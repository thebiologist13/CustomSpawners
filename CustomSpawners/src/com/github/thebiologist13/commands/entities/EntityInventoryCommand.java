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
		
		if(arg3.length == 1) {
			plugin.sendMessage(arg0, LESS_ARGS);
			return;
		}
		
		if(!(arg0 instanceof Player)) {
			plugin.sendMessage(arg0, NO_CONSOLE);
			return;
		} else {
			p = (Player) arg0;
		}
		
		if(!p.hasPermission(PERMISSION)) {
			plugin.sendMessage(arg0, NO_PERMISSION);
			return;
		}
		
		if(command == null) {
			plugin.sendMessage(arg0, NOT_COMMAND);
			return;
		}
		
		if(command.equals("clearinventory")) {
			s.setInventory(new SInventory());
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully cleared entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s inventory.");
			return;
		}

		ItemStack stack;
		
		if(s == null) {
			if(arg3.length >= 3) {
				s = CustomSpawners.getEntity(arg3[1]);
			} else {
				plugin.sendMessage(arg0, NEEDS_SELECTION);	
				return;
			}
			
			if(s == null) {
				plugin.sendMessage(arg0, NO_ID);
				return;
			}
			
			stack = plugin.getItemStack(arg3[2]);
			
			if(arg3.length == 4)
				stack.setAmount(Integer.parseInt(arg3[3]));
		} else {
			stack = plugin.getItemStack(arg3[1]);
			
			if(arg3.length == 3)
				stack.setAmount(Integer.parseInt(arg3[2]));
		}
		
		if(stack == null) {
			plugin.sendMessage(arg0, INVALID_ITEM);
			return;
		}
		
		SInventory inv = new SInventory();
		
		if(command.equals("addinventoryitem")) {
			s.addInventoryItem(stack);
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully added item to entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s inventory.");
		} else if(command.equals("setinventory")) {
			inv.addItem(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s inventory.");
		} else if(command.equals("sethelmet")) {
			inv = s.getInventory();
			inv.setHelmet(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s helmet.");
		} else if(command.equals("setchest")) {
			inv = s.getInventory();
			inv.setChest(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s chestplate.");
		} else if(command.equals("setleggings")) {
			inv = s.getInventory();
			inv.setLeg(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s leggings.");
		} else if(command.equals("setboots")) {
			inv = s.getInventory();
			inv.setBoot(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s boots.");
		} else if(command.equals("sethand")) {
			inv = s.getInventory();
			inv.setHand(stack);
			s.setInventory(inv);
			plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s item in hand.");
		} else {
			plugin.sendMessage(arg0, NOT_COMMAND);
		}
		
	}

}
