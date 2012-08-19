package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityCatTypeCommand extends SpawnerCommand {

	public EntityCatTypeCommand(CustomSpawners plugin) {
		super(plugin);
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

				s = plugin.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				String value = arg3[1];
				
				if(value.equalsIgnoreCase("NONE")|| value.equalsIgnoreCase("WILD")) {
					type = "WILD_OCELOT";
				} else if(value.equalsIgnoreCase("RED_CAT") || value.equalsIgnoreCase("RED")|| value.equalsIgnoreCase("REDCAT")) {
					type = "RED_CAT";
				} else if(value.equalsIgnoreCase("BLACK_CAT") || value.equalsIgnoreCase("BLACK")|| value.equalsIgnoreCase("BLACKCAT")) {
					type = "BLACK_CAT";
				} else if(value.equalsIgnoreCase("SIAMESE_CAT") || value.equalsIgnoreCase("SIAMESE")|| value.equalsIgnoreCase("SIAMESECAT")) {
					type = "SIAMESE_CAT";
				} else {
					p.sendMessage(ChatColor.RED + value + " is not a valid cat type.");
					return;
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = plugin.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				String value = arg3[2];
				
				if(value.equalsIgnoreCase("NONE")|| value.equalsIgnoreCase("WILD")) {
					type = "WILD_OCELOT";
				} else if(value.equalsIgnoreCase("RED_CAT") || value.equalsIgnoreCase("RED")|| value.equalsIgnoreCase("REDCAT")) {
					type = "RED_CAT";
				} else if(value.equalsIgnoreCase("BLACK_CAT") || value.equalsIgnoreCase("BLACK")|| value.equalsIgnoreCase("BLACKCAT")) {
					type = "BLACK_CAT";
				} else if(value.equalsIgnoreCase("SIAMESE_CAT") || value.equalsIgnoreCase("SIAMESE")|| value.equalsIgnoreCase("SIAMESECAT")) {
					type = "SIAMESE_CAT";
				} else {
					p.sendMessage(ChatColor.RED + value + " is not a valid cat type.");
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
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ type + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
