package com.github.thebiologist13.commands.spawners;

import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class CappedCommand extends SpawnerCommand {

	public CappedCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public CappedCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		spawner.setCapped(Boolean.parseBoolean(in));
		
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "capped", in));
	}

}
