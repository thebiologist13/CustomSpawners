package com.github.thebiologist13.commands.spawners;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ActivateAllCommand extends SpawnerCommand {

	public ActivateAllCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public ActivateAllCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		Iterator<Spawner> itr = CustomSpawners.spawners.values().iterator();
		while(itr.hasNext()) {
			itr.next().setActive(true);
		}
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "All spawners set active.");
	}
	
}
