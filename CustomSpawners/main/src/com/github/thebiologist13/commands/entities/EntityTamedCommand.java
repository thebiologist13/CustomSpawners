package com.github.thebiologist13.commands.entities;

import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityTamedCommand extends EntityCommand {

	public EntityTamedCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityTamedCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		boolean value = Boolean.parseBoolean(in);
		entity.setAngry(!value);
		entity.setTamed(value);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "tamed", in));
	}

}
