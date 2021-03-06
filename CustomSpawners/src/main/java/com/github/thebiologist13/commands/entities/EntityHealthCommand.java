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
			entity.setHealth(-1);
		} else if(in.equals("minimum") || in.equals("min")) {
			entity.setHealth(-2);
		} else {
			try {
				double health = handleDynamic(in, entity.getHealth(null));
				
				entity.setHealth(health);
			} catch(IllegalArgumentException e) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "Health value must be a number, \"maximum\", or \"minimum\".");
				return;
			}
		}
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "health", in));
		
	}

}
