package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SetRadiusCommand extends SpawnerCommand {

	public SetRadiusCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SetRadiusCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "0");
		
		try {
			double radius = handleDynamic(in, spawner.getRadius());
			
			if(radius < 0) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The radius must be greater than zero.");
				return;
			}
			
			if(radius > CONFIG.getDouble("spawners.radiusLimit", 128) && !permissible(sender, "customspawners.limitoverride")) {
				PLUGIN.sendMessage(sender, NO_OVERRIDE);
				return;
			}
			
			spawner.setRadius(radius);
			
			PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "radius", in));
		} catch(IllegalArgumentException e) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The radius must be a number.");
		}
		
	}
	
}