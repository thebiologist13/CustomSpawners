package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityDropsCommand extends SpawnerCommand {

	public EntityDropsCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//SpawnableEntity
		SpawnableEntity s = null;
		//Perms
		String perm1 = "customspawners.entities.adddrop";
		String perm2 = "customspawners.entities.setdrop";
		String perm3 = "customspawners.entities.cleardrops";
		String perm4 = "customspawners.entities.setusingdrops";
		
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;
		
		if(p.hasPermission(perm1) && arg3[0].equalsIgnoreCase("adddrop")) {
			
			ItemStack drop = null;
			int amount = 1;
			
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 3) {

				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				drop = plugin.getItemStack(arg3[1]);
				
				if(drop == null) {
					p.sendMessage(ChatColor.RED + arg3[1] + " is not a valid item.");
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[2])) {
					p.sendMessage(ChatColor.RED + "You must input an integer for the amount.");
					return;
				}
				
				amount = Integer.parseInt(arg3[2]);
				
			} else if(arg3.length == 3) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 4) {

				s = CustomSpawners.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				drop = plugin.getItemStack(arg3[2]);
				
				if(drop == null) {
					p.sendMessage(ChatColor.RED + arg3[2] + " is not a valid item.");
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[3])) {
					p.sendMessage(ChatColor.RED + "You must input an integer for the amount.");
					return;
				}
				
				amount = Integer.parseInt(arg3[3]);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			drop.setAmount(amount);
			s.addDrop(drop);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully added a drop to spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "! Item Details: " + ChatColor.GOLD 
					+ plugin.getItemName(drop) + ChatColor.GREEN + ".");
			
		} else if(p.hasPermission(perm2) && arg3[0].equalsIgnoreCase("setdrops")) {
			
			ItemStack drop = null;
			int amount = 1;
			
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 3) {

				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				drop = plugin.getItemStack(arg3[1]);
				
				if(drop == null) {
					p.sendMessage(ChatColor.RED + arg3[1] + " is not a valid item.");
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[2])) {
					p.sendMessage(ChatColor.RED + "You must input an integer for the amount.");
					return;
				}
				
				amount = Integer.parseInt(arg3[2]);
				
			} else if(arg3.length == 3) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 4) {

				s = CustomSpawners.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				drop = plugin.getItemStack(arg3[2]);
				
				if(drop == null) {
					p.sendMessage(ChatColor.RED + arg3[2] + " is not a valid item.");
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[3])) {
					p.sendMessage(ChatColor.RED + "You must input an integer for the amount.");
					return;
				}
				
				amount = Integer.parseInt(arg3[3]);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			drop.setAmount(amount);
			ArrayList<ItemStack> dropAsArray = new ArrayList<ItemStack>();
			dropAsArray.add(drop);
			s.setDrops(dropAsArray);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set drop of spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ plugin.getItemName(drop) + ChatColor.GREEN + "!");
			
		} else if(p.hasPermission(perm3) && arg3[0].equalsIgnoreCase("cleardrops")) {
			
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 1) {

				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
			} else if(arg3.length == 1) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 2) {

				s = CustomSpawners.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			ArrayList<ItemStack> dropAsArray = new ArrayList<ItemStack>();
			s.setDrops(dropAsArray);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully cleared spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s drops!");
			
		} else if(p.hasPermission(perm4) && arg3[0].equalsIgnoreCase("setusingdrops")) {
			
			boolean value = false;
			
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				if(arg3[1].equalsIgnoreCase("true") || arg3[1].equalsIgnoreCase("false")) {
					if(arg3[1].equals("true")) {
						value = true;
					}
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
					return;
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {
				
				s = CustomSpawners.getEntity(arg3[1]);
				
				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(arg3[2].equalsIgnoreCase("true") || arg3[2].equalsIgnoreCase("false")) {
					if(arg3[2].equals("true")) {
						value = true;
					}
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			s.setUsingCustomDrops(value);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set entity " + ChatColor.GOLD + plugin.getFriendlyName(s) + 
					ChatColor.GREEN + "'s custom drops value to " + ChatColor.GOLD + String.valueOf(value) + ChatColor.GREEN + "!");
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
