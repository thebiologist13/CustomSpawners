package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityPassiveCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityPassiveCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Value
		boolean passive = false;
		//Perm
		String perm = "customspawners.entities.setpassive";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p= (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));

				String value = arg3[1];

				if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					if(value.equals("true")) {
						passive = true;
					}
					//Doesn't need one for false, already set above
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
					return;
				}

			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}

				if(!plugin.isValidSpawner(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}

				s = plugin.getEntityById(Integer.parseInt(arg3[1]));

				String value = arg3[2];

				if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					if(value.equals("true")) {
						passive = true;
					}
					//Doesn't need one for false, already set above
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
					return;
				}

			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

			//Set
			s.setPassive(passive);

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the passive value of entity with ID " + 
					ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " to " + ChatColor.GOLD +
					String.valueOf(passive) + ChatColor.GREEN + "!");

		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}

}
