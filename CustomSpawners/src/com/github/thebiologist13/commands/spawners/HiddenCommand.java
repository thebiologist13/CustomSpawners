package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class HiddenCommand extends SpawnerCommand {

	public HiddenCommand(CustomSpawners plugin) {
		super(plugin);
	}

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
		
		if(p.hasPermission("customspawners.spawners.sethidden") && arg3[0].equalsIgnoreCase("sethidden")) {
			//Set hidden for selection
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
			//Arguments entered for selection, but there is none
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//Set hidden of specific spawner
			} else if(arg3.length == 2) {

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
			
			//Set the value
			s.setHidden(true);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "hidden" + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
		if(p.hasPermission("customspawners.spawners.setunhidden") && arg3[0].equalsIgnoreCase("setunhidden")) {
			//Set unhidden for selection
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
			//Arguments entered for selection, but there is none
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//Set unhidden of specific spawner
			} else if(arg3.length == 2) {
				
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
			
			//Set the value
			s.setHidden(false);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "unhidden" + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
