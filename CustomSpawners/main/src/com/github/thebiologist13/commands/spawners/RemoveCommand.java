package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class RemoveCommand extends SpawnerCommand {

	public RemoveCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public RemoveCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		int id = spawner.getId();
		
		if(CONFIG.getBoolean("spawners.killOnRemove", true))
			PLUGIN.removeMobs(spawner);
		
		PLUGIN.removeSpawner(spawner);
		PLUGIN.getFileManager().removeDataFile(id, true);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Spawner " + ChatColor.GOLD + id + ChatColor.GREEN + " has been removed from the server.");
		
	}
	
}
