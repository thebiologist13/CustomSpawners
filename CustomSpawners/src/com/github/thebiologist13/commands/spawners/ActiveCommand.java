package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class ActiveCommand extends SpawnerCommand {

	private CustomSpawners plugin;
	
	public ActiveCommand(CustomSpawners plugin) {
		this.plugin = plugin;
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
		
		if(p.hasPermission("customspawners.spawners.setactive") && arg3[0].equalsIgnoreCase("setactive")) {
			//Set active for selection
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
			//Arguments entered for selection, but there is none
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//Set redstone powered of specific spawner
			} else if(arg3.length == 2) {
				
				s = plugin.getSpawner(arg3[1]);

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
			s.setActive(true);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + String.valueOf(s.getId()) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "active" + ChatColor.GREEN + "!");
			
		} else if(p.hasPermission("customspawners.spawners.setinactive") && arg3[0].equalsIgnoreCase("setinactive")) {
			//Set inactive for selection
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
			//Arguments entered for selection, but there is none
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//Set inactive of specific spawner
			} else if(arg3.length == 2) {

				s = plugin.getSpawner(arg3[1]);

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
			s.setActive(false);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the spawner with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + 
					ChatColor.GREEN + " to be " + ChatColor.GOLD + "inactive" + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
