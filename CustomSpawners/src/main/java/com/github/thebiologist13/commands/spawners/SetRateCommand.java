package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SetRateCommand extends SpawnerCommand {

	public SetRateCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SetRateCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "120");

		try {
			int rate = handleDynamic(in, spawner.getRate());

			if(rate < 0) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The rate must be greater than zero.");
				return;
			}

			if(rate < CONFIG.getInt("spawners.rateLimit", 10)) {
				if(warnLag(sender))
					return;
				if(!permissible(sender, "customspawners.limitoverride")) {
					PLUGIN.sendMessage(sender, NO_OVERRIDE);
					return;
				}
			}

			spawner.setRate(rate);

			PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "spawn rate", in + " ticks (" + 
					PLUGIN.convertTicksToTime(rate) + ") "));
		} catch(IllegalArgumentException e) {
			PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
		}

	}

}
