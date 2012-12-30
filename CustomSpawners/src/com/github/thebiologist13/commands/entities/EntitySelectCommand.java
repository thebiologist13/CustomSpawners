package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SubCommand;

public class EntitySelectCommand extends SubCommand {

	public EntitySelectCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//ID to select
		int selectionId = -1;
		
		final String deselect = ChatColor.GREEN + "You have deselected your entity.";
		
		//Make sure a player issued command
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		} 
		
		if(p == null) {
			
			if(arg3[1].equalsIgnoreCase("NONE")) {
				CustomSpawners.consoleEntity = -1;
				plugin.sendMessage(arg0, deselect);
				return;
			}
			
			//Assign selectionId if a number
			SpawnableEntity s = CustomSpawners.getEntity(arg3[1]);
			
			if(s == null) {
				plugin.sendMessage(arg0, NO_ID);
				return;
			}
			
			selectionId = s.getId();
			
			//Put selectionId as selected entity
			CustomSpawners.consoleEntity = selectionId;
			
			//Success message
			plugin.sendMessage(arg0, ChatColor.GREEN + "You have selected entity " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + ".");
			
		} else {

			//Permission check
			if(p.hasPermission("customspawners.entities.select")) {
				
				if(arg3[1].equalsIgnoreCase("NONE")) {
					CustomSpawners.entitySelection.remove(p);
					plugin.sendMessage(p, ChatColor.GREEN + "You have deselected your entity.");
					return;
				}
				
				//Assign selectionId if a number
				SpawnableEntity s = CustomSpawners.getEntity(arg3[1]);
				
				if(s == null) {
					plugin.sendMessage(p, NO_ID);
					return;
				}
				
				selectionId = s.getId();
				
				//Put selectionId as Player's selected entities
				CustomSpawners.entitySelection.put(p, selectionId);
				
				//Success message
				plugin.sendMessage(p, ChatColor.GREEN + "You have selected entity " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + ".");
			} else {
				plugin.sendMessage(p, NO_PERMISSION);
				return;
			}
			
		}
		
	}

}
