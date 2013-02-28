package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class PlayerDistanceCommand extends SpawnerCommand {

	public PlayerDistanceCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public PlayerDistanceCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "0");
		
		if(!CustomSpawners.isDouble(in)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The distance must be a number.");
			return;
		}
		
		double dis = Double.parseDouble(in);
		
		if(dis < 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The distance must be greater than zero.");
			return;
		}
		
		if(dis > CONFIG.getDouble("spawners.playerDistanceLimit", 128) && !permissible(sender, "customspawners.limitoverride")) {
			PLUGIN.sendMessage(sender, NO_OVERRIDE);
			return;
		}
		
		if(subCommand.equals("setmaxdistance")) {
			
			spawner.setMaxPlayerDistance(dis);
			
			PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "max player distance", in));
			
		} else if(subCommand.equals("setmindistance")) {
			
			spawner.setMinPlayerDistance(dis);
			
			PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "min player distance", in));
			
		}
		
	}
}
