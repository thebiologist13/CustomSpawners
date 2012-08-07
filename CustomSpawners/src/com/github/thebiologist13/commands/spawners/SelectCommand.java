package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class SelectCommand extends SpawnerCommand {

	private CustomSpawners plugin;
	
	public SelectCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Spawner to select
		Spawner s = null;
		
		//Make sure a player issued command
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		} 
		
		p = (Player) arg0;
		
		//Permission check
		if(p.hasPermission("customspawners.spawners.select")) {
			
			if(arg3[1].equalsIgnoreCase("NONE")) {
				CustomSpawners.spawnerSelection.remove(p);
				p.sendMessage(ChatColor.GREEN + "You have deselected your spawner.");
				return;
			}
			
			//Assign selectionId if a number
			s = plugin.getSpawner(arg3[1]);

			if(s == null) {
				p.sendMessage(NO_ID);
				return;
			}
			
			//Put selectionId as Player's selected spawner
			CustomSpawners.spawnerSelection.put(p, s.getId());
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "You have selected spawner " + ChatColor.GOLD + 
					plugin.getFriendlyName(s) + ChatColor.GREEN + ".");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
