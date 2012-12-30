package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public class RemoveCommand extends SubCommand {

	public RemoveCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Spawner to remove
		Spawner s = null;
		
		//Check that sender is a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Check permissions
		if(p.hasPermission("customspawners.spawners.remove")) {
			
			//If the player wants to remove the selected spawner
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 1) {
				
				s = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
			
			//When the arg3 length is 1, but no spawner is selected.
			} else if(arg3.length == 1){
					
					p.sendMessage(NEEDS_SELECTION);
					return;
					
			//If the player want to remove a spawner with a specified ID
			} else if(arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(arg3[1]);
			
			//General error message
			} else {
				
				plugin.log.info(GENERAL_ERROR);
				return;
				
			}
			
			//Check if the ID entered is the ID of a spawner
			if(s == null) {
				p.sendMessage(NO_ID);
				return;
			}
			
			//Remove the spawner by calling the remove() method
			plugin.removeSpawner(s);
			plugin.getFileManager().removeDataFile(s.getId(), true);
			
			//Send success message
			p.sendMessage(ChatColor.GREEN + "Successfully removed spawner with ID " + ChatColor.GOLD + s.getId() + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
