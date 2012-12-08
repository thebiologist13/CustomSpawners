package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class AddTypeCommand extends SpawnerCommand {

	public AddTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
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
		
		if(p.hasPermission("customspawners.spawners.addtype")) {
			//If they want to set the spawn type of a selected spawner
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
				type = CustomSpawners.getEntity(arg3[1]);
				
				if(type == null) {
					p.sendMessage(ENTITY_NONEXISTANT);
					return;
				}
				
			//Argument length is for a selected spawner, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//If they want to set spawn type by ID
			} else if(arg3.length == 3) {
			
				type = CustomSpawners.getEntity(arg3[2]);
				
				if(type == null) {
					p.sendMessage(ENTITY_NONEXISTANT);
					return;
				}

				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			//Set the new type
			s.addTypeData(type);
			
			//Success message
			if(!type.getName().isEmpty()) {
				p.sendMessage(ChatColor.GREEN + "Added spawnable entity with ID " + ChatColor.GOLD + String.valueOf(type.getId()) + " (" + type.getName() + ")"
						+ ChatColor.GREEN + " to spawner with ID " + ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + "!");
			} else {
				p.sendMessage(ChatColor.GREEN + "Added spawnable entity with ID " + ChatColor.GOLD + String.valueOf(type.getId()) 
						+ ChatColor.GREEN + " to spawner with ID " + ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + "!");
			}
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}

}
