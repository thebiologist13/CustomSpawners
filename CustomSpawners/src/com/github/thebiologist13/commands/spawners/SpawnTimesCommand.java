package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SpawnTimesCommand extends SpawnerCommand {

	public SpawnTimesCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SpawnTimesCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("clearspawntime")) {
			spawner.setSpawnTimes(new ArrayList<Integer>());
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Cleared the spawn times of spawner " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(spawner));
			return;
		} 
		
		String in = getValue(args, 0, "all");
		
		if(in.equals("all") || in.equals("anytime") || in.equals("alltimes")) {
			spawner.setSpawnTimes(new ArrayList<Integer>());
			PLUGIN.sendMessage(sender, ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) +
					ChatColor.GREEN + " can now spawn anytime.");
			return;
		}
		
		if(subCommand.equals("setspawntime")) {
			spawner.setSpawnTimes(new ArrayList<Integer>());
		}
		
		if(in.indexOf(",") == -1) {
			if(!CustomSpawners.isInteger(in)) {
				PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
				return;
			}
			spawner.addTime(Integer.parseInt(in));
		}
		
		String[] timesStr = in.split(",");

		for(String s : timesStr) {
			
			if(!CustomSpawners.isInteger(s)) {
				PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
				return;
			}
			
			spawner.addTime(Integer.parseInt(s));
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Set the spawn times of spawner " + 
				ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " to " + 
				ChatColor.GOLD + in);
		
	}

}
