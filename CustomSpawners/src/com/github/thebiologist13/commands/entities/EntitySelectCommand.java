package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntitySelectCommand extends SpawnerCommand {

	public EntitySelectCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//ID to select
		int selectionId = -1;
		
		//Make sure a player issued command
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		} 
		
		p = (Player) arg0;
		
		//Permission check
		if(p.hasPermission("customspawners.entities.select")) {
			
			if(arg3[1].equalsIgnoreCase("NONE")) {
				CustomSpawners.entitySelection.remove(p);
				p.sendMessage(ChatColor.GREEN + "You have deselected your entity.");
				return;
			}
			
			//Assign selectionId if a number
			SpawnableEntity s = plugin.getEntity(arg3[1]);
			
			if(s == null) {
				p.sendMessage(NO_ID);
				return;
			}
			
			selectionId = s.getId();
			
			//Put selectionId as Player's selected entities
			CustomSpawners.entitySelection.put(p, selectionId);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "You have selected entity " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + ".");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
