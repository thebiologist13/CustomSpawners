package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityCatTypeCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityCatTypeCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /customspawners setcattype [id] <type>
		//Array Index with selection           0              1
		//Without selection                    0       1      2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Type
		String type = "";
		//Permissions
		String perm = "customspawners.entities.setcattype";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));
				
				if(arg3[1].equalsIgnoreCase("NONE")) {
					type = "WILD_OCELOT";
				} else if(arg3[1].equalsIgnoreCase("RED_CAT") || arg3[1].equalsIgnoreCase("RED")) {
					type = "RED_CAT";
				} else if(arg3[1].equalsIgnoreCase("BLACK_CAT") || arg3[1].equalsIgnoreCase("BLACK")) {
					type = "BLACK_CAT";
				} else if(arg3[1].equalsIgnoreCase("SIAMESE_CAT") || arg3[1].equalsIgnoreCase("SIAMESE")) {
					type = "SIAMESE_CAT";
				} else {
					p.sendMessage(ChatColor.RED + arg3[1] + " is not a valid cat type.");
					return;
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				int id = 0;

				//Check that the ID entered is a number
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}

				id = Integer.parseInt(arg3[1]);

				//Check if the ID entered is the ID of a entity
				if(!plugin.isValidEntity(id)) {
					p.sendMessage(NO_ID);
					return;
				}

				s = plugin.getEntityById(id);
				
				if(arg3[2].equalsIgnoreCase("NONE")) {
					type = "WILD_OCELOT";
				} else if(arg3[2].equalsIgnoreCase("RED_CAT") || arg3[2].equalsIgnoreCase("RED")) {
					type = "RED_CAT";
				} else if(arg3[2].equalsIgnoreCase("BLACK_CAT") || arg3[2].equalsIgnoreCase("BLACK")) {
					type = "BLACK_CAT";
				} else if(arg3[2].equalsIgnoreCase("SIAMESE_CAT") || arg3[2].equalsIgnoreCase("SIAMESE")) {
					type = "SIAMESE_CAT";
				} else {
					p.sendMessage(ChatColor.RED + arg3[2] + " is not a valid cat type.");
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

			//Carry out command
			s.setCatType(type);
			if(!type.equals("WILD_OCELOT")) {
				s.setTamed(true);
				s.setAngry(false);
			}

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the cat type of spawnable entity with ID " 
					+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ type + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
