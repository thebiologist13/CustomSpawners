package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SubCommand;

public class EntityProfessionCommand extends SubCommand {

	public EntityProfessionCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /customspawners setprofession [id] <type>
		//Array Index with selection             0                1
		//Without selection                      0        1       2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Type
		String type = "";
		//Permissions
		String perm = "customspawners.entities.setprofession";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				if(arg3[1].equalsIgnoreCase("BLACKSMITH")) {
					type = "BLACKSMITH";
				} else if(arg3[1].equalsIgnoreCase("BUTCHER")) {
					type = "BUTCHER";
				} else if(arg3[1].equalsIgnoreCase("FARMER")) {
					type = "FARMER";
				} else if(arg3[1].equalsIgnoreCase("LIBRARIAN")) {
					type = "LIBRARIAN";
				} else if(arg3[1].equalsIgnoreCase("PRIEST")) {
					type = "PRIEST";
				} else {
					p.sendMessage(ChatColor.RED + arg3[1] + " is not a valid villager profession.");
					return;
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = CustomSpawners.getEntity(arg3[1]);
				
				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(arg3[2].equalsIgnoreCase("BLACKSMITH")) {
					type = "BLACKSMITH";
				} else if(arg3[2].equalsIgnoreCase("BUTCHER")) {
					type = "BUTCHER";
				} else if(arg3[2].equalsIgnoreCase("FARMER")) {
					type = "FARMER";
				} else if(arg3[2].equalsIgnoreCase("LIBRARIAN")) {
					type = "LIBRARIAN";
				} else if(arg3[2].equalsIgnoreCase("PRIEST")) {
					type = "PRIEST";
				} else {
					p.sendMessage(ChatColor.RED + arg3[1] + " is not a valid villager profession.");
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

			//Carry out command
			Villager.Profession profession = Villager.Profession.valueOf(type);
			s.setProfession(profession);

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the profession of spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ type + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
