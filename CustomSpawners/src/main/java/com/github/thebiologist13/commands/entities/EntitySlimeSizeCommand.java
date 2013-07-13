package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntitySlimeSizeCommand extends EntityCommand {

	public EntitySlimeSizeCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntitySlimeSizeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "1");
		
		if(!CustomSpawners.isInteger(in)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The size of a slime must be an integer.");
			return;
		}
		
		int size = Integer.parseInt(in);
		
		if(size < 1 || size > 256) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "A slime must be a size between 1 and 256.");
			return;
		}
		
		entity.setSlimeSize(size);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "slime size", in));
		
	}

}
