package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class MaxMobsCommand extends SpawnerCommand {

	public MaxMobsCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public MaxMobsCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "12");
		
		try {
			int maxMobs = handleDynamic(in, spawner.getMaxMobs());
			
			if(maxMobs < 0) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The maximum mobs must be greater than zero.");
				return;
			}
			
			if(maxMobs > CONFIG.getInt("spawners.maxMobsLimit", 256) && !permissible(sender, "customspawners.limitoverride")) {
				PLUGIN.sendMessage(sender, NO_OVERRIDE);
				return;
			}
			
			spawner.setMaxMobs(maxMobs);
			
			PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "maximum mobs", in));
		} catch(IllegalArgumentException e) {
			PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
		}
		
	}
	
}
