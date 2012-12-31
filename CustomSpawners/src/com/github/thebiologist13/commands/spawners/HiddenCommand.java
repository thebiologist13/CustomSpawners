package com.github.thebiologist13.commands.spawners;

import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class HiddenCommand extends SpawnerCommand {

	public HiddenCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public HiddenCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "false");
		spawner.setHidden(Boolean.parseBoolean(in));
		
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "hidden", in));
	}
}
