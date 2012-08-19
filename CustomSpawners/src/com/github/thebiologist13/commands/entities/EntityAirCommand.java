package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityAirCommand extends SpawnerCommand {

	public EntityAirCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /entities setair [id] <health>
		//Array Index with selection    0           1
		//Without selection             0     1     2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Permissions
		String perm = "customspawners.entities.setair";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				if(arg3[1].equalsIgnoreCase("MAXIMUM")) {
					s.setAir(-1);
				} else if(arg3[1].equalsIgnoreCase("MINIMUM")) {
					s.setAir(-2);
				} else {
					if(plugin.isInteger(arg3[1])) {
						s.setAir(Integer.parseInt(arg3[1]));
					} else {
						p.sendMessage(ChatColor.RED + "Air value must be an integer, \"MAXIMUM\", or \"MINIMUM\".");
						return;
					}
				}
				
				//Success
				p.sendMessage(ChatColor.GREEN + "Successfully set the air of spawnable entity with ID " 
						+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD 
						+ arg3[1] + ChatColor.GREEN + "!");
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = plugin.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(arg3[1].equalsIgnoreCase("MAXIMUM")) {
					s.setAir(-1);
				} else if(arg3[1].equalsIgnoreCase("MINIMUM")) {
					s.setAir(-2);
				} else {
					if(plugin.isInteger(arg3[1])) {
						s.setAir(Integer.parseInt(arg3[1]));
					} else {
						p.sendMessage(ChatColor.RED + "Air value must be an integer, \"MAXIMUM\", or \"MINIMUM\".");
						return;
					}
				}
				
				//Success
				p.sendMessage(ChatColor.GREEN + "Successfully set the air of spawnable entity with ID " 
						+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
						+ arg3[2] + ChatColor.GREEN + "!");
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
