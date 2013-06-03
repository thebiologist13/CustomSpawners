package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityFireTicksCommand extends EntityCommand {

	public EntityFireTicksCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityFireTicksCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "0");
		
		try {
			int ticks = handleDynamic(in, entity.getFireTicks(null));
			entity.setFireTicks(ticks);
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "fire ticks", PLUGIN.convertTicksToTime(ticks)));
		} catch(IllegalArgumentException e) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The fire ticks must be an integer.");
		}
		
	}

}
