package com.github.thebiologist13.commands.entities;

import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityAngryCommand extends EntityCommand {

	public EntityAngryCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityAngryCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		entity.setAngry(Boolean.parseBoolean(in));
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "angry", in));
	}

}