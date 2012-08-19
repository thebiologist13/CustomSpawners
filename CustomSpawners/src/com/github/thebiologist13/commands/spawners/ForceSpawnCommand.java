package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class ForceSpawnCommand extends SpawnerCommand {

	public ForceSpawnCommand(CustomSpawners plugin) {
		super(plugin);
	}

	//TODO Force the spawner to spawn using the force spawn method of a certain type.
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Spawner
		Spawner s = null;
		
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission("customspawners.spawners.forcespawn")) {
			
			//Player has selection
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
			//Player has no selection but has arguments for selection
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//Defined ID
			} else if(arg3.length == 2) {

				s = plugin.getSpawner(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
			//General Error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			if(s.isHidden() && !p.hasPermission("customspawners.forcespawn.hidden")) {
				p.sendMessage(ChatColor.RED + "You are not allowed to force spawns for that spawner!");
				return;
			}
			
			//Run the spawn method
			s.forceSpawn();
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
