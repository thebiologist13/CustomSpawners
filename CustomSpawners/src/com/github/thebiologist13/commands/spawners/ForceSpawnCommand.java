package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public class ForceSpawnCommand extends SubCommand {

	public ForceSpawnCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {

			//Player has selection
			if(CustomSpawners.consoleSpawner != -1 && arg3.length == 1) {

				s = CustomSpawners.getSpawner(String.valueOf(CustomSpawners.consoleSpawner));

				//Player has no selection but has arguments for selection
			} else if(arg3.length == 1) {

				plugin.sendMessage(arg0, NEEDS_SELECTION);
				return;

				//Defined ID
			} else if(arg3.length == 2) {

				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					plugin.sendMessage(arg0, NO_ID);
					return;
				}

				//General Error
			} else {

				plugin.sendMessage(arg0, GENERAL_ERROR);
				return;

			}

			//Run the spawn method
			s.forceSpawn();
			
		} else {

			if(p.hasPermission("customspawners.spawners.forcespawn")) {
				
				//Player has selection
				if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
					
					s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
					
				//Player has no selection but has arguments for selection
				} else if(arg3.length == 1) {
					
					plugin.sendMessage(p, NEEDS_SELECTION);
					return;
					
				//Defined ID
				} else if(arg3.length == 2) {

					s = CustomSpawners.getSpawner(arg3[1]);

					if(s == null) {
						plugin.sendMessage(p, NO_ID);
						return;
					}
					
				//General Error
				} else {
					
					plugin.sendMessage(p, GENERAL_ERROR);
					return;
					
				}
				
				if(s.isHidden() && !p.hasPermission("customspawners.forcespawn.hidden")) {
					plugin.sendMessage(p, ChatColor.RED + "You are not allowed to force spawns for that spawner!");
					return;
				}
				
				//Run the spawn method
				s.forceSpawn();
				
			} else {
				plugin.sendMessage(p, NO_PERMISSION);
				return;
			}
			
		}
		
	}
	
}
