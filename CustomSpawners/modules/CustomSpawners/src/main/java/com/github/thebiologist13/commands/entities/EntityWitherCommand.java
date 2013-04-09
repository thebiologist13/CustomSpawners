package com.github.thebiologist13.commands.entities;

import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityWitherCommand extends EntityCommand {

	public EntityWitherCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityWitherCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		entity.setProp("wither", Boolean.parseBoolean(in));
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "wither", in));
	}

}
