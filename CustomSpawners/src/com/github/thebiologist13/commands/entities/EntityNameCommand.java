package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityNameCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	public EntityNameCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Spawner
		SpawnableEntity s = null;
		//Name
		String name = "";

		//Check that sender is a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		//Check permissions
		if(p.hasPermission("customspawners.entities.setname")) {

			//If the player wants to rename the selected spawner
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));
				
				name = arg3[1];

				//When the arg3 length is 1, but no spawner is selected.
			} else if(arg3.length == 2){

				p.sendMessage(NEEDS_SELECTION);
				return;

				//If the player want to rename a spawner with a specified ID
			} else if(arg3.length == 3) {

				//Check that the ID entered is a number
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				//Check if the ID entered is the ID of a spawner
				if(!plugin.isValidEntity(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}

				s = plugin.getEntityById(Integer.parseInt(arg3[1]));
				
				name = arg3[2];

				//General error message
			} else {

				plugin.log.info(GENERAL_ERROR);
				return;

			}

			//Remove the spawner by calling the remove() method
			s.setName(name);

			//Send success message
			p.sendMessage(ChatColor.GREEN + "Successfully set the name of entity with ID " + ChatColor.GOLD +
					String.valueOf(s.getId()) + ChatColor.GREEN + " to " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}

}
