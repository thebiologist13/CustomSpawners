package com.github.thebiologist13.commands.spawners;

import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SetRedstoneCommand extends SpawnerCommand {

	public SetRedstoneCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SetRedstoneCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		spawner.setRedstoneTriggered(Boolean.parseBoolean(in));
		
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "redstone triggered", in));
	}
	
}
