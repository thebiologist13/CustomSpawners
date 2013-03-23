package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityCartSpeedCommand extends EntityCommand {

	public EntityCartSpeedCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityCartSpeedCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "0.4");
		
		if(!CustomSpawners.isDouble(in)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The yield must be a double.");
			return;
		}
		
		entity.setProp("minecartSpeed", Double.parseDouble(in));
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "minecart speed", in));
		
	}

}
