package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityColorCommand extends SpawnerCommand {

	public EntityColorCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /customspawners setcolor [id] <color>
		//Array Index with selection           0             1
		//Without selection                    0      1      2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Color
		String color = "";
		//Permissions
		String perm = "customspawners.entities.setcolor";
		
		final String INVALID_COLOR = ChatColor.RED + "That is not a valid color.";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				DyeColor[] colors = DyeColor.values();
				boolean match = false;
				
				for(DyeColor d : colors) {
					if(arg3[1].equalsIgnoreCase(d.toString())) {
						color = d.toString();
						match = true;
						break;
					} else if(arg3[1].equalsIgnoreCase("lightblue")) {
						color = DyeColor.LIGHT_BLUE.toString();
						match = true;
						break;
					}
					
				}
				
				if(!match) {
					p.sendMessage(INVALID_COLOR);
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
				
				DyeColor[] colors = DyeColor.values();
				boolean match = false;
				
				for(DyeColor d : colors) {
					if(arg3[2].equalsIgnoreCase(d.toString())) {
						color = d.toString();
						match = true;
						break;
					} else if(arg3[2].equalsIgnoreCase("lightblue")) {
						color = DyeColor.LIGHT_BLUE.toString();
						match = true;
						break;
					}
					
				}
				
				if(!match) {
					p.sendMessage(INVALID_COLOR);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

			//Carry out command
			s.setColor(color);

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the color of spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ color + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
