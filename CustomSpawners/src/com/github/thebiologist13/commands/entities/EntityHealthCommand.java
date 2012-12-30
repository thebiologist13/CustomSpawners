package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityHealthCommand extends EntityCommand {

	public EntityHealthCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityHealthCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "maximum");
		
		if(in.equals("maximum") || in.equals("max")) {
			entity.setAir(-1);
		} else if(in.equals("minimum") || in.equals("min")) {
			entity.setAir(-2);
		} else {
			if(!CustomSpawners.isInteger(in)) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "Health value must be an integer, \"maximum\", or \"minimum\".");
				return;
			}
			
			int health = Integer.parseInt(in);
			
			entity.setHealth(health);
		}
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "health", in));
		
	}

}
