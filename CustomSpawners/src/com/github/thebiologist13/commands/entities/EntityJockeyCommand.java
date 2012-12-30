package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SubCommand;

public class EntityJockeyCommand extends SubCommand {

	public EntityJockeyCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /entities setjockey [id] <true or false>
		//Array Index with selection    0                  1
		//Without selection             0         1        2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Value
		boolean value = false;
		//Permissions
		String perm = "customspawners.entities.setjockey";
		
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				if(arg3[1].equalsIgnoreCase("true") || arg3[1].equalsIgnoreCase("false")) {
					if(arg3[1].equals("true")) {
						value = true;
					}
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
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
				
				if(arg3[2].equalsIgnoreCase("true") || arg3[2].equalsIgnoreCase("false")) {
					if(arg3[2].equals("true")) {
						value = true;
					}
				} else {
					p.sendMessage(MUST_BE_BOOLEAN);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			s.setJockey(value);
			if(value) {
				s.setType(EntityType.SPIDER);
			}
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the jockey value of entity with ID " + 
					ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD +
					String.valueOf(value) + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}

	}

}
