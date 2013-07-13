package com.github.thebiologist13.commands.spawners;

import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SpawnOnEnterCommand extends SpawnerCommand {

	public SpawnOnEnterCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SpawnOnEnterCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		boolean value = Boolean.parseBoolean(in);
		spawner.setSpawnOnEnter(value);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "spawn on player enter", in));
	}

}
