package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityAirCommand extends EntityCommand {

	public EntityAirCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityAirCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String aIn = getValue(args, 0, "maximum");
		
		if(aIn.equals("maximum") || aIn.equals("max")) {
			entity.setAir(-1);
		} else if(aIn.equals("minimum") || aIn.equals("min")) {
			entity.setAir(-2);
		} else {
			try {
				int air = handleDynamic(aIn, entity.getAir(null));
				entity.setAir(air);
			} catch(IllegalArgumentException e) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "Air value must be an integer, \"maximum\", or \"minimum\".");
				return;
			}
		}
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "air", aIn));
		
	}

}