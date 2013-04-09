package com.github.thebiologist13.commands.spawners;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class RemoveAllMobsCommand extends SpawnerCommand {

	public RemoveAllMobsCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public RemoveAllMobsCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Removing all spawned mobs...");
		
		Iterator<Spawner> itr = CustomSpawners.spawners.values().iterator();
		while(itr.hasNext()) {
			PLUGIN.removeMobs(itr.next());
		}
		
	}

}
