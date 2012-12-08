package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityItemTypeCommand extends SpawnerCommand {

	public EntityItemTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Command Syntax = /customspawners setitemtype [id] <item ID>:<item metadata>
		//Array Index with selection           0                      1
		//Without selection                    0        1             2
		
		//Player
		Player p = null;
		//SpawnableEntity
		SpawnableEntity s = null;
		//Item Stack
		ItemStack item = null;
		//Perm
		String perm = "customspawners.entities.setitemtype";
		
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				String value = arg3[1];
				item = plugin.getItemStack(value);
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = CustomSpawners.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				String value = arg3[2];
				item = plugin.getItemStack(value);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Set
			if(item == null) {
				p.sendMessage(INVALID_ITEM);
				return;
			}
			
			s.setItemType(item);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the item type of spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ plugin.getItemName(item) + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
