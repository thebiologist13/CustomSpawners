package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class RemoveMobsCommand extends SpawnerCommand {

	public RemoveMobsCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public RemoveMobsCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Removing mobs spawned by spawner " +
				ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + "...");
		
		PLUGIN.removeMobs(spawner);
		
	}
	
}
