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
		
		try {
			entity.setProp("minecartSpeed", handleDynamic(in, (Double) (entity.getProp("minecartSpeed"))));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "minecart speed", in));
		} catch(IllegalArgumentException e) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "Minecart speed must be a decimal number.");
		}
		
	}

}
