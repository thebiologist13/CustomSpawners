package com.github.thebiologist13.commands.spawners;

import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class TrackNearCommand extends SpawnerCommand {

	public TrackNearCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	public TrackNearCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		spawner.setTrackNearby(Boolean.parseBoolean(in));
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "track nearby mobs", in));
	}

}
