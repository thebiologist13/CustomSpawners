package com.github.thebiologist13.commands.spawners;

import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SpawnOnPowerCommand extends SpawnerCommand {

	public SpawnOnPowerCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SpawnOnPowerCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		boolean value = Boolean.parseBoolean(in);
		spawner.setSpawnOnRedstone(value);
		spawner.setRedstoneTriggered(value);
		
		if(value)
			CustomSpawners.redstoneSpawners.put(spawner.getLoc(), spawner.getId());
		else
			CustomSpawners.redstoneSpawners.remove(spawner.getLoc());
		
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "spawn on redstone trigger", in));
	}

}
