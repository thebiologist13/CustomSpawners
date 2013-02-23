package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityAgeCommand extends EntityCommand {

	public EntityAgeCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityAgeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String aIn = getValue(args, 0, "ADULT");
		
		if(aIn.equals("adult")) {
			entity.setAge(-1);
		} else if(aIn.equals("baby")) {
			entity.setAge(-2);
		} else {
			if(!CustomSpawners.isInteger(aIn)) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "Age value must be an integer, \"adult\", or \"baby\".");
				return;
			}
			
			int age = Integer.parseInt(aIn);
			
			entity.setAge(age);
		}
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "age", aIn));
		
	}

}