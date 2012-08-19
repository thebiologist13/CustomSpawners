package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class SetLocationCommand extends SpawnerCommand {

	public SetLocationCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Location
		Location loc = null;
		
		//Spawner
		Spawner s = null;
		
		//Target block
		Block target = null;
		
		//Check for player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Permission
		if(p.hasPermission("customspawners.spawners.setlocation")) {
			//If the player wants to set the location of the selected spawner
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = plugin.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
				
			//If the arguments are for selection but nothing is selected
			} else if(arg3.length == 1) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//If the player wants to set the location of a spawner by ID 
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
			

			//Try to get the target block after scanning the distance from config
			target = p.getTargetBlock(null, plugin.getConfig().getInt("players.maxDistance", 50));
			
			loc = target.getLocation();
			
			//Set the new location
			s.setLoc(loc);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Set the location of the spawner with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN +
					" to " + ChatColor.GOLD + "(" + s.getLoc().getBlockX() + ", " + s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")" + 
					ChatColor.GREEN + "!");
 		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
