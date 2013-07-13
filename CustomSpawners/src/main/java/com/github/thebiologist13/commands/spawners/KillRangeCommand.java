package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class KillRangeCommand extends SpawnerCommand {

	public KillRangeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public KillRangeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("setkillrange")) {

			String in = getValue(args, 0, "64");

			if(!CustomSpawners.isDouble(in)) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The kill range must be a number.");
				return;
			}

			double range = Double.parseDouble(in);

			if(range < 0) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The kill range must be greater than 0.");
				return;
			}

			spawner.setKillRange(range);

			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set the kill range to " + ChatColor.GOLD +
					range + ChatColor.GREEN + " on spawner " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) +
					ChatColor.GREEN + ".");

		} else if(subCommand.equals("usekillrange")) {
			String in = getValue(args, 0, "false");
			spawner.setUseKillRange(Boolean.parseBoolean(in));

			PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "use kill range", in));
		}
		
	}

}
