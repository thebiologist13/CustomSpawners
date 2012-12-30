package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

@SuppressWarnings("unused")
public class SetTypeCommand extends SubCommand {

	public SetTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//New entity type
		SpawnableEntity type = null;
		
		//Spawner
		Spawner s = null;
		
		//Make sure it is a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission("customspawners.spawners.settype")) {
			//If they want to set the spawn type of a selected spawner
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
				type = CustomSpawners.getEntity(arg3[1]);
				
				if(type == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
			//Argument length is for a selected spawner, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//If they want to set spawn type by ID
			} else if(arg3.length == 3) {

				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				type = CustomSpawners.getEntity(arg3[2]);
				
				if(type == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			List<Integer> typeAsList = new ArrayList<Integer>();
			typeAsList.add(type.getId());
			
			//Set the new type
			s.setTypeData(typeAsList);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Successfully changed entity type of spawner with ID " + 
					ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD +
					plugin.getFriendlyName(type) + ChatColor.GREEN + "!");
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
