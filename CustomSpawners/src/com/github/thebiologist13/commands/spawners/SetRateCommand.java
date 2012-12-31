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
		
		if(!CustomSpawners.isInteger(in)) {
			PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
			return;
		}
		
		int rate = Integer.parseInt(in);
		
		if(rate < 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The rate must be greater than zero.");
			return;
		}
		
		if(rate < CONFIG.getInt("spawners.rateLimit", 16) && !permissible(sender, "customspawners.limitoverride")) {
			PLUGIN.sendMessage(sender, NO_OVERRIDE);
			return;
		}
		
		spawner.setRate(rate);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "spawn rate", in + " ticks (" + PLUGIN.convertTicksToTime(rate) + ") "));
		
	}
	
}
